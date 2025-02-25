package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpiredCalculator {	
	
	public static boolean isValid(String from)
	{
		int diff=0;
	    try {
	    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");  
		    String to =formatter.format(new Date());  		    
		    Date today=formatter.parse(to);
			Date dateAfter = formatter.parse(from);
			long difference = today.getTime() - dateAfter.getTime();
			diff = (int) (difference / (1000*60*60*24));
			System.out.println(""+diff);
			
		} 
	    catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		if(diff>90)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static void main(String a[]){	
		
		System.out.println(""+ExpiredCalculator.isValid("21/06/2019"));
	}

}
