package com.example.testclient.service;


import com.example.testclient.entity.Department;
import com.example.testclient.errorHandler.DepartmentNotFoundException;

public interface DepartmentSpace {

	public Department getDeptById(long id) throws DepartmentNotFoundException;
	
}
