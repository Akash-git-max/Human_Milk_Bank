package com.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="past")
public class Past {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="pid")
	private int pid;
	
	@Column(name="pdate")
	private String pdate;
	
	@Column(name="batch")
	private String batch;
	
	@Column(name="units")
	private String units;
	
	@Column(name="totalmilk")
	private int totalmilk;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="poperator")
	private String poperator;

	
	//Default Constructor
	public Past() {		
	}


	public int getPid() {
		return pid;
	}


	public void setPid(int pid) {
		this.pid = pid;
	}


	public String getPdate() {
		return pdate;
	}


	public void setPdate(String pdate) {
		this.pdate = pdate;
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


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getPoperator() {
		return poperator;
	}


	public void setPoperator(String poperator) {
		this.poperator = poperator;
	}


	@Override
	public String toString() {
		return "Past [pid=" + pid + ", pdate=" + pdate + ", batch=" + batch + ", units=" + units + ", totalmilk="
				+ totalmilk + ", remark=" + remark + ", poperator=" + poperator + "]";
	}

}
