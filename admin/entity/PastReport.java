package com.admin.entity;

public class PastReport {
	
	String batch,unit,milk,remark;
	String date;

	public PastReport(String batch, String unit, String milk, String remark) {
		super();
		this.batch = batch;
		this.unit = unit;
		this.milk = milk;
		this.remark = remark;
	}
	
	public PastReport(String date,String batch, String unit, String milk, String remark) {
		super();
		this.date=date;
		this.batch = batch;
		this.unit = unit;
		this.milk = milk;
		this.remark = remark;
	}

	public String getBatch() {
		return batch;
	}

	public String getUnit() {
		return unit;
	}

	public String getMilk() {
		return milk;
	}

	public String getRemark() {
		return remark;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setMilk(String milk) {
		this.milk = milk;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
