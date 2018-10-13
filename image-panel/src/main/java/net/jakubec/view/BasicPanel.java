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

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This interface has to be implemented of every panel of the program which
 * should be display in the program.
 * 
 * @author amunra
 * @since Version 0.1
 * 
 */
public interface BasicPanel {

	/**
	 * Set the zoomlevel to 100 percent
	 */
	void fullImage();

	/**
	 * opens the Image specified by this File
	 *
	 * @param selected the file which should be displayed
	 * @throws ViewException if the specified file could not be opened
	 */
	void openImage(File selected) throws ViewException;

	/**
	 * prints the current Image
	 */
	void printImage();

	/**
	 * rotates the image clockwise if the parameter is true else counter
	 * clockwise
	 * 
	 * @param clockwise
	 *            specifies if the image should be rotated clockwise or not
	 */
	void rotateImage(boolean clockwise);

	/**
	 * sets the current image that should be displayed.
	 * 
	 * @param img the image which should be displayed
	 */
	void setImage(BufferedImage img);


	/**
	 * sets the zoomlevel to a value so that the image can be shown full. If the
	 * image size is smaller than the paint able area the image is drawn
	 * normally. If the image size is greater than the paint able area the image
	 * is drawn as small as needed to fit in the panel
	 */
	void zoom0();

	/**
	 * sets the zoom level to 100 perzent
	 */
	void zoom1();

	/**
	 * zoom out
	 */
	void zoomm();

	/**
	 * zoom in
	 */
	void zoomp();

}
