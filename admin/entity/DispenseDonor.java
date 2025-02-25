package com.admin.entity;

public class DispenseDonor {

	String date;
	String rname;
	String qr;
	int milk;
	
	public DispenseDonor(String date, String rname, String qr, int milk) {
		super();
		this.date = date;
		this.rname = rname;
		this.qr = qr;
		this.milk = milk;
	}
	
	public String getDate() {
		return date;
	}
	public String getRname() {
		return rname;
	}
	public String getQr() {
		return qr;
	}
	public int getMilk() {
		return milk;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public void setQr(String qr) {
		this.qr = qr;
	}
	public void setMilk(int milk) {
		this.milk = milk;
	}
	
	@Override
	public String toString() {
		return "DispenseDonor [date=" + date + ", rname=" + rname + ", qr=" + qr + ", milk=" + milk + "]";
	}
	
	
}
