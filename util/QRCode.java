package com.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.WriterException;




public class QRCode {
	
	private String filepath,qrcode;
	BufferedImage bi,result;
	
	public QRCode() throws WriterException, IOException {
		
		//Generate QR Code
		bi=null;
		
		QRCodeGenerator generator=new QRCodeGenerator();
		
		filepath=generator.getAbsolutePath();
		qrcode=generator.getUniqueCode();
		
		//File outputfile = new File(filepath);
		
		bi = generator.getBufferdImage();
		
		result = drawTextOnImage(qrcode, bi, 15);
		
		//Storing BufferedImage to filelocation				
		//ImageIO.write(result, "png", outputfile);
	}
	
	//Write on Image
	private BufferedImage drawTextOnImage(String text, BufferedImage image, int space) {
        BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight() + space, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));

        g2d.drawImage(image, 0, 0, null);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Calibri", Font.BOLD, 16));
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);

        //center text at bottom of image in the new space
        g2d.drawString(text, (bi.getWidth() / 2) - textWidth / 2, bi.getHeight());

        g2d.dispose();
        return bi;
    }
	
	public String absolutePath() {
		return filepath;
	}	
	
	public String getQRCodeNumber() {
		return qrcode;
	}
	
	public BufferedImage getImage() {
		return result;
	}
	
	public static void main(String[] args) throws WriterException, IOException {
		
		QRCode code=new QRCode();
		System.out.println(code.absolutePath());
		System.out.println(code.getQRCodeNumber());
		System.out.println("DONE");
	}
	

}
