package com.example.testclient.errorHandler;

import java.lang.Exception;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class DepartmentNotFoundException extends Exception{
	
	//final Logger logger = LoggerFactory.getLogger(DepartmentNotFoundException.class);
	
	public DepartmentNotFoundException() {
		super();
	//	logger.debug("in logger");
	}
	
	public DepartmentNotFoundException(String message) {
		super(message);
//		logger.debug("in logger");
	}
	
	public DepartmentNotFoundException(String message, Throwable cause) {
		super(message,cause);
	//	logger.debug("in logger");
	}
	
	public DepartmentNotFoundException(Throwable cause) {
		super(cause);
	//	logger.debug("in logger");
	}
	
	protected DepartmentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	    super(message, cause, enableSuppression, writableStackTrace);
	//    logger.debug("in logger");
	}
}
