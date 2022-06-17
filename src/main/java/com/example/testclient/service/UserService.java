package com.example.testclient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testclient.entity.User;
import com.example.testclient.repository.UserRepository;
import com.razorpay.Order;

@Service
public class UserService {
	
	@Autowired
	UserRepository userrepo;

	public User findByUserMail(String email) {
		// TODO Auto-generated method stub
		return userrepo.findByEmail(email);
	}

	public void saveToken(String jwt, String username) {
		// TODO Auto-generated method stub
		User uu = userrepo.findByEmail(username);
		uu.setToken(jwt);
		userrepo.save(uu);
	}
	
}
