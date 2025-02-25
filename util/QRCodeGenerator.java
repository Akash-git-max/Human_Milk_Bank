package com.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {
	
	private String filePath,fileType,qrtext;
	int size;
	File qrFile;
	
	BufferedImage result;
	
	public QRCodeGenerator() throws WriterException, IOException  
	{	
		qrtext=UniqueKeyGenerator.generateUniqueKey();		
		//filePath="C://qrcode/"+qrtext+".png";
		//fileType="png";
		size=135;		
		//qrFile = new File(filePath);
		result=createQRImage(qrtext, size);
	}

	private BufferedImage createQRImage(String qrCodeText, int size) throws WriterException, IOException 
	{
		// Create the ByteMatrix for the QR-Code that encodes the given String
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		//ImageIO.write(image, fileType, qrFile);
		return image;
	}
	
	//Return Absolute path of QR Code png File
	public String getAbsolutePath()
	{
		System.out.println("Height: "+result.getHeight()+" Width: "+result.getWidth());
		return filePath;
	}
	
	//Return Unique Number of QR Code
	public String getUniqueCode()
	{
		return qrtext;
	}
	
	public BufferedImage getBufferdImage()
	{		
		return result;
	}
	
	public static void main(String[] args) throws WriterException, IOException {
		
		QRCodeGenerator generator=new QRCodeGenerator();
		System.out.println(generator.getAbsolutePath());
		System.out.println(generator.getUniqueCode());
		
		System.out.println("DONE");
	}

}
