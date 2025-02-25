package com.admin.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.Array;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.admin.entity.Admin;
import com.admin.entity.Culture;
import com.admin.entity.Discard;
import com.admin.entity.Dispense;
import com.admin.entity.DispenseDonor;
import com.admin.entity.Donor;
import com.admin.entity.Past;
import com.admin.entity.PastData;
import com.admin.entity.PastReport;
import com.admin.entity.QRCodeMilk;
import com.admin.entity.Royalty;
import com.admin.entity.SearchEntity;
import com.admin.entity.Security;
import com.admin.entity.Stmt;

@Repository
public class AdminDAO implements AdminDAOInterface {	
	
	@Autowired
	private SessionFactory sessionfactory;
	
	@SuppressWarnings("deprecation")
	@Override	
	public List<Admin> checkAdmin(String username, String password) {	
		
		Session session=sessionfactory.getCurrentSession();
		
        boolean isValidUser = false;
        
       // Query<Admin> list=session.createQuery("from admin", Admin.class);
		
        SQLQuery<Admin> query = session.createSQLQuery("select * from admin where username='"+username+"' and password='"+password+"'");     
        
        
		@SuppressWarnings("unchecked")
		List<Admin> admin=new ArrayList<Admin>();
		admin=(List<Admin>)query.getResultList();
        
		return admin;
	}

	@Override
	public boolean changePass(String id, String username, String password) {
		
		Session session=sessionfactory.getCurrentSession();
		
		SQLQuery<Admin> query = session.createSQLQuery("UPDATE admin SET username='"+username+"',password='"+password+"' WHERE adminid='"+id+"'");
          
        int result = query.executeUpdate();
        System.out.println("Result: "+result);
        if(result==1)
        {
        	return true;
        }
        else
        {
        	return false;
        }		
	}

	@Override
	public SearchEntity searchResult(String qrcode) {
		
		SearchEntity searchEntity=new SearchEntity();
		Session session=sessionfactory.getCurrentSession();	
		
		//Get the Donor 
		Query<Donor> query=session.createQuery("from Donor where qrcode=:qr",Donor.class);
		query.setParameter("qr", qrcode);		
		Donor donor=query.uniqueResult();
		
		//Check whether donor is empty or not
		if(donor!=null)
		{
			searchEntity.setDonor(donor);
			
			//get the Dispense
			Query<Dispense> query1=session.createQuery("from Dispense where qrcode=:qr",Dispense.class);
			query1.setParameter("qr", qrcode);	
			Dispense dispense=query1.uniqueResult();	
			
			if(dispense!=null)
			{
				searchEntity.setDispense(dispense);
			}
			
			String batch=donor.getBatch();
			
			//get Pasteurization
			Query<Past> query2=session.createQuery("from Past where batch=:qr",Past.class);
			query2.setParameter("qr", batch);
			Past past=query2.uniqueResult();
			
			if(past!=null)
			{
				searchEntity.setPast(past);
			}
			
			//get from Culture
			Query<Culture> query3=session.createQuery("from Culture where batch=:qr",Culture.class);
			query3.setParameter("qr", batch);
			Culture culture=query3.uniqueResult();
			
			if(culture!=null)
			{
				searchEntity.setCulture(culture);
			}
			
			//get from Discard
			Query<Discard> query4=session.createQuery("from Discard where batch=:qr",Discard.class);
			query4.setParameter("qr", batch);
			Discard discard=query4.uniqueResult();
			if(discard!=null)
			{
				searchEntity.setDiscard(discard);
			}
		}
		//If any of the above object is null then manually check it and put null value in the object's parameter
		//or you can pass the object to SearchEntity as a parameter and then check inside the Searchentity whether object is null or not
		 
		System.out.println("Search Entity is: "+searchEntity.toString());
		return searchEntity;
	}

	@Override
	public void saveDonor(Donor donor) {		
		Session currentSession=sessionfactory.getCurrentSession();
		currentSession.saveOrUpdate(donor);		
	}

	@Override
	public String lastBatch() {
		Session session=sessionfactory.getCurrentSession();
				
		
		Query<String> query=session.createQuery("SELECT MAX(batch) from Donor");
		String batch;
		try {
				batch=query.uniqueResult();	
				if(batch.equals("")||batch.equals(null)||batch.equals("null"))
				{
					batch="100";
				}
			}
		catch(NullPointerException e)
		{
			batch="100";
		}
		System.out.println("batch: "+batch);
		return batch;
		
	}

	@Override
	public List<PastData> pastData() {
		
		Session session=sessionfactory.getCurrentSession();
		
		List<PastData> data=new ArrayList<PastData>();
		//Get Batch
		Query<String> query=session.createQuery("SELECT batch from Donor group by batch ORDER by batch DESC");
		List<String> batch=(List<String>)query.getResultList();
		
		//Get Batch
		Query<Long> query1=session.createQuery("SELECT sum(milk) from Donor group by batch ORDER by batch DESC");
		List<Long> milk=(List<Long>)query1.list();
		
		//Get Batch
		Query<Long> query2=session.createQuery("SELECT COUNT(batch) from Donor group by batch ORDER by batch DESC");
		List<Long> sample=(List<Long>)query2.list();
		
		for(int i=0;i<batch.size();i++)
		{
			PastData p=new PastData(batch.get(i),""+milk.get(i),""+sample.get(i));
			data.add(p);
		}
		
		return data;
	}

	@Override
	public boolean checkBatch(String batch) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		Query<String> query=session.createQuery("SELECT pdate from Past where batch='"+batch+"'");
		List<String> batchc=(List<String>)query.getResultList();
		for(int i=0;i<batchc.size();i++)
		{
			System.out.println(""+batchc.get(i));
		}
		
		if(batchc.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void savePast(Past past) {		
		Session currentSession=sessionfactory.getCurrentSession();
		currentSession.saveOrUpdate(past);			
	}

	@Override
	public boolean checkPastBatch(String batch) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		Query<String> query=session.createQuery("SELECT pdate from Past where batch='"+batch+"'");		
		
		List<String> batchc=(List<String>)query.getResultList();
		
		Query<String> query1=session.createQuery("SELECT cdate from Culture where batch='"+batch+"'");
		
		List<String> batchc1=(List<String>)query1.getResultList();	
		
		
		if(batchc.isEmpty())
		{
			return false;
		}
		else
		{
			if(batchc1.isEmpty())
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}		
	}

	@Override
	public void saveCulture(Culture cul) {
		Session currentSession=sessionfactory.getCurrentSession();
		currentSession.saveOrUpdate(cul);			
	}

	@Override
	public List<String> getRDonorToday() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date());        
        
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query=session.createQuery("SELECT COUNT(*) as count FROM Donor WHERE rtype='1' AND edate='"+today+"'");
		List<Long> count=(List<Long>)query.getResultList();		
		
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery("SELECT SUM(milk) as milk FROM Donor WHERE rtype='1' AND edate='"+today+"'");
		List<Long> milk=(List<Long>)query1.getResultList();	
		
		String a=""+count.get(0);		
		String b=""+milk.get(0);
		
		
		List<String> result=new ArrayList<String>();
		
		result.add(a);
		result.add(b);
		
		return result;
	}

	@SuppressWarnings("unused")
	@Override
	public List<Stmt> getRDonorMini() {	
		
		Session session=sessionfactory.getCurrentSession();		
		//Today
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date()); 
        String last;
        //5 days back date
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -4);
        Date sevenDaysAgo = cal.getTime();
        last=f.format(sevenDaysAgo);
                
        String sql="select edate from Donor where rtype='1' AND  edate between '"+last+"' AND '"+today+"' group by edate order by edate ASC";
        String sql1="select COUNT(*) from Donor where rtype='1' AND  edate between '"+last+"' AND '"+today+"' group by edate order by edate ASC";
        String sql2="select SUM(milk) from Donor where rtype='1' AND  edate between '"+last+"' AND '"+today+"' group by edate order by edate ASC";
        //Getting Date
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> datelist=(List<String>)query.getResultList();	
		
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql1);
		List<Long> count=(List<Long>)query1.getResultList();		
				
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query2=session.createQuery(sql2);
		List<Long> milk=(List<Long>)query2.getResultList();	
				
		
		List<Stmt> mini=new ArrayList<Stmt>();
		
		for(int i=0;i<datelist.size();i++)
		{
			Stmt p=new Stmt(datelist.get(i),""+count.get(i),""+milk.get(i));
			mini.add(p);
		}
		return mini;
	}

	@Override
	public List<Stmt> getRDonorDetails(String sdate, String edate) {
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select edate from Donor where rtype='1' AND  edate between '"+edate+"' AND '"+sdate+"' group by edate order by edate ASC";
        String sql1="select COUNT(*) from Donor where rtype='1' AND  edate between '"+edate+"' AND '"+sdate+"' group by edate order by edate ASC";
        String sql2="select SUM(milk) from Donor where rtype='1' AND  edate between '"+edate+"' AND '"+sdate+"' group by edate order by edate ASC";
        //Getting Date
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> datelist=(List<String>)query.getResultList();	
		
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql1);
		List<Long> count=(List<Long>)query1.getResultList();		
				
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query2=session.createQuery(sql2);
		List<Long> milk=(List<Long>)query2.getResultList();	
				
		
		List<Stmt> mini=new ArrayList<Stmt>();
		
		for(int i=0;i<datelist.size();i++)
		{
			Stmt p=new Stmt(datelist.get(i),""+count.get(i),""+milk.get(i));
			mini.add(p);
		}
		return mini;		
	}

	@Override
	public List<PastReport> getPastToday() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date());        
        
		//Get Batch
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery("SELECT batch FROM Past WHERE pdate='"+today+"'");
		List<String> batch=(List<String>)query.getResultList();		
		
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery("SELECT totalmilk FROM Past WHERE pdate='"+today+"'");
		List<Long> milk=(List<Long>)query1.getResultList();	
		
		//Get units
		@SuppressWarnings("unchecked")
		Query<String> query2=session.createQuery("SELECT units FROM Past WHERE pdate='"+today+"'");
		List<String> units=(List<String>)query2.getResultList();
		
		//Get remarks
		@SuppressWarnings("unchecked")
		Query<String> query3=session.createQuery("SELECT remark FROM Past WHERE pdate='"+today+"'");
		List<String> remark=(List<String>)query3.getResultList();
		
		List<PastReport> result=new ArrayList<PastReport>();
		
		for(int i=0;i<batch.size();i++)
		{
			PastReport d1=new PastReport(batch.get(i),units.get(i),""+milk.get(i),remark.get(i));
			result.add(d1);
		}
		
		return result;
	}

	@Override
	public List<PastReport> getPastMini() {
		
		Session session=sessionfactory.getCurrentSession();		
		//Today
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date()); 
        String last;
        //5 days back date
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -4);
        Date sevenDaysAgo = cal.getTime();
        last=f.format(sevenDaysAgo);
        
        
      //Get Batch
      		@SuppressWarnings("unchecked")
      		Query<String> query=session.createQuery("SELECT batch FROM Past WHERE pdate between '"+last+"' AND '"+today+"' group by pdate order by pdate ASC");
      		List<String> batch=(List<String>)query.getResultList();		
      		
      		//Get Milk
      		@SuppressWarnings("unchecked")
      		Query<Long> query1=session.createQuery("SELECT totalmilk FROM Past WHERE pdate between '"+last+"' AND '"+today+"' group by pdate order by pdate ASC");
      		List<Long> milk=(List<Long>)query1.getResultList();	
      		
      		//Get units
      		@SuppressWarnings("unchecked")
      		Query<String> query2=session.createQuery("SELECT units FROM Past WHERE pdate between '"+last+"' AND '"+today+"' group by pdate order by pdate ASC");
      		List<String> units=(List<String>)query2.getResultList();
      		
      		//Get remarks
      		@SuppressWarnings("unchecked")
      		Query<String> query3=session.createQuery("SELECT remark FROM Past WHERE pdate between '"+last+"' AND '"+today+"' order by pdate ASC");
      		List<String> remark=(List<String>)query3.getResultList();
      		
      		//Get remarks
      		@SuppressWarnings("unchecked")
      		Query<String> query4=session.createQuery("SELECT pdate FROM Past WHERE pdate between '"+last+"' AND '"+today+"' order by pdate ASC");
      		List<String> date=(List<String>)query4.getResultList();
      		
      		List<PastReport> result=new ArrayList<PastReport>();
      		
      		for(int i=0;i<batch.size();i++)
      		{
      			PastReport p1=new PastReport(date.get(i), batch.get(i), units.get(i), ""+milk.get(i), remark.get(i));
      			result.add(p1);
      		}
      		System.out.print("Size: "+result.size());
        
		return result;
	}

	@Override
	public List<PastReport> getPastDetails(String today, String last) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="SELECT batch FROM Past WHERE pdate between '"+last+"' AND '"+today+"' order by pdate ASC";
        String sql1="SELECT totalmilk FROM Past WHERE pdate between '"+last+"' AND '"+today+"' order by pdate ASC";
        String sql2="SELECT units FROM Past WHERE pdate between '"+last+"' AND '"+today+"' order by pdate ASC";
        String sql3="SELECT remark FROM Past WHERE pdate between '"+last+"' AND '"+today+"' order by pdate ASC";
        String sql4="SELECT pdate FROM Past WHERE pdate between '"+last+"' AND '"+today+"' order by pdate ASC";
        //Getting Date
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> batch=(List<String>)query.getResultList();	
		
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql1);
		List<Long> milk=(List<Long>)query1.getResultList();		
				
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<String> query2=session.createQuery(sql2);
		List<String> units=(List<String>)query2.getResultList();	
		
		//Get Remark
		@SuppressWarnings("unchecked")
		Query<String> query3=session.createQuery(sql3);
		List<String> remark=(List<String>)query3.getResultList();
		
		//Get PDate
		@SuppressWarnings("unchecked")
		Query<String> query4=session.createQuery(sql4);
		List<String> date=(List<String>)query4.getResultList();
		
		List<PastReport> mini=new ArrayList<PastReport>();
		
		for(int i=0;i<date.size();i++)
		{
			PastReport p=new PastReport(date.get(i), batch.get(i), units.get(i), ""+milk.get(i), remark.get(i));
			mini.add(p);
		}
		return mini;
	}

	@Override
	public List<Culture> getCultureToday() {
		Session session=sessionfactory.getCurrentSession();	
		
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date()); 
     	
      	@SuppressWarnings("unchecked")
      	Query<Culture> query=session.createQuery("FROM Culture WHERE cdate='"+today+"'");
      	List<Culture> batch=(List<Culture>)query.getResultList();	
        
		return batch;
	}

	@Override
	public List<Culture> getCultureMini() {
		Session session=sessionfactory.getCurrentSession();	
		
		//Today
		Format f = new SimpleDateFormat("dd/MM/yyyy");
		String today = f.format(new Date()); 
		String last;
		//5 days back date
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, -4);
		Date sevenDaysAgo = cal.getTime();
		last=f.format(sevenDaysAgo);
		
		 //Get Batch
  		@SuppressWarnings("unchecked")
  		Query<Culture> query=session.createQuery("FROM Culture WHERE cdate between '"+last+"' AND '"+today+"' order by cdate ASC");
  		List<Culture> batch=(List<Culture>)query.getResultList();		
  		
		return batch;
	}

	@Override
	public List<Culture> getCultureDetails(String today, String last) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="FROM Culture WHERE cdate between '"+last+"' AND '"+today+"' order by cdate ASC";
		
		@SuppressWarnings("unchecked")
		Query<Culture> query=session.createQuery(sql);
		List<Culture> batch=(List<Culture>)query.getResultList();	
		
		return batch;
	}

	@Override
	public boolean checkCultureBatch(String batch) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		Query<String> query=session.createQuery("SELECT cdate from Culture where batch='"+batch+"'");		
		
		List<String> batchc=(List<String>)query.getResultList();
		
		Query<String> query1=session.createQuery("SELECT dddate from Discard where batch='"+batch+"'");
		
		List<String> batchc1=(List<String>)query1.getResultList();	
		
		
		if(batchc.isEmpty())
		{
			return false;
		}
		else
		{
			if(batchc1.isEmpty())
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}	
	}

	@Override
	public void saveDiscard(Discard dis) {
		
		Session currentSession=sessionfactory.getCurrentSession();
		currentSession.saveOrUpdate(dis);	
		
	}

	@Override
	public List<Discard> getDiscardToday() {
		
		Session session=sessionfactory.getCurrentSession();	
		
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date()); 
     	
      	@SuppressWarnings("unchecked")
      	Query<Discard> query=session.createQuery("FROM Discard WHERE dddate='"+today+"'");
      	List<Discard> batch=(List<Discard>)query.getResultList();	
        
		return batch;		
	}

	@Override
	public List<Discard> getDiscardMini() {
		Session session=sessionfactory.getCurrentSession();	
		
		//Today
		Format f = new SimpleDateFormat("dd/MM/yyyy");
		String today = f.format(new Date()); 
		String last;
		//5 days back date
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, -4);
		Date sevenDaysAgo = cal.getTime();
		last=f.format(sevenDaysAgo);
		
		 //Get Batch
  		@SuppressWarnings("unchecked")
  		Query<Discard> query=session.createQuery("FROM Discard WHERE dddate between '"+last+"' AND '"+today+"' order by dddate ASC");
  		List<Discard> batch=(List<Discard>)query.getResultList();		
  		
		return batch;
	}

	@Override
	public List<Discard> getDiscardDetails(String today, String last) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="FROM Discard WHERE dddate between '"+last+"' AND '"+today+"' order by dddate ASC";
		
		@SuppressWarnings("unchecked")
		Query<Discard> query=session.createQuery(sql);
		List<Discard> batch=(List<Discard>)query.getResultList();	
		
		return batch;
	}

	@Override
	public List<String> getODonorToday() {
Session session=sessionfactory.getCurrentSession();		
		
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date());        
        
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query=session.createQuery("SELECT COUNT(*) as count FROM Donor WHERE rtype='0' AND edate='"+today+"'");
		List<Long> count=(List<Long>)query.getResultList();		
		
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery("SELECT SUM(milk) as milk FROM Donor WHERE rtype='0' AND edate='"+today+"'");
		List<Long> milk=(List<Long>)query1.getResultList();	
		
		String a=""+count.get(0);		
		String b=""+milk.get(0);
		
		
		List<String> result=new ArrayList<String>();
		
		result.add(a);
		result.add(b);
		
		return result;
	}

	@Override
	public List<Stmt> getODonorMini() {
		Session session=sessionfactory.getCurrentSession();		
		//Today
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date()); 
        String last;
        //5 days back date
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -4);
        Date sevenDaysAgo = cal.getTime();
        last=f.format(sevenDaysAgo);
                
        String sql="select edate from Donor where rtype='0' AND  edate between '"+last+"' AND '"+today+"' group by edate order by edate ASC";
        String sql1="select COUNT(*) from Donor where rtype='0' AND  edate between '"+last+"' AND '"+today+"' group by edate order by edate ASC";
        String sql2="select SUM(milk) from Donor where rtype='0' AND  edate between '"+last+"' AND '"+today+"' group by edate order by edate ASC";
        //Getting Date
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> datelist=(List<String>)query.getResultList();	
		
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql1);
		List<Long> count=(List<Long>)query1.getResultList();		
				
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query2=session.createQuery(sql2);
		List<Long> milk=(List<Long>)query2.getResultList();	
				
		
		List<Stmt> mini=new ArrayList<Stmt>();
		
		for(int i=0;i<datelist.size();i++)
		{
			Stmt p=new Stmt(datelist.get(i),""+count.get(i),""+milk.get(i));
			mini.add(p);
		}
		return mini;
	}

	@Override
	public List<Stmt> getODonorDetails(String sdate, String edate) {
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select edate from Donor where rtype='0' AND  edate between '"+edate+"' AND '"+sdate+"' group by edate order by edate ASC";
        String sql1="select COUNT(*) from Donor where rtype='0' AND  edate between '"+edate+"' AND '"+sdate+"' group by edate order by edate ASC";
        String sql2="select SUM(milk) from Donor where rtype='0' AND  edate between '"+edate+"' AND '"+sdate+"' group by edate order by edate ASC";
        //Getting Date
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> datelist=(List<String>)query.getResultList();	
		
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql1);
		List<Long> count=(List<Long>)query1.getResultList();		
				
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query2=session.createQuery(sql2);
		List<Long> milk=(List<Long>)query2.getResultList();	
				
		
		List<Stmt> mini=new ArrayList<Stmt>();
		
		for(int i=0;i<datelist.size();i++)
		{
			Stmt p=new Stmt(datelist.get(i),""+count.get(i),""+milk.get(i));
			mini.add(p);
		}
		return mini;
	}

	@Override
	public boolean checkDonorQR(String qr) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select edate from Donor where qrcode='"+qr+"'";
		
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> list=(List<String>)query.getResultList();
		if(list.size()==1)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public boolean checkDispenseQR(String qr) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select qrcode from Dispense where qrcode='"+qr+"'";
		
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> list=(List<String>)query.getResultList();
		if(list.size()==1)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public void saveDispence(Dispense dis) {
		
		Session currentSession=sessionfactory.getCurrentSession();
		currentSession.saveOrUpdate(dis);		
	}

	@Override
	public List<QRCodeMilk> getQRCodeMilk() {
		
		List<QRCodeMilk> list=new ArrayList<QRCodeMilk>();
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select qrcode from Donor order by edate ASC";
        String sql1="select milk from Donor order by edate ASC";
        
        	//Getting QRCode
      		@SuppressWarnings("unchecked")
      		Query<String> query=session.createQuery(sql);
      		List<String> datelist=(List<String>)query.getResultList();	
      		
      		//Get Milk
      		@SuppressWarnings("unchecked")
      		Query<Integer> query1=session.createQuery(sql1);
      		List<Integer> count=(List<Integer>)query1.getResultList();
      		
      		for(int i=0;i<datelist.size();i++)
      		{
      			QRCodeMilk qr=new QRCodeMilk(datelist.get(i),count.get(i));
      			list.add(qr);      			
      		}
        
		return list;
	}

	@Override
	public List<Dispense> getDispenseToday() {
		
		Session session=sessionfactory.getCurrentSession();	
		
		Format f = new SimpleDateFormat("dd/MM/yyyy");
        String today = f.format(new Date()); 
     	
      	@SuppressWarnings("unchecked")
      	Query<Dispense> query=session.createQuery("FROM Dispense WHERE dsdate='"+today+"'");
      	List<Dispense> batch=(List<Dispense>)query.getResultList();	
        
		return batch;
	}

	@Override
	public List<Stmt> getDispenseMini() {
		Session session=sessionfactory.getCurrentSession();	
		
		//Today
		Format f = new SimpleDateFormat("dd/MM/yyyy");
		String today = f.format(new Date()); 
		String last;
		//5 days back date
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, -4);
		Date sevenDaysAgo = cal.getTime();
		last=f.format(sevenDaysAgo);
		
		
		String sql="select dsdate from Dispense where  dsdate between '"+last+"' AND '"+today+"' group by dsdate order by dsdate ASC";
        String sql1="select COUNT(*) from Dispense where dsdate between '"+last+"' AND '"+today+"' group by dsdate order by dsdate ASC";
        String sql2="select SUM(totalmilk) from Dispense where dsdate between '"+last+"' AND '"+today+"'GROUP by dsdate order by dsdate ASC";
        //Getting Date
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> datelist=(List<String>)query.getResultList();	
		
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql1);
		List<Long> count=(List<Long>)query1.getResultList();		
				
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query2=session.createQuery(sql2);
		List<Long> milk=(List<Long>)query2.getResultList();	
		
		
		List<Stmt> mini=new ArrayList<Stmt>();
		
		for(int i=0;i<datelist.size();i++)
		{
			Stmt p=new Stmt(datelist.get(i),""+count.get(i),""+milk.get(i));
			mini.add(p);
		}
		return mini;
		
	}

	@Override
	public List<Stmt> getDispenseDetails(String today, String last) {
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select dsdate from Dispense where  dsdate between '"+last+"' AND '"+today+"' group by dsdate order by dsdate ASC";
        String sql1="select COUNT(*) from Dispense where dsdate between '"+last+"' AND '"+today+"' group by dsdate order by dsdate ASC";
        String sql2="select SUM(totalmilk) from Dispense where dsdate between '"+last+"' AND '"+today+"' GROUP by dsdate order by dsdate ASC";
        //Getting Date
		@SuppressWarnings("unchecked")
		Query<String> query=session.createQuery(sql);
		List<String> datelist=(List<String>)query.getResultList();	
		
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql1);
		List<Long> count=(List<Long>)query1.getResultList();		
				
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Long> query2=session.createQuery(sql2);
		List<Long> milk=(List<Long>)query2.getResultList();	
		
		
		List<Stmt> mini=new ArrayList<Stmt>();
		
		for(int i=0;i<datelist.size();i++)
		{
			Stmt p=new Stmt(datelist.get(i),""+count.get(i),""+milk.get(i));
			mini.add(p);
		}
		return mini;
	}

	@Override
	public int getTotalRecipient() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select did from Donor where  rtype='1'";
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql);
		List<Long> count=(List<Long>)query1.getResultList();
		
		return count.size();
	}

	@Override
	public int getTotalOwn() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select did from Donor where  rtype='0'";
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql);
		List<Long> count=(List<Long>)query1.getResultList();
		
		return count.size();
	}

	@Override
	public int getTotalPast() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select pid from Past";
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql);
		List<Long> count=(List<Long>)query1.getResultList();
		
		return count.size();
	}

	@Override
	public int getTotalCulture() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="select cid from Culture";
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Long> query1=session.createQuery(sql);
		List<Long> count=(List<Long>)query1.getResultList();
		
		return count.size();
	}

	@Override
	public List<Donor> getRecipientDonor() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="from Donor where  rtype='0' order by edate DESC";
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Donor> query1=session.createQuery(sql);
		List<Donor> count=(List<Donor>)query1.getResultList();
		
		return count;
	}

	@Override
	public List<Donor> getOwnDonor() {
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="from Donor where  rtype='1' order by edate DESC";
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Donor> query1=session.createQuery(sql);
		List<Donor> count=(List<Donor>)query1.getResultList();
		
		return count;
	}

	@Override
	public List<Past> getPast() {
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="from Past order by pdate DESC";
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Past> query1=session.createQuery(sql);
		List<Past> count=(List<Past>)query1.getResultList();
		
		return count;
	}

	@Override
	public List<Culture> getCulture() {
		Session session=sessionfactory.getCurrentSession();		
		
		String sql="from Culture order by cdate DESC";
		//Get Count
		@SuppressWarnings("unchecked")
		Query<Culture> query1=session.createQuery(sql);
		List<Culture> count=(List<Culture>)query1.getResultList();
		
		return count;
	}

	@Override
	public String getTotalMilkCollection() {
		
		Session session=sessionfactory.getCurrentSession();	
		String sql2="select SUM(milk) from Donor";
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Integer> query2=session.createQuery(sql2);
		List<Integer> milk=(List<Integer>)query2.getResultList();	
		
		return ""+milk.get(0);
	}

	@Override
	public String getTotalMilkDispense() {
		
		Session session=sessionfactory.getCurrentSession();	
		String sql2="select SUM(totalmilk) from Dispense";
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Integer> query2=session.createQuery(sql2);
		List<Integer> milk=(List<Integer>)query2.getResultList();	
		
		return ""+milk.get(0);
	}

	@Override
	public boolean checkRoyaltycard(String qrcode) {
		
		Session session=sessionfactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		SQLQuery<Integer> query = session.createSQLQuery("select rid from Royalty where qrcode='"+qrcode+"'");
		List<Integer> admin=new ArrayList<Integer>();
	   	admin=(List<Integer>)query.getResultList();
	   	
	   	if(admin.size()==0)
	   	{
	   		return false;
	   	}
	   	else
	   	{
	   		return true;
	   	}		
	}

	@Override
	public void addRoyalty(Royalty r) {
		Session currentSession=sessionfactory.getCurrentSession();
		currentSession.saveOrUpdate(r);
		
	}

	@Override
	public void updateRoyalty(String qrcode) {
		
		Session session=sessionfactory.getCurrentSession();	
		String sql2="from Royalty where qrcode='"+qrcode+"'";
		//Get Milk
		@SuppressWarnings("unchecked")
		Query<Royalty> query2=session.createQuery(sql2);
		Royalty r=(Royalty)query2.getSingleResult();
		
		int donate=r.getDonate();
		donate++;
		r.setDonate(donate);
		session.saveOrUpdate(r);		
		
	}

	@Override
	public List<Royalty> getRoyaltyList() {
		
		Session session=sessionfactory.getCurrentSession();	
		
		String sql="from Royalty";
		//Get List
		@SuppressWarnings("unchecked")
		Query<Royalty> query1=session.createQuery(sql);
		List<Royalty> list=(List<Royalty>)query1.getResultList();
		
		return list;
	}

	@Override
	public List<Security> getSecurity() {
		
		Session session=sessionfactory.getCurrentSession();	
		String sql1="select * from Security";		
		
		SQLQuery<Security> query = session.createSQLQuery(sql1);
		
		List<Security> list=new ArrayList<Security>();
	   	list=(List<Security>)query.getResultList();
		
		return list;
	}

	@Override
	public boolean changeSecurity(String security, String answer, String newsecurity, String newanswer) {		
		
		Session session=sessionfactory.getCurrentSession();
		
		String old=security+answer;
		String newa=newsecurity+newanswer;
		
		try
		{
			Query<Security> query2=session.createQuery("from Security where question='"+old+"' and sid='1'");
			Security r=(Security)query2.getSingleResult();
			
			SQLQuery<Security> query = session.createSQLQuery("UPDATE Security SET question='"+newa+"' WHERE sid='1'");
	          
	        int result = query.executeUpdate();
	        System.out.println("Result: "+result);
	        if(result==1)
	        {
	        	return true;
	        }
	        else
	        {
	        	return false;
	        }	
		}
		catch(NoResultException e)
		{
			return false;
		}	
		
	}

	@Override
	public boolean checkSecurity(String security, String answer) {
		
		Session session=sessionfactory.getCurrentSession();
		
		String old=security+answer;
		
		try
		{
			Query<Security> query2=session.createQuery("from Security where question='"+old+"' and sid='1'");
			Security r=(Security)query2.getSingleResult();
			
			SQLQuery<Admin> query = session.createSQLQuery("UPDATE admin SET password='123456' WHERE adminid='1'");
	          
	        int result = query.executeUpdate();
	        System.out.println("Result: "+result);
	        if(result==1)
	        {
	        	return true;
	        }
	        else
	        {
	        	return false;
	        }	
		}
		catch(NoResultException e)
		{
			return false;
		}
	}

	@Override
	public boolean checkQRCode(String qrcode) {
		
		Session session=sessionfactory.getCurrentSession();
		
		@SuppressWarnings("unchecked")
		SQLQuery<Integer> query = session.createSQLQuery("select did from Donor where qrcode='"+qrcode+"'");
		
		List<Integer> admin=new ArrayList<Integer>();
	   	admin=(List<Integer>)query.getResultList();
	   	
	   	if(admin.size()==0)
	   	{
	   		return false;
	   	}
	   	else
	   	{
	   		return true;
	   	}	
	}

	@Override
	public boolean checkSample(String qr) {
		Session session=sessionfactory.getCurrentSession();
		
		//check donor is own or not
		@SuppressWarnings("unchecked")
		SQLQuery<String> query2 = session.createSQLQuery("select rtype from Donor where qrcode='"+qr+"'");
		
		String rtype=(String)query2.getSingleResult();
		
		if(rtype.equals("0")) {
			
			return false;
		}
		else
		{		
			//Retrieve batch no.		
			
			@SuppressWarnings("unchecked")
			SQLQuery<String> query = session.createSQLQuery("select batch from Donor where qrcode='"+qr+"'");
			
			String batch=(String)query.getSingleResult();
		   	
			if(!batch.equals(null)|| !batch.equals("")) {
				
				//Retrive discarded sample from batch
				@SuppressWarnings("unchecked")
				SQLQuery<String> query1 = session.createSQLQuery("select discard from Discard where batch='"+batch+"'");
				
				String discard=(String)query1.getSingleResult();
				
				//check whether qr code is present in discared sample
				
				if(discard.equals(""))
			 	{
					return false;
			 	}
				else
				{	
				 	 	String arr[] = discard.split(","); 	 	
				 	 	if(arr.length>0)
				 	 	{
					 	 	for(int i=0;i<arr.length;i++)
					 	 	{			 	 		
					 	 		if(arr[i].equals(qr))
					 	 		{
					 	 			return true;			
					 	 		}
					 	 	}
				 	 	}		 	 	
				}
			}
		}
		
		return false;	
		
	}

	@Override
	public boolean convertowntorec(String batch, String qr) {
		
		Session session=sessionfactory.getCurrentSession();
		//Check whether qr code present or not
		
		if(checkQRCode(qr)) {
			
			//if present then update into batch by catch and rtype into 1
			@SuppressWarnings("unchecked")
			SQLQuery<Admin> query = session.createSQLQuery("UPDATE Donor SET batch='"+batch+"', rtype='1' WHERE qrcode='"+qr+"'");
	          
	        int result = query.executeUpdate();
	        System.out.println("Result: "+result);
	        if(result==1)
	        {
	        	return true;
	        }
	        else
	        {
	        	return false;
	        }			
			
		}
		else
		{
			return false;
		}
	}

	@Override
	public Royalty getRoyalty(String search) {
		
		Session session=sessionfactory.getCurrentSession();
		Royalty royalty=null;
		
		@SuppressWarnings("unchecked")
		Query<Royalty> query2=session.createQuery("from Royalty where qrcode='"+search+"'");
		try {
		royalty=(Royalty)query2.getSingleResult();
		}catch(Exception e)
		{
			royalty=null;
		}
		return royalty;
	}

	@Override
	public List<Dispense> getRoyaltyDispense(String search) {
		
		Session session=sessionfactory.getCurrentSession();
		
		List<Dispense> dispense=new ArrayList<Dispense>();
		
		String sql1="select * from Dispense where royalty='"+search+"'";		
		
		SQLQuery<Dispense> query = session.createSQLQuery(sql1);		
		
		dispense=(List<Dispense>)query.getResultList();		
	   	
	   	return dispense;
	}

	@Override
	public String getOwnDispense() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		@SuppressWarnings("unchecked")
		SQLQuery<BigInteger> query2=session.createSQLQuery("SELECT count(*) FROM Donor as d inner join Dispense as ds on d.qrcode=ds.qrcode where d.rtype='0'");
		BigInteger sample=(BigInteger)query2.getSingleResult();
		System.out.println("Own Dispense: "+sample);
		return ""+sample;
	}

	@Override
	public String getRecDispense() {
		
		Session session=sessionfactory.getCurrentSession();		
		
		@SuppressWarnings("unchecked")
		SQLQuery<BigInteger> query2=session.createSQLQuery("SELECT count(*) FROM Donor as d inner join Dispense as ds on d.qrcode=ds.qrcode where d.rtype='1'");
		BigInteger sample=(BigInteger)query2.getSingleResult();
		System.out.println("Rec Dispense: "+sample);
		return ""+sample;
	}

	@Override
	public String getAvailableStock() {
		Session session=sessionfactory.getCurrentSession();		
		
		@SuppressWarnings("unchecked")
		SQLQuery<BigDecimal> query2=session.createSQLQuery("SELECT sum(negative) FROM Discard");
		BigDecimal sample=(BigDecimal)query2.getSingleResult();
		
		System.out.println("Available: "+sample);
		
		return ""+sample;
	}

	@Override
	public String getDiscared() {
		Session session=sessionfactory.getCurrentSession();		
		
		@SuppressWarnings("unchecked")
		SQLQuery<BigDecimal> query2=session.createSQLQuery("SELECT sum(positive) FROM Discard");
		BigDecimal sample=(BigDecimal)query2.getSingleResult();
		
		System.out.println("Discared: "+sample);
		return ""+sample;
	}

	@Override
	public List<DispenseDonor> getDispenseDonor() {
		Session session=sessionfactory.getCurrentSession();	
		
		List<String> date=new ArrayList<String>();
		List<String> rname=new ArrayList<String>();
		List<Integer> milk=new ArrayList<Integer>();
		List<String> qr=new ArrayList<String>();
		
		String sql1="SELECT dis.dsdate FROM Dispense as dis JOIN Donor as don on dis.qrcode=don.qrcode WHERE don.rtype='1' order by dis.dsid desc ";	
		String sql2="SELECT dis.rname FROM Dispense as dis JOIN Donor as don on dis.qrcode=don.qrcode WHERE don.rtype='1' order by dis.dsid desc ";	
		String sql3="SELECT dis.totalmilk FROM Dispense as dis JOIN Donor as don on dis.qrcode=don.qrcode WHERE don.rtype='1' order by dis.dsid desc ";	
		String sql4="SELECT dis.qrcode FROM Dispense as dis JOIN Donor as don on dis.qrcode=don.qrcode WHERE don.rtype='1' order by dis.dsid desc ";	
		
		SQLQuery<String> query = session.createSQLQuery(sql1);		
		date=(List<String>)query.getResultList();	
		
		SQLQuery<String> query1 = session.createSQLQuery(sql2);		
		rname=(List<String>)query1.getResultList();	
		
		SQLQuery<Integer> query2 = session.createSQLQuery(sql3);		
		milk=(List<Integer>)query2.getResultList();	
		
		SQLQuery<String> query3 = session.createSQLQuery(sql4);		
		qr=(List<String>)query3.getResultList();	
	   	
		List<DispenseDonor> list=new ArrayList<DispenseDonor>();
		
		for(int i=0;i<date.size();i++)
		{
			DispenseDonor ds=new DispenseDonor(date.get(i).toString(), rname.get(i).toString(), qr.get(i).toString(), milk.get(i));
			list.add(ds);
		}
		
	   	return list;		
	}

	@Override
	public List<String> getApproveQR() {
		Session session=sessionfactory.getCurrentSession();			
		
		List<String> list=new ArrayList<String>();
		
		String sql1="SELECT approve FROM discard order by ddid";
		SQLQuery<String> query = session.createSQLQuery(sql1);		
		list=(List<String>)query.getResultList();	
		
		
		//Convert list into qr code List
		
		List<String> qr=new ArrayList<String>();
		
		for(String s:list)
		{
			String arr[] = s.split(","); 	 	
	 	 	if(arr.length>0)
	 	 	{
		 	 	for(int i=0;i<arr.length;i++)
		 	 	{			 	 		
		 	 		qr.add(arr[i]);
		 	 	}
	 	 	}
		}
		
		return qr;
	}

	@Override
	public List<Donor> getDonorList(List<String> qr) {
		
		Session session=sessionfactory.getCurrentSession();		
		
		List<Donor> list=new ArrayList<Donor>();
		
		for(String s:qr)
		{			
			Query<Donor> query=session.createQuery("from Donor where qrcode=:qr",Donor.class);
			query.setParameter("qr", s);		
			Donor d=query.uniqueResult();
			
			list.add(d);
		}		
		
		System.out.println("list size: "+list.size());
		return list;
	}

	@Override
	public List<String> getDiscardQR() {
Session session=sessionfactory.getCurrentSession();			
		
		List<String> list=new ArrayList<String>();
		
		String sql1="SELECT discard FROM discard order by ddid";
		SQLQuery<String> query = session.createSQLQuery(sql1);		
		list=(List<String>)query.getResultList();	
		
		
		//Convert list into qr code List
		
		List<String> qr=new ArrayList<String>();
		
		for(String s:list)
		{
			String arr[] = s.split(","); 	 	
	 	 	if(arr.length>0)
	 	 	{
		 	 	for(int i=0;i<arr.length;i++)
		 	 	{			 	 		
		 	 		qr.add(arr[i]);
		 	 	}
	 	 	}
		}
		
		return qr;
	}

	@Override
	public List<DispenseDonor> getDispenseOwn() {
		Session session=sessionfactory.getCurrentSession();	
		
		List<String> date=new ArrayList<String>();
		List<String> rname=new ArrayList<String>();
		List<Integer> milk=new ArrayList<Integer>();
		List<String> qr=new ArrayList<String>();
		
		String sql1="SELECT dis.dsdate FROM Dispense as dis JOIN Donor as don on dis.qrcode=don.qrcode WHERE don.rtype='0' order by dis.dsid desc ";	
		String sql2="SELECT dis.rname FROM Dispense as dis JOIN Donor as don on dis.qrcode=don.qrcode WHERE don.rtype='0' order by dis.dsid desc ";	
		String sql3="SELECT dis.totalmilk FROM Dispense as dis JOIN Donor as don on dis.qrcode=don.qrcode WHERE don.rtype='0' order by dis.dsid desc ";	
		String sql4="SELECT dis.qrcode FROM Dispense as dis JOIN Donor as don on dis.qrcode=don.qrcode WHERE don.rtype='0' order by dis.dsid desc ";	
		
		SQLQuery<String> query = session.createSQLQuery(sql1);		
		date=(List<String>)query.getResultList();	
		
		SQLQuery<String> query1 = session.createSQLQuery(sql2);		
		rname=(List<String>)query1.getResultList();	
		
		SQLQuery<Integer> query2 = session.createSQLQuery(sql3);		
		milk=(List<Integer>)query2.getResultList();	
		
		SQLQuery<String> query3 = session.createSQLQuery(sql4);		
		qr=(List<String>)query3.getResultList();	
	   	
		List<DispenseDonor> list=new ArrayList<DispenseDonor>();
		
		for(int i=0;i<date.size();i++)
		{
			DispenseDonor ds=new DispenseDonor(date.get(i).toString(), rname.get(i).toString(), qr.get(i).toString(), milk.get(i));
			list.add(ds);
		}
		
	   	return list;
	}

	@Override
	public List<Donor> getAvailableOwn() {
		Session session=sessionfactory.getCurrentSession();	
		List<Donor> list=new ArrayList<Donor>();
		
		String sql1="FROM Donor WHERE qrcode NOT IN (SELECT qrcode FROM Dispense) and rtype='0' ";	
		
		Query<Donor> query=session.createQuery(sql1,Donor.class);
		list=(List<Donor>)query.getResultList();	
		
	   	return list;
	}		
}
