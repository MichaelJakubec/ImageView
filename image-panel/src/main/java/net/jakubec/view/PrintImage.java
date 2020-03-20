/*
 * Copyright 2018 Michael Jakubec
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jakubec.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterResolution;



public class PrintImage implements Printable {
	private BufferedImage img;
	private static final int POINTS_PER_INCH=300;
	public PrintImage(){
		
	}
	
	public PrintImage(BufferedImage img, String fileName){
		this.img = img;
		try{
			PrinterJob pjob = PrinterJob.getPrinterJob();
			
		
			
			if (pjob.printDialog() == false)
				return;
			
			
//			int res =  pjob.getPageResolution();
//		      Dimension d = pjob.getPageDimension();
//		     System.out.println( "Resolution : " + res + "\n" +
//		                         "Width : " + d.width + "\n" +
//		                         "Height : " + d.height + "\n" +
//		                         "Pixel on page : " + (res * d.width * d.height) );

			pjob.setPrintable(this);
			pjob.setJobName("ImageView "+ fileName);
			PrinterResolution res= new PrinterResolution(3000,3000,ResolutionSyntax.DPI);
			PrintRequestAttributeSet as = new HashPrintRequestAttributeSet();

		    as.add(MediaSizeName.ISO_A4);
		    as.add(OrientationRequested.PORTRAIT);
		    as.add(Chromaticity.MONOCHROME);
			as.add(res);
			as.add(PrintQuality.HIGH);

			pjob.print(as);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex){
		int resolutionMultiplier=4;
		
		if (pageIndex > 0) return NO_SUCH_PAGE;
		
		
		
		try {
			Graphics2D g2d = (Graphics2D) graphics;
			Toolkit tk = Toolkit.getDefaultToolkit();
			final Dimension orig= new Dimension(img.getWidth(),img.getHeight());
			Dimension boundingBox = new Dimension((int) (pageFormat.getImageableWidth()), (int) pageFormat.getImageableHeight());
			
				g2d.drawImage(img, 0, 0,null);
				
	        
			
		} catch (Exception e) {
			return NO_SUCH_PAGE;
		}
		return PAGE_EXISTS;
	}
}
