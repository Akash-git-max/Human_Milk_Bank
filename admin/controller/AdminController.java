package com.admin.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
import com.admin.service.AdminServiceInterface;
import com.google.zxing.WriterException;
import com.util.PrintJasperReport;
import com.util.PrintQRCode;
import com.util.QRCode;

import net.sf.jasperreports.engine.JRException;

@Controller
@RequestMapping("/admin")
public class AdminController {	
	
	String qr,abspath;
	
	private final String expire="12/12/2020";
	
	@Autowired
	private AdminServiceInterface service;	
	
	
	public String getHospitalName()
	{
		return "Demo Hospital";
	}
	
	//Admin Data controller
	@GetMapping("/login")
	public String adminLogin(Model theModel) throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
	    String to =formatter.format(new Date());  		    
	    Date today=formatter.parse(to);
		Date dateAfter = formatter.parse(expire);
		long difference =  dateAfter.getTime()-today.getTime();
		int diff = (int) (difference / (1000*60*60*24));
		System.out.println("Difference: "+diff);
		if(diff>=0)
		{
			return "login";
		}
		else
		{
			return "expired";
		}
		//return "login";
	}
	
	@PostMapping("/adminLogin")
	public String adminLoginBean(@RequestParam("username")String username, @RequestParam("password")String password,Model themodel,HttpServletRequest httpServletRequest, HttpSession session) {
		
		String msg = "";		
		HttpSession httpSession = httpServletRequest.getSession();
		List<Admin> admin = (List<Admin>)service.findUser(username, password);
        
        if(admin.isEmpty()) {
        	msg = "Invalid credentials";
        } else {
        	httpSession.setAttribute("admin","admin"); 
        	String milkc=service.getTotalMilkCollection();
        	if(milkc.equals(null)|| milkc.equals("null"))
        	{
        		milkc=""+0;
        	}
			String milkd=service.getTotalMilkDispense();
			if(milkd.equals(null)||milkd.equals("null"))
        	{
        		milkd=""+0;
        	}
			themodel.addAttribute("milkc",milkc);
			themodel.addAttribute("milkd",milkd);
			
            return "dashboard";            
        }
        themodel.addAttribute("msg", msg);
        System.out.println(msg);
		
		return "login";
	}
	
	@RequestMapping("/forgetpass")
	public String forgetpass(HttpServletRequest httpServletRequest,Model themodel)
	{		
		HttpSession httpSession = httpServletRequest.getSession();		
		return "forgetpass";
	}
	
	@RequestMapping("/forgetpassbean")
	public String forgetpassbean(@RequestParam("security")String security, @RequestParam("answer")String answer,HttpServletRequest httpServletRequest,Model themodel)
	{		
		HttpSession httpSession = httpServletRequest.getSession();
		String msg="";
		if(service.checkSecurity(security,answer))
		{
			msg="Password Reset Successfully, New Password is 123456";
		}else
		{
			msg="Please enter valid security question and answer";
		}
		themodel.addAttribute("msg", msg);	
		return "forgetpass";
	}
	
	@RequestMapping("/security")
	public String security(HttpServletRequest httpServletRequest,Model themodel)
	{
		HttpSession httpSession = httpServletRequest.getSession();
		return "security";
	}
	
	@RequestMapping("/securitybean")
	public String securitybean(HttpServletRequest httpServletRequest,Model themodel,
			@RequestParam("security")String security, @RequestParam("answer")String answer,
			@RequestParam("security1")String newsecurity, @RequestParam("answer1")String newanswer)
	{		
		HttpSession httpSession = httpServletRequest.getSession();		
		
		String msg="";
		String errmsg="";	
		
		
		if(service.changeSecurity(security,answer,newsecurity,newanswer))
		{
			msg="Security Question Changed Successfully"; 
		}
		else
		{
			errmsg="Please Enter Valid Security Question and Answer"; 
		}
		
		themodel.addAttribute("msg", msg);
		themodel.addAttribute("errmsg", errmsg);
		return "security";
	}
	
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest httpServletRequest)
	{		
		HttpSession httpSession = httpServletRequest.getSession();
        httpSession.invalidate();
		return "login";
	}
	
	@PostMapping("/search")
	public String Searching(@RequestParam("search")String search,Model themodel,HttpServletRequest httpServletRequest, HttpSession session) 
	{
		SearchEntity se=service.searchResult(search);
		
		httpServletRequest.setAttribute("search", se);
		return "search-result";
	}
	
	@RequestMapping("/home")
	public String getHome(Model themodel,HttpServletRequest httpServletRequest)
	{
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession!=null || httpServletRequest.isRequestedSessionIdValid())
		{			
				String milkc=service.getTotalMilkCollection();
				String milkd=service.getTotalMilkDispense();
				themodel.addAttribute("milkc",milkc);
				themodel.addAttribute("milkd",milkd);
				
				return "dashboard";			
		}else
		{
			return "login";
		}		
		
	}
	
	@RequestMapping("/changepass")
	public String changePassword(Model theModel,HttpServletRequest httpServletRequest)
	{	
		HttpSession httpSession = httpServletRequest.getSession();
		
		if(httpSession!=null || httpServletRequest.isRequestedSessionIdValid())
		{
			if(httpSession.getAttribute("admin").equals("admin"))
			{
				return "changepass";
			}else
			{
				return "login";
			}
		}else
		{
			return "login";
		}			
	}
	
	
	@PostMapping("/changepassbean")
	public String changePassBean(@RequestParam("userId")String userid,@RequestParam("newUsername")String username, @RequestParam("newPassword")String password,Model themodel,HttpServletRequest httpServletRequest, HttpSession session) {
		
		String msg = "";	
		
        boolean isValid = service.changePass(userid, username, password);
        
        
        if(isValid) {  
        	msg="Password Changed Successfully";            
        } else {
            msg = "Password Not Changed";
        }
        themodel.addAttribute("msg", msg);
        System.out.println(msg);
		
		return "changepass";
	}
	
	
	//Donor Registration
	
	@RequestMapping("/registration")
	public String donorRegistration(Model theModel) throws WriterException, IOException
	{	
		QRCode qrcode=new QRCode();
		qr=qrcode.getQRCodeNumber();
		abspath=qrcode.absolutePath();
		service.setQRCodeImage(qrcode.getImage());
		
		theModel.addAttribute("qrcode",qr);
		theModel.addAttribute("hospital", getHospitalName());
		String batch=service.lastBatch();
		
		//check if batch is already pasturize
		if(!service.checkBatch(batch)) {
			int b=Integer.parseInt(batch);
			b++;
			batch=""+b;
		}
		theModel.addAttribute("batch",batch);
		
		return "donor-registration";
	}	
	
	@PostMapping("/donorregistere")
	public String donorRegistered(@RequestParam("edate")String edate,@RequestParam("hname")String hname,
			@RequestParam("contact")String contact,@RequestParam("batch")String batch,@RequestParam("mname")String mname,
			@RequestParam("fhname")String fhname,@RequestParam("surname")String surname,@RequestParam("dob")String dob,
			@RequestParam("rtype")String rtype,@RequestParam("hiv")String hiv,@RequestParam("vdrl")String vdrl,
			@RequestParam("hbsag")String hbsag,@RequestParam("milk")String milk,@RequestParam("qrcode")String qrcode,Model theModel,
			HttpServletRequest request, HttpServletResponse response) throws JRException, NamingException, SQLException, IOException, WriterException 
	{	
		if(service.checkQRCode(qrcode))
		{
			QRCode qrcode1=new QRCode();
			qr=qrcode1.getQRCodeNumber();
			abspath=qrcode1.absolutePath();
			service.setQRCodeImage(qrcode1.getImage());
			
			theModel.addAttribute("qrcode",qr);
			theModel.addAttribute("hospital", getHospitalName());
			String batch1=service.lastBatch();
			
			//check if batch is already pasturize
			if(!service.checkBatch(batch1)) {
				int b=Integer.parseInt(batch1);
				b++;
				batch1=""+b;
			}
			theModel.addAttribute("batch",batch1);
			theModel.addAttribute("errmsg","This Sample is Already Registered.");
			
			return "donor-registration";
		}
		else {
			Donor donor=new Donor();donor.setEdate(edate);donor.setHospital(hname);	donor.setMobile(contact);donor.setBatch(batch);	donor.setMother(mname);
			donor.setFhname(fhname);donor.setSurname(surname);donor.setDob(dob);donor.setRtype(rtype);donor.setHiv(hiv);donor.setVdrl(vdrl);donor.setHbsag(hbsag);
			donor.setMilk(Integer.parseInt(milk));donor.setQrcode(qrcode);
			System.out.println("Save: " +donor.toString());
			theModel.addAttribute("donor", donor);
			service.saveDonor(donor);		
			theModel.addAttribute("img",service.getQRCodeImage());		 
			
			return "success";
		}
	}
	
	@RequestMapping("/printQRCode")
	public void printQRCode(Model theModel,HttpServletRequest request, HttpServletResponse response) throws JRException, NamingException, SQLException, IOException
	{			
		//Select the Report according to flag
		String filename="qrcode";		
						
		//Printing the Jasper Report
						
		HashMap<String, Object> hm=new HashMap<String, Object>();
						
		hm.put("image", service.getQRCodeImage());		
		System.out.println(hm);
		PrintJasperReport.printreport(filename,request,response,hm);
		
	}
	
	@RequestMapping("/dcollection")
	public String donorCollection(Model theModel)
	{			
		List<String> dlist=service.getRDonorToday();
		if(dlist.isEmpty())
		{
			theModel.addAttribute("msg","No Data Enter Today");
		}
		else
		{
			theModel.addAttribute("count",dlist.get(0));
			theModel.addAttribute("milk",dlist.get(1));	
		}
				
		return "donor-collection";
	}
	@RequestMapping("/dmini")
	public String donorMiniStatement(Model theModel,HttpServletRequest httpServletRequest)
	{	
		List<Stmt> mini=service.getRDonorMini();
		if(mini.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);
			
			System.out.println("Size: "+mini.size());
		}
		return "donor-mini";
	}
	@RequestMapping("/dstatement")
	public String donorDetailStatement(Model theModel)
	{	
		return "donor-statement";
	}
	
	@RequestMapping("/dstmt")
	public String donorStatement(@RequestParam("sdate")String sdate, @RequestParam("edate")String edate,Model themodel,HttpServletRequest httpServletRequest) throws ParseException
	{	
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = sdf.parse(sdate);
		String ss=new SimpleDateFormat("dd/MM/yyyy").format(startdate);
		
		Date enddate=sdf.parse(edate);
		String ee=new SimpleDateFormat("dd/MM/yyyy").format(enddate);
		
		List<Stmt> mini=service.getRDonorDetails(ee,ss);
		
		if(mini.isEmpty())
		{
			themodel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);			
			System.out.println("Size: "+mini.size());
		}
		return "donor-statement";
	}
	
	//Own Mother Registration	
		@RequestMapping("/ownregistration")
		public String owndonorRegistration(Model theModel) throws WriterException, IOException
		{	
			QRCode qrcode=new QRCode();
			String qr=qrcode.getQRCodeNumber();
			service.setQRCodeImage(qrcode.getImage());
			
			theModel.addAttribute("qrcode",qr);
			theModel.addAttribute("hospital", getHospitalName());
			String batch=service.lastBatch();		
			theModel.addAttribute("batch",batch);
			
			return "own-registration";
		}
	
		@PostMapping("/owndonorregistere")
		public String owndonorRegistered(@RequestParam("edate")String edate,@RequestParam("hname")String hname,
				@RequestParam("contact")String contact,@RequestParam("mname")String mname,
				@RequestParam("fhname")String fhname,@RequestParam("surname")String surname,@RequestParam("dob")String dob,
				@RequestParam("rtype")String rtype,@RequestParam("hiv")String hiv,@RequestParam("vdrl")String vdrl,
				@RequestParam("hbsag")String hbsag,@RequestParam("milk")String milk,@RequestParam("qrcode")String qrcode,Model theModel) throws WriterException, IOException
		{	
			if(service.checkQRCode(qrcode))
			{
				QRCode qrcode1=new QRCode();
				String qr=qrcode1.getQRCodeNumber();
				service.setQRCodeImage(qrcode1.getImage());
				
				theModel.addAttribute("qrcode",qr);
				theModel.addAttribute("hospital", getHospitalName());
				String batch=service.lastBatch();		
				theModel.addAttribute("batch",batch);
				theModel.addAttribute("errmsg","This Sample is Already Registered.");
				return "own-registration";
			}
			else {
				Donor donor=new Donor();donor.setEdate(edate);donor.setHospital(hname);	donor.setMobile(contact);donor.setBatch("");donor.setMother(mname);
				donor.setFhname(fhname);donor.setSurname(surname);donor.setDob(dob);donor.setRtype(rtype);donor.setHiv(hiv);donor.setVdrl(vdrl);donor.setHbsag(hbsag);
				donor.setMilk(Integer.parseInt(milk));donor.setQrcode(qrcode);
				System.out.println("Save: " +donor.toString());
				theModel.addAttribute("donor", donor);
				service.saveDonor(donor);
				
				//new PrintQRCode(service.getQRCodeImage(),qr);
				
				
				return "ownsuccess";
			}
		}
		
		@RequestMapping("/owndcollection")
		public String owndonorCollection(Model theModel)
		{	
			List<String> dlist=service.getODonorToday();
			if(dlist.isEmpty())
			{
				theModel.addAttribute("msg","No Data Enter Today");
			}
			else
			{
				theModel.addAttribute("count",dlist.get(0));
				theModel.addAttribute("milk",dlist.get(1));	
			}
			return "own-collection";
		}
		@RequestMapping("/owndmini")
		public String owndonorMiniStatement(Model theModel,HttpServletRequest httpServletRequest)
		{	
			List<Stmt> mini=service.getODonorMini();
			if(mini.isEmpty())
			{
				theModel.addAttribute("msg","No Data is Present");
				httpServletRequest.setAttribute("mini", mini);
			}
			else
			{
				httpServletRequest.setAttribute("mini", mini);
				
				System.out.println("Size: "+mini.size());
			}			
			return "own-mini";
		}
		@RequestMapping("/owndstatement")
		public String owndonorDetailStatement(Model theModel)
		{	
			return "own-statement";
		}
		
		@RequestMapping("/ownstmt")
		public String owndonorStatement(@RequestParam("sdate")String sdate, @RequestParam("edate")String edate,Model themodel,HttpServletRequest httpServletRequest) throws ParseException
		{	
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date startdate = sdf.parse(sdate);
			String ss=new SimpleDateFormat("dd/MM/yyyy").format(startdate);
			
			Date enddate=sdf.parse(edate);
			String ee=new SimpleDateFormat("dd/MM/yyyy").format(enddate);
			
			List<Stmt> mini=service.getODonorDetails(ee,ss);
			
			if(mini.isEmpty())
			{
				themodel.addAttribute("msg","No Data is Present");
				httpServletRequest.setAttribute("mini", mini);
			}
			else
			{
				httpServletRequest.setAttribute("mini", mini);			
				System.out.println("Size: "+mini.size());
			}
			return "own-statement";
		}
	
	
	//pasturization
	@RequestMapping("/pastform")
	public String pastForm(Model theModel)
	{	
		List<PastData> data=service.pastData();
		
		theModel.addAttribute("pastdata", data);
		
		return "past-form";
	}
	
	@PostMapping("/past-form-bean")
	public String pastFormBean(@RequestParam("pdate")String pdate,@RequestParam("batch")String batch,@RequestParam("remark")String remark,
			@RequestParam("operator")String operator,@RequestParam("samples")String samples,@RequestParam("milk")String milk,Model theModel)
	{	
		System.out.println("Batch is: "+batch);
		if(service.checkBatch(batch))
		{			
			System.out.println("Past");
			
			Past past=new Past();
			past.setBatch(batch);past.setPdate(pdate);past.setPoperator(operator);past.setRemark(remark);
			past.setTotalmilk(Integer.parseInt(milk));past.setUnits(samples);
			service.savePast(past);
			theModel.addAttribute("msg", "Batch is Pasturized Successfully!");
		}
		else
		{
			System.out.println("Already Past");
			theModel.addAttribute("errmsg", "Batch is Already pasturized !");
		}
		
		List<PastData> data=service.pastData();		
		theModel.addAttribute("pastdata", data);
		
		return "past-form";
	}
	
	@RequestMapping("/pasttoday")
	public String pastToday(Model theModel,HttpServletRequest httpServletRequest)
	{	
		List<PastReport> dlist=service.getPastToday();
		
		if(dlist.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("list", dlist);
		}
		else
		{
			httpServletRequest.setAttribute("list", dlist);			
			System.out.println("Size: "+dlist.size());
		}
		
		return "past-collection";
	}
	@RequestMapping("/pastmini")
	public String pastMiniStatement(Model theModel,HttpServletRequest httpServletRequest)
	{	
		List<PastReport> mini=service.getPastMini();
		if(mini.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);
			
			System.out.println("Size: "+mini.size());
		}
		
		return "past-mini";
	}
	@RequestMapping("/pastdetail")
	public String pastDetailStatement(Model theModel)
	{	
		return "past-statement";
	}	
	
	@RequestMapping("/pstmt")
	public String pastStatement(@RequestParam("sdate")String sdate, @RequestParam("edate")String edate,Model themodel,HttpServletRequest httpServletRequest) throws ParseException
	{	
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = sdf.parse(sdate);
		String ss=new SimpleDateFormat("dd/MM/yyyy").format(startdate);
		
		Date enddate=sdf.parse(edate);
		String ee=new SimpleDateFormat("dd/MM/yyyy").format(enddate);
		
		List<PastReport> mini=service.getPastDetails(ee,ss);
		
		if(mini.isEmpty())
		{
			themodel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);			
			System.out.println("Size: "+mini.size());
		}
		return "past-statement";
	}
	
	//Culture
	@RequestMapping("/cultureform")
	public String cultureForm(Model theModel)
	{			
		List<PastData> data=service.pastData();		
		theModel.addAttribute("pastdata", data);		
		return "culture-form";
	}
	
	@RequestMapping("/culture-form-bean")
	public String culturebeanForm(@RequestParam("cdate")String cdate,@RequestParam("batch")String batch,@RequestParam("operator")String operator,@RequestParam("samples")String samples,@RequestParam("milk")String milk,Model theModel)
	{	
		
		if(service.checkPastBatch(batch)) 
		{
			//Batch is in Past Form proceed to Culture
			
			Culture cul=new Culture();
			cul.setBatch(batch);cul.setCdate(cdate);cul.setCoperator(operator);cul.setTotalmilk(Integer.parseInt(milk));cul.setUnits(samples);
			service.saveCulture(cul);
			theModel.addAttribute("msg", "Batch is Cultured Successfully!");
		}
		else
		{
			//Batch is not in Past show msg first Past the Batch
			theModel.addAttribute("errmsg", "Batch is not Pastulized or Already Cultured !");
		}
		
		List<PastData> data=service.pastData();		
		theModel.addAttribute("pastdata", data);
		
		return "culture-form";
	}
	@RequestMapping("/culturetoday")
	public String cultureToday(Model theModel ,HttpServletRequest httpServletRequest)
	{	
		
		List<Culture> dlist=service.getCultureToday();
		
		if(dlist.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("list", dlist);
		}
		else
		{
			httpServletRequest.setAttribute("list", dlist);			
			System.out.println("Size: "+dlist.size());
		}
		
		return "culture-collection";
	}
	@RequestMapping("/culturemini")
	public String cultureMiniStatement(Model theModel,HttpServletRequest httpServletRequest)
	{	
		List<Culture> mini=service.getCultureMini();
		if(mini.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);			
			System.out.println("Size: "+mini.size());
		}
		
		return "culture-mini";
	}
	@RequestMapping("/culturedetail")
	public String cultureDetailStatement(Model theModel)
	{	
		return "culture-statement";
	}
	
	@RequestMapping("/cstmt")
	public String cStatement(@RequestParam("sdate")String sdate, @RequestParam("edate")String edate,Model themodel,HttpServletRequest httpServletRequest) throws ParseException
	{	
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = sdf.parse(sdate);
		String ss=new SimpleDateFormat("dd/MM/yyyy").format(startdate);
		
		Date enddate=sdf.parse(edate);
		String ee=new SimpleDateFormat("dd/MM/yyyy").format(enddate);
		
		List<Culture> mini=service.getCultureDetails(ee,ss);
		
		if(mini.isEmpty())
		{
			themodel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);			
			System.out.println("Size: "+mini.size());
		}
		return "culture-statement";
	}
	
	//Discard
	@RequestMapping("/discardform")
	public String discardForm(Model theModel)
	{	
		List<PastData> data=service.pastData();		
		theModel.addAttribute("pastdata", data);
		
		return "discard-form";
	}	
	
	@RequestMapping("/discard-form-bean")
	public String discardFormBean(@RequestParam("cdate")String cdate, @RequestParam("batch")String batch, @RequestParam("operator")String operator, @RequestParam("discard")String discard, @RequestParam("approve")String approve,Model themodel,HttpServletRequest httpServletRequest)
	{	
		//Check Batch whether already process or not
		if(service.checkCultureBatch(batch)) 
		{
			//Count Positive Samples
			String str=discard; int dcount=0;
			if(!(discard.equals("") || discard.equals(null)))
			{
				for(String subString: str.split(",")){
					   if(subString!=null)
					   {
						   dcount++;
					   }
					}
			}
			
			
			//Count Negative Samples
			String str1=approve; int acount=0;
			if(!(approve.equals("") || approve.equals(null)))
			{
				for(String subString: str1.split(",")){
					   if(subString!=null)
					   {
						   acount++;
					   }
					}
			}
			
			
			Discard dis=new Discard();
			dis.setBatch(batch);dis.setDddate(cdate);dis.setPositive(dcount);dis.setNegative(acount);dis.setDiscard(discard);dis.setApprove(approve);dis.setDoperator(operator);
			service.saveDiscard(dis);
			
			themodel.addAttribute("msg", "Batch is Process Successfully!");
		}
		else
		{
			//Batch is not in Culture show msg first Past the Batch
			themodel.addAttribute("errmsg", "Batch is not Culture or Already Discarded !");
		}
		
		
		List<PastData> data=service.pastData();		
		themodel.addAttribute("pastdata", data);
		return "discard-form";
	}
	
	@RequestMapping("/discardtoday")
	public String discardToday(Model theModel,HttpServletRequest httpServletRequest)
	{	
		List<Discard> dlist=service.getDiscardToday();
		
		if(dlist.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("list", dlist);
		}
		else
		{
			httpServletRequest.setAttribute("list", dlist);			
			System.out.println("Size: "+dlist.size());
		}
		
		
		return "discard-collection";
	}
	@RequestMapping("/discardmini")
	public String discardMiniStatement(Model theModel,HttpServletRequest httpServletRequest)
	{	
		List<Discard> mini=service.getDiscardMini();
		if(mini.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);			
			System.out.println("Size: "+mini.size());
		}
		
		return "discard-mini";
	}
	@RequestMapping("/discarddetail")
	public String discardDetailStatement(Model theModel)
	{	
		return "discard-statement";
	}	
	
	@RequestMapping("/disstmt")
	public String discardDetail(@RequestParam("sdate")String sdate, @RequestParam("edate")String edate,Model themodel,HttpServletRequest httpServletRequest) throws ParseException
	{	
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = sdf.parse(sdate);
		String ss=new SimpleDateFormat("dd/MM/yyyy").format(startdate);
		
		Date enddate=sdf.parse(edate);
		String ee=new SimpleDateFormat("dd/MM/yyyy").format(enddate);
		
		List<Discard> mini=service.getDiscardDetails(ee,ss);
		
		if(mini.isEmpty())
		{
			themodel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);			
			System.out.println("Size: "+mini.size());
		}
		return "discard-statement";
	}
	
	//Despense
	@RequestMapping("/dispenseform")
	public String dispenseForm(Model theModel)
	{	
		List<QRCodeMilk> list=service.getQRCodeMilk();
		theModel.addAttribute("list", list);
		
		return "dispense-form";
	}
	@PostMapping("/dispence-form-bean")
	public String dispenseFormBean(@RequestParam("cdate")String cdate, @RequestParam("infant")String infant,@RequestParam("tag")String tag,
			@RequestParam("qr")String qr,@RequestParam("milk")String milk,@RequestParam("dtype")String dtype,@RequestParam("rname")String rname,
			@RequestParam("royalty")String royalty,@RequestParam("remark")String remark,Model themodel,HttpServletRequest httpServletRequest)
	{	
		
		//If QR is present in Donor Table
		if(service.checkDonorQR(qr))
		{
			//If QR code is present in Dispence Table
			if(service.checkDispenseQR(qr))
			{
				themodel.addAttribute("errmsg", "The Given Sample is aready Dispenced.");				
			}
			else
			{
				if(service.checkSample(qr))
				{
					themodel.addAttribute("errmsg", "The sample is Discared");
				}
				else
				{
					//QR is not in Dispence Then Save the Object
					Dispense dis=new Dispense();
					dis.setInfantname(infant);dis.setQrcode(qr);dis.setTotalmilk(Integer.parseInt(milk));dis.setTag(tag);dis.setRecipient(dtype);dis.setRname(rname);dis.setRemark(remark);dis.setDsdate(cdate);dis.setRoyalty(royalty);
						
					service.saveDispence(dis);
					themodel.addAttribute("msg", "The sample Dispence Successfully");
				}
				
			}
		}
		else
		{
			themodel.addAttribute("errmsg", "Then QR Code is not in the database");			
		}
		
		List<QRCodeMilk> list=service.getQRCodeMilk();
		themodel.addAttribute("list", list);
		
		return "dispense-form";
		
	}
	
	
	@RequestMapping("/dispensetoday")
	public String dispenseToday(Model theModel,HttpServletRequest httpServletRequest)
	{	
		List<Dispense> dlist=service.getDispenseToday();
		
		if(dlist.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("list", dlist);
		}
		else
		{
			httpServletRequest.setAttribute("list", dlist);			
			System.out.println("Size: "+dlist.size());
		}
		return "dispense-collection";
	}
	
	@RequestMapping("/dispensemini")
	public String dispenseMiniStatement(Model theModel,HttpServletRequest httpServletRequest)
	{	
		List<Stmt> mini=service.getDispenseMini();
		if(mini.isEmpty())
		{
			theModel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);			
			System.out.println("Size: "+mini.size());
		}
		return "dispense-mini";
	}
	@RequestMapping("/dispensedetail")
	public String dispenseDetailStatement(Model theModel)
	{	
		return "dispense-statement";
	}
	
	@RequestMapping("/dipstmt")
	public String dispenseDetail(@RequestParam("sdate")String sdate, @RequestParam("edate")String edate,Model themodel,HttpServletRequest httpServletRequest) throws ParseException
	{	
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = sdf.parse(sdate);
		String ss=new SimpleDateFormat("dd/MM/yyyy").format(startdate);
		
		Date enddate=sdf.parse(edate);
		String ee=new SimpleDateFormat("dd/MM/yyyy").format(enddate);
		
		List<Stmt> mini=service.getDispenseDetails(ee,ss);
		
		if(mini.isEmpty())
		{
			themodel.addAttribute("msg","No Data is Present");
			httpServletRequest.setAttribute("mini", mini);
		}
		else
		{
			httpServletRequest.setAttribute("mini", mini);			
			System.out.println("Size: "+mini.size());
		}
		return "dispense-statement";
	}
	
	//Store
	@RequestMapping("/storesummary")
	public String storeSummary(Model theModel)
	{	
		//Get total Recipient Collection
		int rcount=service.getTotalRecipient();
		theModel.addAttribute("rec", rcount);
		
		//get total Own Mother Collection
		int ocount=service.getTotalOwn();
		theModel.addAttribute("own", ocount);
		
		//get Past 
		int pastcount=service.getTotalPast();
		theModel.addAttribute("past", pastcount);
		
		//get total culture
		int culcount=service.getTotalCulture();
		theModel.addAttribute("cul", culcount);	
		
		//get own milk dispensed		
		String odis=service.getOwnDispense();
		if(odis.equals(null)|| odis.equals(""))
		{
			odis=""+0;
		}
		theModel.addAttribute("odis", odis);
		
		//get recepient milk dispensed
		String recdis=service.getRecDispense();
		if(recdis.equals(null)|| recdis.equals(""))
		{
			recdis=""+0;
		}
		theModel.addAttribute("recdis", recdis);
		
		//get total available samples
		String available=service.getAvailableStock();	
		if(available.equals(null)|| available.equals("null"))
		{
			available=""+0;
		}
		int rem=Integer.parseInt(available)-Integer.parseInt(recdis);
		theModel.addAttribute("rem", rem);
		
		//get total Discarded samples
		String discard=service.getDiscared();
		if(discard.equals(null)|| discard.equals("null"))
		{
			discard=""+0;
		}
		//own remailning
		int ownrem=ocount-Integer.parseInt(odis);
		theModel.addAttribute("ownrem", ownrem);
		theModel.addAttribute("discard", discard);
		return "store-summary";
	}
	
	
	@RequestMapping("/storeown")
	public String storeown(Model theModel)
	{	
		
		List<Donor> list=service.getRecipientDonor();
		theModel.addAttribute("list", list);	
		
		return "store-own";
	}
	@RequestMapping("/storerecipient")
	public String storerecipient(Model theModel)
	{	
		List<Donor> list=service.getOwnDonor();
		theModel.addAttribute("list", list);	
		return "store-recipient";
	}
	@RequestMapping("/storepast")
	public String storepast(Model theModel)
	{	
		List<Past> list=service.getPast();
		theModel.addAttribute("list", list);	
		return "store-past";
	}
	@RequestMapping("/storeculture")
	public String storeculture(Model theModel)
	{	
		List<Culture> list=service.getCulture();
		theModel.addAttribute("list", list);	
		return "store-culture";
	}
	
	@RequestMapping("/rcardform")
	public String rcardform(Model theModel)
	{				
		Royalty r=new Royalty();
		theModel.addAttribute("royalty", r);
		return "royalty-card";
	}
	
	@RequestMapping("/rcardform_bean")
	public String rcardformBean(@RequestParam("name")String name, @RequestParam("middle")String middle,@RequestParam("surname")String surname, 
			@RequestParam("contact")String contact,@RequestParam("dob")String dob, @RequestParam("qrcode")String qrcode,@RequestParam("donate")String donate,
			Model themodel,HttpServletRequest httpServletRequest,Model theModel)
	{	
		Royalty r=new Royalty();
		r.setName(name);r.setMiddle(middle);r.setSurname(surname);r.setMobile(contact);r.setDob(dob);r.setQrcode(qrcode);r.setDonate(Integer.parseInt(donate));
		
		if(service.checkRoyaltycard(qrcode))
		{
			theModel.addAttribute("errmsg", "Given Royalty Card is Already Registered");
			theModel.addAttribute("royalty", r);
		}
		else
		{
			service.addRoyalty(r);
			Royalty r1=new Royalty();
			theModel.addAttribute("royalty", r1);
			theModel.addAttribute("msg", "Royalty Card is Registered Successfully!");
		}
			
		return "royalty-card";
	}
	
	@RequestMapping("/updatecard")
	public String updateCard(@RequestParam("qrcode")String qrcode,Model theModel)
	{	
		Royalty r=new Royalty();
		
		if(service.checkRoyaltycard(qrcode))
		{
			service.updateRoyalty(qrcode);
			theModel.addAttribute("msg", "Royalty Card is Updated Successfully!");
			theModel.addAttribute("royalty", r);
		}
		else
		{
			theModel.addAttribute("errmsg", "Given Royalty Card is Not Registered");
			theModel.addAttribute("royalty", r);
		}
		return "royalty-card";
	}
	
	@RequestMapping("/rcardlist")
	public String cardList(Model theModel)
	{	
		
		List<Royalty> list=service.getRoyaltyList();
		theModel.addAttribute("list", list);
			
		return "royalty-list";
	}
	
	
	//Reports Controller
	@RequestMapping("/odatereport")
	public String odatereport(Model theModel)
	{
		theModel.addAttribute("flag", "own");
		return "reportdate";
	}
	
	@RequestMapping("/rdatereport")
	public String rdatereport(Model theModel)
	{
		theModel.addAttribute("flag", "rec");
		return "reportdate";
	}
	
	@RequestMapping("/pastdate")
	public String pastdate(Model theModel)
	{
		theModel.addAttribute("flag", "past");
		return "reportdate";
	}
	
	@RequestMapping("/culturedate")
	public String culturedate(Model theModel)
	{
		theModel.addAttribute("flag", "culture");
		return "reportdate";
	}
	
	@RequestMapping("/negdate")
	public String negdate(Model theModel)
	{
		theModel.addAttribute("flag", "negative");
		return "reportdate";
	}
	
	@RequestMapping("/posdate")
	public String posdate(Model theModel)
	{
		theModel.addAttribute("flag", "positive");
		return "reportdate";
	}
	
	@RequestMapping("/disdate")
	public String disdate(Model theModel)
	{
		theModel.addAttribute("flag", "dispense");
		return "reportdate";
	}
	
	@RequestMapping("/disoutdate")
	public String disoutdate(Model theModel)
	{
		theModel.addAttribute("flag", "dispenseout");
		return "reportdate";
	}
	
	@RequestMapping("/disowndate")
	public String disowndate(Model theModel)
	{
		theModel.addAttribute("flag", "disown");
		return "reportdate";
	}
	
	@RequestMapping("/disroydate")
	public String disroydate(Model theModel)
	{
		theModel.addAttribute("flag", "disroy");
		return "reportdate";
	}
	
	@RequestMapping("/disothdate")
	public String disothdate(Model theModel)
	{
		theModel.addAttribute("flag", "disoth");
		return "reportdate";
	}
	
	@RequestMapping("/reportdatewise")
	public String reportdatewise(@RequestParam("date")String date, @RequestParam("flag")String flag,Model theModel,HttpServletRequest request, HttpServletResponse response) throws ParseException, JRException, NamingException, SQLException, IOException
	{
		//Convert date to specific format (11/11/2019)
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = sdf.parse(date);
		
		String newdate=new SimpleDateFormat("dd/MM/yyyy").format(startdate);
		
		//Select the Report according to flag
		String filename="";
		
		if(flag.equals("own")) {
			filename="OwnMilkReport-Mini";
		}else if(flag.equals("rec"))
		{
			filename="RecepientMilkReport-mini";
		}else if(flag.equals("past"))
		{
			filename="PastReport-Mini";
		}else if(flag.equals("culture"))
		{
			filename="CultureReport-Mini";
		}else if(flag.equals("negative"))
		{
			filename="NegativeReport-Mini";
		}else if(flag.equals("positive"))
		{
			filename="PositiveReport-Mini";
		}else if(flag.equals("dispense"))
		{
			filename="DispenseReport-Mini";
		}else if(flag.equals("dispenseout"))
		{
			filename="DispenseOutsideReport-Mini";
		}else if(flag.equals("disown"))
		{
			filename="Dispenseown-Mini";
		}else if(flag.equals("disroy"))
		{
			filename="Dispenseroyalty-Mini";
		}else if(flag.equals("disoth"))
		{
			filename="Dispenseother-Mini";
		}
		
		//Printing the Jasper Report
		try
		{
		HashMap<String, Object> hm=new HashMap<String, Object>();
		
		hm.put("hospitalname", getHospitalName());
		hm.put("startdate", newdate);
		hm.put("enddate", newdate);
		System.out.println(hm);
		PrintJasperReport.printreport(filename,request,response,hm);
		
		theModel.addAttribute("flag", flag);
		return "reportdate";
		}
		catch(Exception e)
		{
			return "nodata";
		}
	}
	
	@RequestMapping("/omini")
	public String omini(Model theModel)
	{
		theModel.addAttribute("flag", "own");
		return "reportmini";
	}
	
	@RequestMapping("/rmini")
	public String rmini(Model theModel)
	{
		theModel.addAttribute("flag", "rec");
		return "reportmini";
	}
	
	@RequestMapping("/pastmini1")
	public String pastmini(Model theModel)
	{
		theModel.addAttribute("flag", "past");
		return "reportmini";
	}
	
	@RequestMapping("/culmini")
	public String culmini(Model theModel)
	{
		theModel.addAttribute("flag", "culture");
		return "reportmini";
	}
	
	@RequestMapping("/negmini")
	public String negmini(Model theModel)
	{
		theModel.addAttribute("flag", "negative");
		return "reportmini";
	}
	
	@RequestMapping("/posmini")
	public String posmini(Model theModel)
	{
		theModel.addAttribute("flag", "positive");
		return "reportmini";
	}
	
	@RequestMapping("/dismini")
	public String dismini(Model theModel)
	{
		theModel.addAttribute("flag", "dispense");
		return "reportmini";
	}
	
	@RequestMapping("/disoutmini")
	public String disoutmini(Model theModel)
	{
		theModel.addAttribute("flag", "dispenseout");
		return "reportmini";
	}
	
	@RequestMapping("/disownmini")
	public String disownmini(Model theModel)
	{
		theModel.addAttribute("flag", "disown");
		return "reportmini";
	}
	
	@RequestMapping("/disroymini")
	public String disroymini(Model theModel)
	{
		theModel.addAttribute("flag", "disroy");
		return "reportmini";
	}
	
	@RequestMapping("/disothmini")
	public String disothmini(Model theModel)
	{
		theModel.addAttribute("flag", "disoth");
		return "reportmini";
	}
	
	@RequestMapping("/reportminiwise")
	public String reportminiwise(@RequestParam("sdate")String sdate, @RequestParam("edate")String edate,@RequestParam("flag")String flag,Model theModel,HttpServletRequest request, HttpServletResponse response) throws ParseException, JRException, NamingException, SQLException, IOException
	{
		//Convert date to specific format (11/11/2019)
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startdate = sdf.parse(sdate);
		
		String newdate1=new SimpleDateFormat("dd/MM/yyyy").format(startdate);	
		
		Date enddate = sdf.parse(edate);
		
		String newdate2=new SimpleDateFormat("dd/MM/yyyy").format(enddate);
		
		//Select the Report according to flag
		String filename="";
		
		if(flag.equals("own")) {
			filename="OwnMilkReport-Mini";
		}else if(flag.equals("rec"))
		{
			filename="RecepientMilkReport-mini";
		}else if(flag.equals("past"))
		{
			filename="PastReport-Mini";
		}else if(flag.equals("culture"))
		{
			filename="CultureReport-Mini";
		}else if(flag.equals("negative"))
		{
			filename="NegativeReport-Mini";
		}else if(flag.equals("positive"))
		{
			filename="PositiveReport-Mini";
		}else if(flag.equals("dispense"))
		{
			filename="DispenseReport-Mini";
		}else if(flag.equals("dispenseout"))
		{
			filename="DispenseOutsideReport-Mini";
		}else if(flag.equals("disown"))
		{
			filename="Dispenseown-Mini";
		}else if(flag.equals("disroy"))
		{
			filename="Dispenseroyalty-Mini";
		}else if(flag.equals("disoth"))
		{
			filename="Dispenseother-Mini";
		}
		
		//Printing the Jasper Report
		
		try {
		HashMap<String, Object> hm=new HashMap<String, Object>();
		
		hm.put("hospitalname", getHospitalName());
		hm.put("startdate", newdate1);
		hm.put("enddate", newdate2);
		System.out.println(hm);
		PrintJasperReport.printreport(filename,request,response,hm);
		
		theModel.addAttribute("flag", flag);
		return "reportmini";
		}
		catch(Exception e)
		{
			return "nodata";
		}
	}
	
	
	@RequestMapping("/ostmt")
	public String ostmt(Model theModel)
	{
		theModel.addAttribute("flag", "own");
		return "reportall";
	}
	
	@RequestMapping("/rstmt")
	public String rstmt(Model theModel)
	{
		theModel.addAttribute("flag", "rec");
		return "reportall";
	}
	
	@RequestMapping("/paststmt")
	public String paststmt(Model theModel)
	{
		theModel.addAttribute("flag", "past");
		return "reportall";
	}
	
	@RequestMapping("/culstmt")
	public String culstmt(Model theModel)
	{
		theModel.addAttribute("flag", "culture");
		return "reportall";
	}
	
	@RequestMapping("/negstmt")
	public String negstmt(Model theModel)
	{
		theModel.addAttribute("flag", "negative");
		return "reportall";
	}
	
	@RequestMapping("/posstmt")
	public String posstmt(Model theModel)
	{
		theModel.addAttribute("flag", "positive");
		return "reportall";
	}
	
	@RequestMapping("/disstmt1")
	public String disstmt(Model theModel)
	{
		theModel.addAttribute("flag", "dispense");
		return "reportall";
	}
	
	@RequestMapping("/disoutstmt")
	public String disoutstmt(Model theModel)
	{
		theModel.addAttribute("flag", "dispenseout");
		return "reportall";
	}
	
	@RequestMapping("/disownstmt")
	public String disownstmt(Model theModel)
	{
		theModel.addAttribute("flag", "disown");
		return "reportall";
	}
	
	@RequestMapping("/disroystmt")
	public String disroystmt(Model theModel)
	{
		theModel.addAttribute("flag", "disroy");
		return "reportall";
	}
	
	@RequestMapping("/disothstmt")
	public String disothstmt(Model theModel)
	{
		theModel.addAttribute("flag", "disoth");
		return "reportall";
	}
	
	
	@RequestMapping("/reportallwise")
	public String reportallwise(@RequestParam("flag")String flag,Model theModel,HttpServletRequest request, HttpServletResponse response) throws ParseException, JRException, NamingException, SQLException, IOException
	{
		
		//Select the Report according to flag
		String filename="";
		
		if(flag.equals("own")) {
			filename="OwnMilkReport-All";
		}else if(flag.equals("rec"))
		{
			filename="RecepientMilkReport-All";
		}else if(flag.equals("past"))
		{
			filename="PastReport-All";
		}else if(flag.equals("culture"))
		{
			filename="CultureReport-All";
		}else if(flag.equals("negative"))
		{
			filename="NegativeReport-All";
		}else if(flag.equals("positive"))
		{
			filename="PositiveReport-All";
		}else if(flag.equals("dispense"))
		{
			filename="DispenseReport-All";
		}else if(flag.equals("dispenseout"))
		{
			filename="DispenseOutsideReport-All";
		}else if(flag.equals("disown"))
		{
			filename="DispenseownAll";
		}else if(flag.equals("disroy"))
		{
			filename="DispenseroyaltyAll";
		}else if(flag.equals("disoth"))
		{
			filename="DispenseotherAll";
		}
		
		//Printing the Jasper Report
		try
		{
		HashMap<String, Object> hm=new HashMap<String, Object>();
		
		hm.put("hospitalname", getHospitalName());		
		System.out.println(hm);
		PrintJasperReport.printreport(filename,request,response,hm);
		
		theModel.addAttribute("flag", flag);
		return "reportmini";
		}
		catch(Exception e)
		{
			return "nodata";
		}
	}
	
	@RequestMapping("/royaltyreport")
	public String royaltyreport(HttpServletRequest request, HttpServletResponse response) throws ParseException, JRException, NamingException, SQLException, IOException
	{
		
		//Select the Report according to flag
		String filename="Royaltylist";		
		
		//Printing the Jasper Report
		try
		{
		HashMap<String, Object> hm=new HashMap<String, Object>();
		
		hm.put("hospitalname", getHospitalName());		
		System.out.println(hm);
		PrintJasperReport.printreport(filename,request,response,hm);		
		
		return "reportmini";
		}
		catch(Exception e)
		{
			return "nodata";
		}
	}
	
	@RequestMapping("/nodata")
	public String nodata()
	{		
		return "nodata";		
	}	
	
	@RequestMapping("/owntorec")
	public String owntorec()
	{		
		return "owntorec";		
	}
	
	@PostMapping("/owntorec-form-bean")
	public String owntorecbean(@RequestParam("qr")String qr,Model theModel,HttpServletRequest httpServletRequest, HttpSession session)
	{		
		
		String batch=service.lastBatch();
		
		//check if batch is already pasturize
		if(!service.checkBatch(batch)) {
			int b=Integer.parseInt(batch);
			b++;
			batch=""+b;
		}
		
		if(service.convertowntorec(batch,qr))
		{
			theModel.addAttribute("msg","Conversion is successfully!..");
		}
		else
		{
			theModel.addAttribute("errmsg","Sorry, Conversion is Failed!..");
		}		
		
		return "owntorec";		
	}
	
	@PostMapping("/searchroyalty")
	public String searchroyalty(@RequestParam("search")String search,Model theModel,HttpServletRequest httpServletRequest, HttpSession session)
	{
		
		//Get Royalty Card Information
		Royalty royalty=service.getRoyalty(search);
		List<Dispense> list=new ArrayList<Dispense>();
		if(royalty==null)
		{
			theModel.addAttribute("errmsg", "Royalty Card is not registered!");
		}
		else
		{
			
			System.out.println(royalty);
			//Get List of Dispense using royalty card
			
			
			list=service.getRoyaltyDispense(search);
			if(list.size()==0)
			{
				theModel.addAttribute("dismsg", "No Dispense From this Royalty Card");
			}
			else
			{
				System.out.println("size:"+list.size());
				
			}
		}
		theModel.addAttribute("royalty", royalty);
		theModel.addAttribute("list", list);
		theModel.addAttribute("total", list.size());
		return "royaltyresult";		
	}
	
	@RequestMapping("/storerecipientavialable")
	public String storerecipientavialable(Model theModel)
	{	
		List<String> qr=new ArrayList<String>();
		qr=service.getApproveQR();
		
		List<Donor> list=new ArrayList<Donor>();
		list=service.getDonorList(qr);
		
		List<DispenseDonor> dislist= service.getDispenseDonor();
		for(DispenseDonor dd:dislist)
		{
			String qr1=dd.getQr();
			
			for(int i=0;i<list.size();i++)
			{
				Donor d=list.get(i);
				if(d.getQrcode().equals(qr1)) {
					list.remove(i);
					System.out.println("deleted");
				}
			}
			
		}
		
		theModel.addAttribute("list", list);
		
		return "store-available";		
	}
	
	@RequestMapping("/storerecipientdiscard")
	public String storerecipientdiscard(Model theModel)
	{		

		List<String> qr=new ArrayList<String>();
		qr=service.getDiscardQR();
		
		List<Donor> list=new ArrayList<Donor>();
		list=service.getDonorList(qr);
		
		theModel.addAttribute("list", list);
		
		return "store-discard";		
	}
	
	@RequestMapping("/storerecipientdispense")
	public String storerecipientdispense(Model theModel)
	{	
		List<DispenseDonor> list=new ArrayList<DispenseDonor>();
		list=service.getDispenseDonor();
		for(DispenseDonor d:list)
		{
			System.out.println("List: "+d);
		}
			
		theModel.addAttribute("list", list);		
		return "store-dispense";		
	}
	
	@RequestMapping("/storeowndis")
	public String storeowndis(Model theModel)
	{	
		List<DispenseDonor> list=new ArrayList<DispenseDonor>();
		list=service.getDispenseOwn();
		for(DispenseDonor d:list)
		{
			System.out.println("List: "+d);
		}
			
		theModel.addAttribute("list", list);			
		return "store-owndispense";		
	}
	
	@RequestMapping("/storeownavilable")
	public String storeownavilable(Model theModel)
	{	
		List<Donor> list=new ArrayList<Donor>();
		list=service.getAvailableOwn();
				
		theModel.addAttribute("list", list);
		return "store-ownavailable";		
	}
}
