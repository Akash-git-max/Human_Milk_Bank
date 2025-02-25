package com.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="discard")
public class Discard {

	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="ddid")
	private int ddid;
	
	@Column(name="dddate")
	private String dddate;
	
	@Column(name="batch")
	private String batch;
	
	@Column(name="negative")
	private int negative;

	@Column(name="positive")
	private int positive;	
	
	@Column(name="discard")
	private String discard;
	
	@Column(name="approve")
	private String approve;
	
	@Column(name="doperator")
	private String doperator;

	
	//Default Constructor
	public Discard() {		
	}


	public int getDdid() {
		return ddid;
	}


	public void setDdid(int ddid) {
		this.ddid = ddid;
	}


	public String getDddate() {
		return dddate;
	}


	public void setDddate(String dddate) {
		this.dddate = dddate;
	}


	public String getBatch() {
		return batch;
	}


	public void setBatch(String batch) {
		this.batch = batch;
	}


	public int getNegative() {
		return negative;
	}


	public void setNegative(int negative) {
		this.negative = negative;
	}


	public int getPositive() {
		return positive;
	}


	public void setPositive(int positive) {
		this.positive = positive;
	}


	public String getDiscard() {
		return discard;
	}


	public void setDiscard(String discard) {
		this.discard = discard;
	}


	public String getApprove() {
		return approve;
	}


	public void setApprove(String approve) {
		this.approve = approve;
	}


	public String getDoperator() {
		return doperator;
	}


	public void setDoperator(String doperator) {
		this.doperator = doperator;
	}


	@Override
	public String toString() {
		return "Discard [ddid=" + ddid + ", dddate=" + dddate + ", batch=" + batch + ", negative=" + negative
				+ ", positive=" + positive + ", discard=" + discard + ", approve=" + approve + ", doperator="
				+ doperator + "]";
	}
	
	

}
