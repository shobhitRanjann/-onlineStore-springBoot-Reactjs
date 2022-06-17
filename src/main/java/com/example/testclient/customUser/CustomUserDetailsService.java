package com.example.testclient.customUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.testclient.entity.User;
import com.example.testclient.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		return null;
//	}  // we get this method from UserDetailsService

	 	@Autowired
	    private UserRepository userRepo;

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = userRepo.findByEmail(username);
	        if (user == null) {
	        	System.out.println("inside exception");
	            throw new UsernameNotFoundException("User not found");
	        }
	        return new CustomUserDetails(user);
	    }
}