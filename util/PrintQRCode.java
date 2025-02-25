package com.util;

import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.PrintService;

public class PrintQRCode implements Printable {

	BufferedImage bi;
	String arr;
	
	public PrintQRCode(BufferedImage bi,String arr) {
    	this.bi=bi;
    	this.arr=arr;
    	PrinterJob job;
		try {
			job = findPrinterJob();
			initializePrinter(job);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

   private void initializePrinter(PrinterJob job) {
	   
       job.setPrintable(this);
       boolean ok = job.printDialog();
       if (ok) {
           try {
                job.print();
           } catch (PrinterException ex) {
            /* The job did not successfully complete */
           }
       }
   }

	@Override
	public int print(java.awt.Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		 
		if (pageIndex == 0) {            
            graphics.drawImage(bi, 0, 0, 100, 90, null);
			//graphics.drawString(arr, 5, 92);            
            return PAGE_EXISTS;
        } else {
            return NO_SUCH_PAGE;
        }
	}
	
	
	public static PrintService findPrintService() {

	    PrintService service = null;

	    // Get array of all print services - sort order NOT GUARANTEED!
	    PrintService[] services = PrinterJob.lookupPrintServices();

	    // Retrieve specified print service from the array
	    for (int index = 0; service == null && index < services.length; index++)
	    {	
	            service = services[index];
	    }

	    // Return the print service
	    return service;
	}
	
	
	public static PrinterJob findPrinterJob() throws Exception {

	    // Retrieve the Printer Service
	    PrintService printService = findPrintService();

	    // Validate the Printer Service
	    if (printService == null) {

	        throw new IllegalStateException("Unrecognized Printer Service \"" );
	    }

	    // Obtain a Printer Job instance.
	    PrinterJob printerJob = PrinterJob.getPrinterJob();

	    // Set the Print Service.
	    printerJob.setPrintService(printService);

	    // Return Print Job
	    return printerJob;
	}
	
	
	/*
	public static void main(String args[]) throws IOException 
	 {
		final File dir = new File("C://qrcode/1568803958434.png");
		BufferedImage bi;
		bi=ImageIO.read(dir);
	    new PrintQRCode(bi,"1568803958434");
	 }
	 */
}
