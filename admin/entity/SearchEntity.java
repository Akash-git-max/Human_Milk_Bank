package com.admin.entity;

public class SearchEntity {	
	
	//From Donor Table		
	private String edate="";	
	private String hospital="";	
	private String mobile="";	
	private String dob="";	
	private String mother="";	
	private String fhname="";	
	private String surname="";	
	private String batch="";	
	private int milk=0;	
	private String hiv="";	
	private String hbsag="";	
	private String vdrl="";	
	private String rtype="";	
	private String qrcode="";
	
	//From Past Table
	private String poperator="";	
	private String pdate="";
	
	//From Culture Table
	private String coperator="";	
	private String cdate="";
	
	//from discard table	
	private String doperator="";	
	private String discard="";	
	private String approve="";	
	private String dddate="";
	
	//from dispense Table	
	private String infantname="";	
	private int totalmilk;	
	private String recipient="";	
	private String tag="";	
	private String rname="";	
	private String remark="";	
	private String dsdate="";
	
	private String discardstatus="1";
	
	//Default Constructor
	public SearchEntity() {
		
	}
	
	public void setDonor(Donor d)
	{
		edate=d.getEdate();
		hospital=d.getHospital();
		mobile=d.getMobile();
		dob=d.getDob();
		mother=d.getMother();
		fhname=d.getFhname();
		surname=d.getSurname();
		batch=d.getBatch();
		milk=d.getMilk();
		hiv=d.getHiv();
		if(hiv.equals("0"))
		{
			hiv="Negative";
		}else {
			hiv="Positive";
		}
		
		hbsag=d.getHbsag();
		if(hbsag.equals("0"))
		{
			hbsag="Negative";
		}else {
			hbsag="Positive";
		}
		
		vdrl=d.getVdrl();
		if(vdrl.equals("0"))
		{
			vdrl="Negative";
		}else {
			vdrl="Positive";
		}
		
		rtype=d.getRtype();
		if(rtype.equals("0"))
		{
			rtype="Own Mother";
		}else {
			rtype="Donor Mother";
		}
		qrcode=d.getQrcode();
	}
	
	public void setPast(Past p)
	{
		poperator=p.getPoperator();
		pdate=p.getPdate();
	}
	
	public void setCulture(Culture c)
	{
		coperator=c.getCoperator();
		cdate=c.getCdate();
	}
	
	public void setDiscard(Discard d)
	{
		doperator=d.getDoperator();
		discard=d.getDiscard();
		
		String[] arrSplit = discard.split(", ");
	    for (int i=0; i < arrSplit.length; i++)
	    {
	    	System.out.println("Discard "+arrSplit[i]);
	    	if(arrSplit[i].equals(qrcode))
	    	{
	    		discardstatus="Discarded Sample";
	    	}	      
	    }
		approve=d.getApprove();
		String[] arrSplit1 = approve.split(", ");
	    for (int i=0; i < arrSplit1.length; i++)
	    {
	    	System.out.println("Discard "+arrSplit1[i]);
	    	if(arrSplit1[i].equals(qrcode))
	    	{
	    		discardstatus="Approved Sample";
	    	}	      
	    }
		dddate=d.getDddate();
	}
	
	public void setDispense(Dispense d)
	{
		infantname=d.getInfantname();
		totalmilk=d.getTotalmilk();
		recipient=d.getRecipient();
		if(recipient.equals("0"))
		{
			recipient="Own";
		}else if(recipient.equals("1"))
		{
			recipient="NICU";
		}else if(recipient.equals("2"))
		{
			recipient="SNCV";
		}else if(recipient.equals("3"))
		{
			recipient=" Other Hospital";
		}else if(recipient.equals("4"))
		{
			recipient=" Indivial";
		}
		tag=d.getTag();
		rname=d.getRname();
		remark=d.getRemark();
		dsdate=d.getDsdate();
	}
	
	public String getEdate() {
		return edate;
	}
	public String getHospital() {
		return hospital;
	}
	public String getMobile() {
		return mobile;
	}
	public String getDob() {
		return dob;
	}
	public String getMother() {
		return mother;
	}
	public String getFhname() {
		return fhname;
	}
	public String getSurname() {
		return surname;
	}
	public String getBatch() {
		return batch;
	}
	public int getMilk() {
		return milk;
	}
	public String getHiv() {
		return hiv;
	}
	public String getHbsag() {
		return hbsag;
	}
	public String getVdrl() {
		return vdrl;
	}
	public String getRtype() {
		return rtype;
	}
	public String getQrcode() {
		return qrcode;
	}
	public String getPoperator() {
		return poperator;
	}
	public String getPdate() {
		return pdate;
	}
	public String getCoperator() {
		return coperator;
	}
	public String getCdate() {
		return cdate;
	}
	public String getDoperator() {
		return doperator;
	}
	public String getDiscard() {
		return discard;
	}
	public String getApprove() {
		return approve;
	}
	public String getDddate() {
		return dddate;
	}
	public String getInfantname() {
		return infantname;
	}
	public int getTotalmilk() {
		return totalmilk;
	}
	public String getRecipient() {
		return recipient;
	}
	public String getTag() {
		return tag;
	}
	public String getRname() {
		return rname;
	}
	public String getRemark() {
		return remark;
	}
	public String getDsdate() {
		return dsdate;
	}
	
	public String getDiscardstatus() {
		return discardstatus;
	}

	@Override
	public String toString() {
		return "SearchEntity [edate=" + edate + ", hospital=" + hospital + ", mobile=" + mobile + ", dob=" + dob
				+ ", mother=" + mother + ", fhname=" + fhname + ", surname=" + surname + ", batch=" + batch + ", milk="
				+ milk + ", hiv=" + hiv + ", hbsag=" + hbsag + ", vdrl=" + vdrl + ", rtype=" + rtype + ", qrcode="
				+ qrcode + ", poperator=" + poperator + ", pdate=" + pdate + ", coperator=" + coperator + ", cdate="
				+ cdate + ", doperator=" + doperator + ", discard=" + discard + ", approve=" + approve + ", dddate="
				+ dddate + ", infantname=" + infantname + ", totalmilk=" + totalmilk + ", recipient=" + recipient
				+ ", tag=" + tag + ", rname=" + rname + ", remark=" + remark + ", dsdate=" + dsdate + ", discardstatus="
				+ discardstatus + "]";
	}
	
}
