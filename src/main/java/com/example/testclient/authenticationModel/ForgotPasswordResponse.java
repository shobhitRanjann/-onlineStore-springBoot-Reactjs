package com.example.testclient.authenticationModel;

public class ForgotPasswordResponse {
	private final String tokenMail;

	public ForgotPasswordResponse(String mail) {
		this.tokenMail = mail;
	}

	public String getJwt() {
		return tokenMail;
	}
}
