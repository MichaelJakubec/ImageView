package net.jakubec.view.print;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSize;

public class PrintImage implements Printable {
	Image image;

	public PrintImage(final Image img) {
		image = img;

		PrinterJob pj = PrinterJob.getPrinterJob();
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(MediaSize.ISO.A4);

		PageFormat pf = pj.pageDialog(aset);
		pj.setPrintable(this, pf);
		boolean ok = pj.printDialog(aset);
	}

	@Override
	public int print(final Graphics g, final PageFormat pf, final int page) throws PrinterException {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		if (page > 0) return NO_SUCH_PAGE;

		g2d.translate(pf.getImageableX(), pf.getImageableY());
		return PAGE_EXISTS;
	}
}
