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

package net.jakubec.view.edit.plain;

import java.awt.Graphics;

/**
 * Base class of all layers in the edit info
 */
public abstract class BasicLayer {
	
	private int xPos;
	
	private int yPos;
	
	private String layerName;
	
	private static int count;

	/**
	 * creates a new layer at the given position
	 * @param xPos x kooordinate of this layer
	 * @param yPos y koordinate of this layer
	 */
	public BasicLayer(int xPos, int yPos){
		this.xPos= xPos;
		this.yPos= yPos;
		
		layerName="Layer"+String.valueOf(count++);
	}

	/**
	 * moves the layer relatively to the current position
	 * @param divX move at x coordinate
	 * @param divY move by y coordinate
	 */
	public void moveImage(int divX, int divY) {
		xPos += divX;
		yPos += divY;	
	}

	/**
	 * Returns the X coordinate of this layer
	 * @return x coordinate
	 */
	public final int getX(){
		return xPos;
	}

	/**
	 * Returns the Y coordinate of this layer
	 * @return the y coordinate
	 */
	public final int getY(){
		return yPos;
	}

	/**
	 * draws the content of the layer with the given Graphics object
	 * @param g the graphics where the image should be drawn
	 */
	public abstract void draw(Graphics g);

	/**
	 * returns the name of this layer
	 * @return name of this layer
	 */
	public String getName(){
		return layerName;
	}

}
