package com.example.testclient.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;


@Entity
public class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	private String address;
	@Column(nullable = true, length = 64)
    private String photos;
	private String description;
	private String producttype;
	private float price;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
	public String getProducttype() {
		return producttype;
	}
	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}
	
	public Department(long id, String name, String address, String photos, String description, String producttype,
			float price) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.photos = photos;
		this.description = description;
		this.producttype = producttype;
		this.price = price;
	}
	public Department() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", address=" + address + ", photos=" + photos
				+ ", description=" + description + ", producttype=" + producttype + ", price=" + price + "]";
	}
	
	
	@Transient
    public String getPhotosImagePath() {
		String photosdata="";
        if (photos == null || Objects.isNull(id) ) 
        	return null;
        String photocs[] = photos.split(",");	
        
        int len = photocs.length;
		 int i=0;
		 while(len!=0) {
			 if(i==0) {
				 photosdata="/dept_photos/" + id + "/" + photocs[i];
			 }
			 else {
				 photosdata=photosdata+","+"/dept_photos/" + id + "/" + photocs[i];
			 }
			 i++;
			 len--;
		 }
        
        return photosdata;      
    }
	
	
}
