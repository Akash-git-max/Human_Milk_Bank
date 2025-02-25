package com.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="donor")
public class Donor {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="did")
	private int did;
	
	@Column(name="edate")
	private String edate;
	
	@Column(name="hospital")
	private String hospital;
	
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="dob")
	private String dob;
	
	@Column(name="mother")
	private String mother;
	
	@Column(name="fhname")
	private String fhname;
	
	@Column(name="surname")
	private String surname;
	
	@Column(name="batch")
	private String batch;
	
	@Column(name="milk")
	private int milk;
	
	@Column(name="hiv")
	private String hiv;
	
	@Column(name="vdrl")
	private String 	vdrl;
	
	@Column(name="hbsag")
	private String hbsag;
	
	@Column(name="qrcode")
	private String 	qrcode;
	
	@Column(name="rtype")
	private String rtype;

	//Default Constructor
	public Donor() {		
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getFhname() {
		return fhname;
	}

	public void setFhname(String fhname) {
		this.fhname = fhname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public int getMilk() {
		return milk;
	}

	public void setMilk(int milk) {
		this.milk = milk;
	}

	public String getHiv() {
		return hiv;
	}

	public void setHiv(String hiv) {
		this.hiv = hiv;
	}

	public String getVdrl() {
		return vdrl;
	}

	public void setVdrl(String vdrl) {
		this.vdrl = vdrl;
	}

	public String getHbsag() {
		return hbsag;
	}

	public void setHbsag(String hbsag) {
		this.hbsag = hbsag;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	@Override
	public String toString() {
		return "Donor [did=" + did + ", edate=" + edate + ", hospital=" + hospital + ", mobile=" + mobile + ", dob="
				+ dob + ", mother=" + mother + ", fhname=" + fhname + ", surname=" + surname + ", batch=" + batch
				+ ", milk=" + milk + ", hiv=" + hiv + ", vdrl=" + vdrl + ", hbsag=" + hbsag + ", qrcode=" + qrcode
				+ ", rtype=" + rtype + "]";
	}
	
}
