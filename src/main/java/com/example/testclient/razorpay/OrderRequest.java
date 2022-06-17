package com.example.testclient.razorpay;

public class OrderRequest {
	private String customerName;
    private String email;
    private String phoneNumber;
    private String amount;
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public OrderRequest(String customerName, String email, String phoneNumber, String amount) {
		super();
		this.customerName = customerName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.amount = amount;
	}
	public OrderRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "OrderRequest [customerName=" + customerName + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", amount=" + amount + "]";
	}
    
    
}
