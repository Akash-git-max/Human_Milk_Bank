package com.admin.service;



import java.awt.image.BufferedImage;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.admin.dao.AdminDAOInterface;
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


@Service
public class AdminService implements AdminServiceInterface {
	
	private BufferedImage bi;
	
	@Autowired
	private AdminDAOInterface adminDAO;
	
	@Override	
	@Transactional
	public List<Admin> findUser(String username, String password) {
        return adminDAO.checkAdmin(username, password);
    }

	@Override
	@Transactional
	public boolean changePass(String id, String username, String password) {		
		return adminDAO.changePass(id, username, password);
	}

	@Override
	@Transactional
	public SearchEntity searchResult(String qrcode) {		
		return adminDAO.searchResult(qrcode);
	}

	@Override
	@Transactional
	public void saveDonor(Donor donor) {		
		adminDAO.saveDonor(donor);
	}

	@Override
	@Transactional
	public String lastBatch() {		
		return adminDAO.lastBatch();
	}

	@Override
	@Transactional
	public List<PastData> pastData() {		
		return adminDAO.pastData();
	}
	
	@Override
	@Transactional
	public boolean checkBatch(String batch)
	{
		return adminDAO.checkBatch(batch);
	}

	@Override
	@Transactional
	public void savePast(Past past) {
		
		adminDAO.savePast(past);
	}

	@Override
	@Transactional
	public boolean checkPastBatch(String batch) {
		
		return adminDAO.checkPastBatch(batch);
	}

	@Override
	@Transactional
	public void saveCulture(Culture cul) {		
		adminDAO.saveCulture(cul);
	}

	@Override
	@Transactional
	public List<String> getRDonorToday() {		
		return adminDAO.getRDonorToday();
	}

	@Override
	@Transactional
	public List<Stmt> getRDonorMini() {		
		return adminDAO.getRDonorMini();
	}

	@Override
	@Transactional
	public List<Stmt> getRDonorDetails(String sdate, String edate) {
		// TODO Auto-generated method stub
		return adminDAO.getRDonorDetails( sdate, edate);
	}

	@Override
	@Transactional
	public List<PastReport> getPastToday() {		
		return adminDAO.getPastToday();
	}

	@Override
	@Transactional
	public List<PastReport> getPastMini() {		
		return adminDAO.getPastMini();
	}

	@Override
	@Transactional
	public List<PastReport> getPastDetails(String ee, String ss) {
		// TODO Auto-generated method stub
		return adminDAO.getPastDetails(ee, ss);
	}

	@Override
	@Transactional
	public List<Culture> getCultureToday() {
		// TODO Auto-generated method stub
		return adminDAO.getCultureToday();
	}

	@Override
	@Transactional
	public List<Culture> getCultureMini() {
		// TODO Auto-generated method stub
		return adminDAO.getCultureMini();
	}

	@Override
	@Transactional
	public List<Culture> getCultureDetails(String ee, String ss) {
		// TODO Auto-generated method stub
		return adminDAO.getCultureDetails(ee, ss);
	}

	@Override
	@Transactional
	public boolean checkCultureBatch(String batch) {
		// TODO Auto-generated method stub
		return adminDAO.checkCultureBatch(batch);
	}

	@Override
	@Transactional
	public void saveDiscard(Discard dis) {
		// TODO Auto-generated method stub
		adminDAO.saveDiscard(dis);
	}

	@Override
	@Transactional
	public List<Discard> getDiscardToday() {
		// TODO Auto-generated method stub
		return adminDAO.getDiscardToday();
	}

	@Override
	@Transactional
	public List<Discard> getDiscardMini() {
		// TODO Auto-generated method stub
		return adminDAO.getDiscardMini();
	}

	@Override
	@Transactional
	public List<Discard> getDiscardDetails(String ee, String ss) {
		// TODO Auto-generated method stub
		return adminDAO.getDiscardDetails(ee, ss);
	}

	@Override
	@Transactional
	public List<String> getODonorToday() {
		// TODO Auto-generated method stub
		return adminDAO.getODonorToday();
	}

	@Override
	@Transactional
	public List<Stmt> getODonorMini() {
		// TODO Auto-generated method stub
		return adminDAO.getODonorMini();
	}

	@Override
	@Transactional
	public List<Stmt> getODonorDetails(String ee, String ss) {
		// TODO Auto-generated method stub
		return adminDAO.getODonorDetails(ee, ss);
	}

	@Override
	@Transactional
	public boolean checkDonorQR(String qr) {
		// TODO Auto-generated method stub
		return adminDAO.checkDonorQR(qr);
	}

	@Override
	@Transactional
	public boolean checkDispenseQR(String qr) {
		// TODO Auto-generated method stub
		return adminDAO.checkDispenseQR(qr);
	}

	@Override
	@Transactional
	public void saveDispence(Dispense dis) {
		// TODO Auto-generated method stub
		adminDAO.saveDispence(dis);
	}

	@Override
	@Transactional
	public List<QRCodeMilk> getQRCodeMilk() {
		// TODO Auto-generated method stub
		return adminDAO.getQRCodeMilk();
	}

	@Override
	@Transactional
	public List<Dispense> getDispenseToday() {
		// TODO Auto-generated method stub
		return adminDAO.getDispenseToday();
	}

	@Override
	@Transactional
	public List<Stmt> getDispenseMini() {
		// TODO Auto-generated method stub
		return adminDAO.getDispenseMini();
	}

	@Override
	@Transactional
	public List<Stmt> getDispenseDetails(String ee, String ss) {
		// TODO Auto-generated method stub
		return adminDAO.getDispenseDetails(ee, ss);
	}

	@Override
	@Transactional
	public int getTotalRecipient() {
		// TODO Auto-generated method stub
		return adminDAO.getTotalRecipient() ;
	}

	@Override
	@Transactional
	public int getTotalOwn() {
		// TODO Auto-generated method stub
		return adminDAO.getTotalOwn();
	}

	@Override
	@Transactional
	public int getTotalPast() {
		// TODO Auto-generated method stub
		return adminDAO.getTotalPast();
	}

	@Override
	@Transactional
	public int getTotalCulture() {
		// TODO Auto-generated method stub
		return adminDAO.getTotalCulture();
	}

	@Override
	@Transactional
	public List<Donor> getRecipientDonor() {
		// TODO Auto-generated method stub
		return adminDAO.getRecipientDonor();
	}

	@Override
	@Transactional
	public List<Donor> getOwnDonor() {
		// TODO Auto-generated method stub
		return adminDAO.getOwnDonor();
	}

	@Override
	@Transactional
	public List<Past> getPast() {
		// TODO Auto-generated method stub
		return adminDAO.getPast();
	}

	@Override
	@Transactional
	public List<Culture> getCulture() {
		// TODO Auto-generated method stub
		return adminDAO.getCulture() ;
	}

	@Override
	@Transactional
	public String getTotalMilkCollection() {
		// TODO Auto-generated method stub
		return  adminDAO.getTotalMilkCollection();
	}

	@Override
	@Transactional
	public String getTotalMilkDispense() {
		// TODO Auto-generated method stub
		return  adminDAO.getTotalMilkDispense();
	}
	
	//for getting BufferedImage for Printing
	@Override
	public BufferedImage getQRCodeImage()
	{
		return bi;
	}
	
	@Override
	public void setQRCodeImage(BufferedImage bi)
	{
		this.bi=bi;
	}

	@Override
	@Transactional
	public boolean checkRoyaltycard(String qrcode) {
		// TODO Auto-generated method stub
		return adminDAO.checkRoyaltycard(qrcode);
	}

	@Override
	@Transactional
	public void addRoyalty(Royalty r) {
		
		adminDAO.addRoyalty(r);
	}

	@Override
	@Transactional
	public void updateRoyalty(String qrcode) {
		// TODO Auto-generated method stub
		adminDAO.updateRoyalty(qrcode);
	}

	@Override
	@Transactional
	public List<Royalty> getRoyaltyList() {
		// TODO Auto-generated method stub
		return adminDAO.getRoyaltyList();
	}

	@Override
	@Transactional
	public List<Security> getSecurity() {
		// TODO Auto-generated method stub
		return adminDAO.getSecurity();
	}

	@Override
	@Transactional
	public boolean changeSecurity(String security, String answer, String newsecurity, String newanswer) {
		// TODO Auto-generated method stub
		return adminDAO.changeSecurity(security, answer, newsecurity, newanswer);
	}

	@Override
	@Transactional
	public boolean checkSecurity(String security, String answer) {
		// TODO Auto-generated method stub
		return adminDAO.checkSecurity(security, answer);
	}

	@Override
	@Transactional
	public boolean checkQRCode(String qrcode) {
		// TODO Auto-generated method stub
		return adminDAO.checkQRCode(qrcode);
	}

	@Override
	@Transactional
	public boolean checkSample(String qr) {
		// TODO Auto-generated method stub
		return adminDAO.checkSample(qr);
	}

	@Override
	@Transactional
	public boolean convertowntorec(String batch, String qr) {
		// TODO Auto-generated method stub
		return adminDAO.convertowntorec(batch, qr);
	}

	@Override
	@Transactional
	public Royalty getRoyalty(String search) {
		// TODO Auto-generated method stub
		return adminDAO.getRoyalty(search);
	}

	@Override
	@Transactional
	public List<Dispense> getRoyaltyDispense(String search) {
		// TODO Auto-generated method stub
		return adminDAO.getRoyaltyDispense(search);
	}

	@Override
	@Transactional
	public String getOwnDispense() {
		// TODO Auto-generated method stub
		return adminDAO.getOwnDispense();
	}

	@Override
	@Transactional
	public String getRecDispense() {
		// TODO Auto-generated method stub
		return adminDAO.getRecDispense();
	}

	@Override
	@Transactional
	public String getAvailableStock() {
		// TODO Auto-generated method stub
		return adminDAO.getAvailableStock();
	}

	@Override
	@Transactional
	public String getDiscared() {
		// TODO Auto-generated method stub
		return adminDAO.getDiscared();
	}

	@Override
	@Transactional
	public List<DispenseDonor> getDispenseDonor() {
		// TODO Auto-generated method stub
		return adminDAO.getDispenseDonor();
	}

	@Override
	@Transactional
	public List<String> getApproveQR() {
		// TODO Auto-generated method stub
		return adminDAO.getApproveQR();
	}

	@Override
	@Transactional
	public List<Donor> getDonorList(List<String> qr) {
		// TODO Auto-generated method stub
		return adminDAO.getDonorList(qr);
	}

	@Override
	@Transactional
	public List<String> getDiscardQR() {
		// TODO Auto-generated method stub
		return adminDAO.getDiscardQR();
	}

	@Override
	@Transactional
	public List<DispenseDonor> getDispenseOwn() {
		// TODO Auto-generated method stub
		return adminDAO.getDispenseOwn();
	}

	@Override
	@Transactional
	public List<Donor> getAvailableOwn() {
		// TODO Auto-generated method stub
		return adminDAO.getAvailableOwn();
	}

	
}
