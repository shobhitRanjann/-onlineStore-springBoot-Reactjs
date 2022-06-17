package com.example.testclient.razorpay;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
 
@Component
@ConfigurationProperties(prefix = "razorpay")
public class RazorPayClientConfig {
    private String key = "rzp_test_Rdab4sKKJnl5EP";
    private String secret = "kZ2qg16zTQr5dou5mpdAIdWn";
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public RazorPayClientConfig(String key, String secret) {
		super();
		this.key = key;
		this.secret = secret;
	}
	@Override
	public String toString() {
		return "RazorPayClientConfig [key=" + key + ", secret=" + secret + "]";
	}
	public RazorPayClientConfig() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}