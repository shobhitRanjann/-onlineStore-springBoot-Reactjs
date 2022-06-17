package com.example.testclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.testclient.entity.User;
import com.example.testclient.errorHandler.DepartmentNotFoundException;

public interface UserRepository extends JpaRepository<User, Long>{

	public User findByEmail(String username);

	public User findByVerificationCode(String verificationCode);

}
