package com.example.testclient.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.testclient.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
	
}
