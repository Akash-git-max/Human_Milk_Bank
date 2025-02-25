package com.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="royalty")
public class Royalty {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="rid")
	private int rid;
	
	@Column(name="name")
	private String name="";
	
	@Column(name="middle")
	private String middle="";
	
	@Column(name="surname")
	private String surname="";
	
	@Column(name="mobile")
	private String mobile="";	
	
	@Column(name="dob")
	private String dob="";
	
	@Column(name="qrcode")
	private String qrcode;	
	
	@Column(name="donate")
	private int donate;

	public int getRid() {
		return rid;
	}

	public String getName() {
		return name;
	}

	public String getMiddle() {
		return middle;
	}

	public String getSurname() {
		return surname;
	}

	public String getMobile() {
		return mobile;
	}

	public String getDob() {
		return dob;
	}

	public String getQrcode() {
		return qrcode;
	}

	public int getDonate() {
		return donate;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMiddle(String middle) {
		this.middle = middle;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setMobile(String contact) {
		this.mobile = contact;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public void setDonate(int donate) {
		this.donate = donate;
	}

	@Override
	public String toString() {
		return "Royalty [rid=" + rid + ", name=" + name + ", middle=" + middle + ", surname=" + surname + ", mobile="
				+ mobile + ", dob=" + dob + ", qrcode=" + qrcode + ", donate=" + donate + "]";
	}

}
