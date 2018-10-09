package net.jakubec.view;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This interface has to be implemented of every panel of the program which
 * should be display in the program. This class is interface is not used for the
 * {@link net.jakubec.view.plugin.ViewPlugin}.
 * 
 * @author amunra
 * @since Version 0.1
 * 
 */
public interface BasicPanel {

	/**
	 * set the zoomlevel to 100 percent
	 */
	void fullImage();

	/**
	 * opens the Image spezified by this File
	 * 
	 * @param selected the file which should be displayed
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
	 * informs the panel to undo the last action
	 */
	void undo();

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
