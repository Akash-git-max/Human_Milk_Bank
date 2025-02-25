package com.admin.entity;

public class PastData {
	
	
	private String batch;	
	
	private String milk;	
	
	private String sample;
	
	public PastData() {
		
	}
	

	public PastData(String batch, String milk, String sample) {
		super();
		this.batch = batch;
		this.milk = milk;
		this.sample = sample;
	}


	public String getBatch() {
		return batch;
	}

	public String getMilk() {
		return milk;
	}

	public String getSample() {
		return sample;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public void setMilk(String milk) {
		this.milk = milk;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	@Override
	public String toString() {
		return "PastData [batch=" + batch + ", milk=" + milk + ", sample=" + sample + "]";
	}

}
