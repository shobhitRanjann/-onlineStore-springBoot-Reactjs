package com.example.testclient.razorpay;

public class AllOrders {
	public String email;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public AllOrders() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AllOrders(String email) {
		super();
		this.email = email;
	}
	@Override
	public String toString() {
		return "AllOrders [email=" + email + "]";
	}
	
}
