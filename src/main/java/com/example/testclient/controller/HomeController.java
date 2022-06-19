package com.example.testclient.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.testclient.authenticationModel.AuthenticationRequest;
import com.example.testclient.authenticationModel.AuthenticationResponse;
import com.example.testclient.customUser.CustomUserDetailsService;
import com.example.testclient.entity.Department;
import com.example.testclient.entity.User;
import com.example.testclient.errorHandler.DepartmentNotFoundException;
import com.example.testclient.service.DepartmentService;
import com.example.testclient.service.DepartmentSpace;
import com.example.testclient.service.UserService;
import com.example.testclient.util.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HomeController {

	@Autowired
	DepartmentService departmentservice;
	
	@Autowired
	UserService userservic;

	@Autowired
	AuthenticationManager authenticationmanager;

	@Autowired
	private CustomUserDetailsService userdetailsservice;
	
	@Autowired
	private DepartmentSpace deptspace;

	@Autowired
	private JwtUtil jwtTokenUtil;

	
	private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/all")
	public List<Department> home() throws DepartmentNotFoundException{
		return departmentservice.findAllDepartment(); 
	}

	@PostMapping("/add")
	public Department addDepartment(Department department,@RequestParam("image") List<MultipartFile> multipartFile) throws DepartmentNotFoundException, IOException{
		//System.out.println();
		Department dd = departmentservice.addDepartment(department, multipartFile);
		return dd;
	}

	@PostMapping("delete/{id}")
	public String deleteDepartment(@PathVariable("id") long id) throws DepartmentNotFoundException, IOException{
		departmentservice.deleteDepartment(id);
		return "deleted";
	}
	
	@GetMapping("allstore/{id}")
	public Department getStoreById(@PathVariable("id") long id) throws DepartmentNotFoundException {
		return deptspace.getDeptById(id);
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/registerapii", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationReq)
		throws Exception {
		try {
			this.authenticationmanager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationReq.getUsername(), authenticationReq.getPassword()));
		} catch (BadCredentialsException e) {
			System.out.println("error is " + e);
			throw new Exception("Incorrect username password", e);
		}
		final UserDetails userdetails = userdetailsservice.loadUserByUsername(authenticationReq.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userdetails);
		userservic.saveToken(jwt, authenticationReq.getUsername());
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	@GetMapping("/verify")
	public String verifyUser(@Param("code") String code) {
	    if (departmentservice.verify(code)) {
	        return "verify_success";
	    } else {
	        return "verify_fail";
	    }
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/process_register")
	public String processRegister(User user, HttpServletRequest request)
			throws UnsupportedEncodingException, MessagingException {
		departmentservice.register(user, getSiteURL(request));
		return "register_success";
	}
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/forgotpassword/{username}")
	public ResponseEntity<?> forgotPassword(@PathVariable("username") String username, HttpServletRequest request) {

		String response = departmentservice.forgotPassword(username, getSiteURL(request));
 
		if (!response.startsWith("Invalid")) { 
			response = "http://localhost:9001/resetpassword?token=" + response;
		}
		return ResponseEntity.ok(response);
	}

	@PutMapping("/resetpassword")
	public String resetPassword(@RequestParam String token,
			@RequestParam String password) {

		return departmentservice.resetPassword(token, password);
	}
	@Autowired
	public UserService userservice;
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/getmail/{mail}")
	public User verific(@PathVariable("mail") String mailId) {
		User uu = userservice.findByUserMail(mailId);
		System.out.println("called");
		return uu;
	}
	
	private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        System.out.println(siteURL.replace(request.getServletPath(), ""));
        return siteURL.replace(request.getServletPath(), "");
    }
	
	
}
