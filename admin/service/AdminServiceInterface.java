package com.admin.service;

import java.awt.image.BufferedImage;
import java.util.List;

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

public interface AdminServiceInterface {

	public List<Admin> findUser(String username, String password);	
	
	public boolean changePass(String id,String username,String password);
	
	public SearchEntity searchResult(String qrcode);
	
	public void saveDonor(Donor donor);
	
	public String lastBatch();
	
	public List<PastData> pastData();
	
	public boolean checkBatch(String batch);
	
	public void savePast(Past past);

	public boolean checkPastBatch(String batch);

	public void saveCulture(Culture cul);

	public List<String> getRDonorToday();

	public List<Stmt> getRDonorMini();

	public List<Stmt> getRDonorDetails(String sdate, String edate);

	public List<PastReport> getPastToday();

	public List<PastReport> getPastMini();

	public List<PastReport> getPastDetails(String ee, String ss);

	public List<Culture> getCultureToday();

	public List<Culture> getCultureMini();

	public List<Culture> getCultureDetails(String ee, String ss);

	public boolean checkCultureBatch(String batch);

	public void saveDiscard(Discard dis);

	public List<Discard> getDiscardToday();

	public List<Discard> getDiscardMini();

	public List<Discard> getDiscardDetails(String ee, String ss);

	public List<String> getODonorToday();

	public List<Stmt> getODonorMini();

	public List<Stmt> getODonorDetails(String ee, String ss);

	public boolean checkDonorQR(String qr);

	public boolean checkDispenseQR(String qr);

	public void saveDispence(Dispense dis);

	public List<QRCodeMilk> getQRCodeMilk();

	public List<Dispense> getDispenseToday();

	public List<Stmt> getDispenseMini();

	public List<Stmt> getDispenseDetails(String ee, String ss);

	public int getTotalRecipient();

	public int getTotalOwn();

	public int getTotalPast();

	public int getTotalCulture();

	public List<Donor> getRecipientDonor();

	public List<Donor> getOwnDonor();

	public List<Past> getPast();

	public List<Culture> getCulture();

	public String getTotalMilkCollection();

	public String getTotalMilkDispense();

	public void setQRCodeImage(BufferedImage image);

	public BufferedImage getQRCodeImage();

	public boolean checkRoyaltycard(String qrcode);

	public void addRoyalty(Royalty r);

	public void updateRoyalty(String qrcode);

	public List<Royalty> getRoyaltyList();

	public List<Security> getSecurity();

	public boolean changeSecurity(String security, String answer, String newsecurity, String newanswer);

	public boolean checkSecurity(String security, String answer);

	public boolean checkQRCode(String qrcode);

	public boolean checkSample(String qr);

	public boolean convertowntorec(String batch, String qr);

	public Royalty getRoyalty(String search);

	public List<Dispense> getRoyaltyDispense(String search);

	public String getOwnDispense();

	public String getRecDispense();

	public String getAvailableStock();

	public String getDiscared();

	public List<DispenseDonor> getDispenseDonor();

	public List<String> getApproveQR();

	public List<Donor> getDonorList(List<String> qr);

	public List<String> getDiscardQR();

	public List<DispenseDonor> getDispenseOwn();

	public List<Donor> getAvailableOwn();
	
	
}
