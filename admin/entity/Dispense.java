package com.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dispense")
public class Dispense {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name="dsid")
	private int dsid;
	
	@Column(name="infantname")
	private String infantname;
	
	@Column(name="qrcode")
	private String qrcode;
	
	@Column(name="totalmilk")
	private int totalmilk;

	@Column(name="tag")
	private String tag;	
	
	@Column(name="recipient")
	private String recipient;
	
	@Column(name="rname")
	private String rname;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="dsdate")
	private String dsdate;

	@Column(name="royalty")
	private String royalty;
	
	//Default Constructor
	public Dispense() {		
	}


	public int getDsid() {
		return dsid;
	}


	public void setDsid(int dsid) {
		this.dsid = dsid;
	}


	public String getInfantname() {
		return infantname;
	}


	public void setInfantname(String infantname) {
		this.infantname = infantname;
	}


	public String getQrcode() {
		return qrcode;
	}


	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}


	public int getTotalmilk() {
		return totalmilk;
	}


	public void setTotalmilk(int totalmilk) {
		this.totalmilk = totalmilk;
	}


	public String getTag() {
		return tag;
	}


	public void setTag(String tag) {
		this.tag = tag;
	}


	public String getRecipient() {
		return recipient;
	}


	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}


	public String getRname() {
		return rname;
	}


	public void setRname(String rname) {
		this.rname = rname;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getDsdate() {
		return dsdate;
	}


	public void setDsdate(String dsdate) {
		this.dsdate = dsdate;
	}


	public String getRoyalty() {
		return royalty;
	}


	public void setRoyalty(String royalty) {
		this.royalty = royalty;
	}


	@Override
	public String toString() {
		return "Dispense [dsid=" + dsid + ", infantname=" + infantname + ", qrcode=" + qrcode + ", totalmilk="
				+ totalmilk + ", tag=" + tag + ", recipient=" + recipient + ", rname=" + rname + ", remark=" + remark
				+ ", dsdate=" + dsdate + ", royalty=" + royalty + "]";
	}


	

}
