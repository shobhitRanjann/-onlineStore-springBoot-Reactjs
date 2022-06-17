package com.example.testclient.razorpay;

public class PaymentResponse {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private String productId;
	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}
	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}
	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}
	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}
	public String getRazorpaySignature() {
		return razorpaySignature;
	}
	public void setRazorpaySignature(String razorpaySignature) {
		this.razorpaySignature = razorpaySignature;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public PaymentResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PaymentResponse(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature, String productId) {
		super();
		this.razorpayOrderId = razorpayOrderId;
		this.razorpayPaymentId = razorpayPaymentId;
		this.razorpaySignature = razorpaySignature;
		this.productId = productId;
	}
	@Override
	public String toString() {
		return "PaymentResponse [razorpayOrderId=" + razorpayOrderId + ", razorpayPaymentId=" + razorpayPaymentId
				+ ", razorpaySignature=" + razorpaySignature + ", productId=" + productId + "]";
	}
    
    
}
