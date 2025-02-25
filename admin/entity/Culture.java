package com.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="culture")
public class Culture {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="cid")
	private int cid;
	
	@Column(name="cdate")
	private String cdate;
	
	@Column(name="batch")
	private String batch;
	
	@Column(name="units")
	private String units;
	
	@Column(name="totalmilk")
	private int totalmilk;	
	
	@Column(name="coperator")
	private String coperator;

	
	//Default Constructor
	public Culture() {		
	}


	public int getCid() {
		return cid;
	}


	public void setCid(int cid) {
		this.cid = cid;
	}


	public String getCdate() {
		return cdate;
	}


	public void setCdate(String cdate) {
		this.cdate = cdate;
	}


	public String getBatch() {
		return batch;
	}


	public void setBatch(String batch) {
		this.batch = batch;
	}


	public String getUnits() {
		return units;
	}


	public void setUnits(String units) {
		this.units = units;
	}


	public int getTotalmilk() {
		return totalmilk;
	}


	public void setTotalmilk(int totalmilk) {
		this.totalmilk = totalmilk;
	}


	public String getCoperator() {
		return coperator;
	}


	public void setCoperator(String coperator) {
		this.coperator = coperator;
	}


	@Override
	public String toString() {
		return "Culture [cid=" + cid + ", cdate=" + cdate + ", batch=" + batch + ", units=" + units + ", totalmilk="
				+ totalmilk + ", coperator=" + coperator + "]";
	}


}
