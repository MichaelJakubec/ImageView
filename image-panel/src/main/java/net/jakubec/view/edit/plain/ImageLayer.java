package net.jakubec.view.edit.plain;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;



public class ImageLayer extends BasicLayer {
	
	BufferedImage image;
	public ImageLayer(BufferedImage img){
		this (img,0,0);
	}
	/**
	 * creates a new ImagePlain with the given image and the given startPosition;
	 * @param img
	 * @param x the startPosition of the Image
	 * @param y the startPosition in y-Direction of the image
	 */
	public ImageLayer(BufferedImage img, int x, int y){
		super(x,y);
		WritableRaster raster = img.copyData( null );
		image = new BufferedImage( img.getColorModel(), raster, img.isAlphaPremultiplied(), null );
		
	}
	
	public ImageLayer(int width, int height){
		super(0,0);
		image= new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		
	}

	public void draw(Graphics g) {
		g.drawImage(image, getX(),getY(),null);
		
	}
	
}
