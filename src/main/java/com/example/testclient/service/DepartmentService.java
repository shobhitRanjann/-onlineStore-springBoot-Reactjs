package com.example.testclient.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.testclient.entity.Department;
import com.example.testclient.entity.User;
import com.example.testclient.errorHandler.DepartmentNotFoundException;
import com.example.testclient.repository.DepartmentRepository;
import com.example.testclient.repository.UserRepository;
import com.example.testclient.util.FileUploadUtil;

import net.bytebuddy.utility.RandomString;

@Service
public class DepartmentService implements DepartmentSpace{
	@Autowired
	DepartmentRepository departmentrepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
     
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
   	private UserRepository repo;
    
	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 30;


	public List<Department> findAllDepartment() {
		// TODO Auto-generated method stub
		return departmentrepo.findAll();
	}

	public Department addDepartment(Department department, List<MultipartFile> multipartFile) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("multipart tostirng"+multipartFile.size());
		
		String imgname="";
		for(int i=0;i<multipartFile.size();i++) {
			String fileName = StringUtils.cleanPath(multipartFile.get(i).getOriginalFilename());
			if(i==0) {
				imgname=fileName;
			}
			else {
				imgname=imgname +","+ fileName;
			}
			
		}
		department.setPhotos(imgname);
		Department departmentre = departmentrepo.save(department);
		String uploadDir = "dept_photos/" + department.getId();
		for(int i=0;i<multipartFile.size();i++) {
			String fileName = StringUtils.cleanPath(multipartFile.get(i).getOriginalFilename());
			 FileUploadUtil.saveFile(uploadDir, fileName, multipartFile.get(i));
		}
		return departmentre;
	}

	public void deleteDepartment(long id) throws DepartmentNotFoundException, IOException {
		// TODO Auto-generated method stub
		Optional<Department> storeent = departmentrepo.findById(id);
		if(!storeent.isPresent()) {
			throw new DepartmentNotFoundException("Department not found");
		}
		Path uploadPath = Paths.get("dept_photos/"+id);
		for(File file: new java.io.File("dept_photos/"+id).listFiles()) 
		    if (!file.isDirectory()) 
		        file.delete();
		Files.deleteIfExists(uploadPath);

		departmentrepo.deleteById(id);
	}
	
	@Override
	public Department getDeptById(long id) throws DepartmentNotFoundException {
		Optional<Department> storeent = departmentrepo.findById(id);
		
		if(!storeent.isPresent()) {
			throw new DepartmentNotFoundException("Department not found");
		}
		return storeent.get();
	}

	 public void register(User user, String siteURL)
	            throws UnsupportedEncodingException, MessagingException {
	        String encodedPassword = passwordEncoder.encode(user.getPassword());
	        user.setPassword(encodedPassword);
	        System.out.println("encoded password >>>>>> : Department service :"+encodedPassword);
	        String randomCode = RandomString.make(64);
	        user.setVerificationCode(randomCode);
	        user.setEnabled(false);

	        repo.save(user);

	        sendVerificationEmail(user, siteURL);
	    }
	 private void sendVerificationEmail(User user, String siteURL)
	            throws MessagingException, UnsupportedEncodingException {
	        String toAddress = user.getEmail();
	        String fromAddress = "javamailsender12@gmail.com";
	        String senderName = "Indiana";
	        String subject = "Please verify your registration";
	        String content = "Dear [[name]],<br>"
	                + "Please click the link below to verify your registration:<br>"
	                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
	                + "Thank you,<br>"
	                + "Your company name.";
	         
	        MimeMessage message = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	        helper.setFrom(fromAddress, senderName);
	        helper.setTo(toAddress);
	        helper.setSubject(subject);
	         
	        content = content.replace("[[name]]", user.getFirstName());
	        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
	         
	        content = content.replace("[[URL]]", verifyURL);
	         
	        helper.setText(content, true);
	         
	        mailSender.send(message);
	         
	    }
	    public boolean verify(String verificationCode) {
	        User user = repo.findByVerificationCode(verificationCode);
	         
	        if (user == null || user.isEnabled()) {
	            return false;
	        } else {
	            user.setVerificationCode(null);
	            user.setEnabled(true);
	            repo.save(user);
	             
	            return true;
	        }
	    }
	    
	    @Autowired
		private UserRepository userRepository;

		public String forgotPassword(String username, String siteUrl){
			
			Optional<User> userOptional = Optional
					.ofNullable(userRepository.findByEmail(username));

			if (!userOptional.isPresent()) {
				return "Invalid email id.";
			}
			User user = userOptional.get();
			user.setVerificationCode(generateToken());
			user.setTokenCreationDate(LocalDateTime.now());
			System.out.println("verification >> "+user.getVerificationCode());
			user = userRepository.save(user);
			String verificationcode = user.getVerificationCode().substring(0, user.getVerificationCode().length()-8);  // check
			if(user.isEnabled()) {
				try {
					sendVerificationEmail(user, siteUrl);
				} catch (UnsupportedEncodingException | MessagingException e) {
					e.printStackTrace();
				}
			}
			return verificationcode;
		}

		public String resetPassword(String token, String password) {

			Optional<User> userOptional = Optional
					.ofNullable(userRepository.findByVerificationCode(token));

			if (!userOptional.isPresent()) {
				return "Invalid token.";
			}
			LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();
			if (isTokenExpired(tokenCreationDate)) {
				return "Token expired.";
			}
			User user = userOptional.get();
			String encodedPassword = passwordEncoder.encode(password);
		    user.setPassword(encodedPassword);
			user.setVerificationCode(null);
			userRepository.save(user);
			return "Your password successfully updated.";
		}
		private String generateToken() {
			StringBuilder token = new StringBuilder();

			return token.append(UUID.randomUUID().toString())
					.append(UUID.randomUUID().toString()).toString();
		}
		private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {
			LocalDateTime now = LocalDateTime.now();
			Duration diff = Duration.between(tokenCreationDate, now);
			return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
		}

		public Optional<Department> findDepartmentById(Long id) {
			return departmentrepo.findById(id);
		}
}