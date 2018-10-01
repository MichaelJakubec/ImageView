package net.jakubec.view.edit.plain;

import java.awt.Graphics;

public abstract class BasicLayer {
	
	private int xPos;
	
	private int yPos;
	
	private String layerName;
	
	private static int count; 
	
	public BasicLayer(int xPos, int yPos){
		this.xPos= xPos;
		this.yPos= yPos;
		
		layerName="Layer"+String.valueOf(count++);
	}
	
	public void moveImage(int divX, int divY) {
		xPos += divX;
		yPos += divY;	
	}
	
	public final int getX(){
		return xPos;
	}
	
	public final int getY(){
		return yPos;
	}
	
	public abstract void draw(Graphics g);
	
	public String getName(){
		return layerName;
	}

}
