package net.jakubec.view.edit;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;

import net.jakubec.view.Application;
import net.jakubec.view.BasicPanel;
import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.edit.tool.VToolBox;
import net.jakubec.view.exception.VExceptionHandler;

public class EditPanel extends JDesktopPane implements BasicPanel {

	public enum EditMode {
		LINE, MOVE, NONE
	}

	public static Color background = Color.white;

	public static Color foreground = Color.black;

	private ImageFrame activeFrame;

	private final LayerFrame layerManager;

	public EditMode mode;

	private BufferedImage origin;

	public EditPanel() {
		super();
		VToolBox toolBox = new VToolBox(this);
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(final KeyEvent e) {
				activeFrame.keyPressed(e);

			}

			@Override
			public void keyReleased(final KeyEvent e) {
				activeFrame.keyReleased(e);
			}

			@Override
			public void keyTyped(final KeyEvent e) {
				activeFrame.keyTyped(e);

			}

		});
		this.add(toolBox, 0);
		layerManager = new LayerFrame();
		this.add(layerManager);
		mode = EditMode.NONE;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	public void dohelp() {
		layerManager.help();
	}

	@Override
	public void fullImage() {
		getAllFrames();

	}

	public EditMode getEditMode() {
		return mode;
	}

	public LayerFrame getLayerManager() {
		return layerManager;
	}

	@Override
	public Container getPanel() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void openImage(final File image) {
		// VSettings.saveStringSetting("current.dir", image.getParent());
		// VSettings.saveStringSetting("current.image", image.getName());
		String[] temp = image.getName().split("\\.");
		// VSettings.saveStringSetting("current.filetype", temp[temp.length -
		// 1]);

		Application.getMainWindow().setTitle(VSettings.PROG_NAME);
		try {
			origin = ImageIO.read(image);
			setImage(origin);
		} catch (Exception e) {
			VExceptionHandler.raiseException(e, "The file couldn't be opened");

		}

	}

	@Override
	public void printImage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateImage(final boolean clockwise) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveAs() {
		// TODO Auto-generated method stub

	}

	public void setEditMode(final EditMode m) {
		mode = m;
	}

	@Override
	public void setImage(final BufferedImage img) {
		this.setImage(img, null);
	}

	public void setImage(final BufferedImage img, final String name) {
		activeFrame = new ImageFrame(this, img);
		this.add(activeFrame, 1);

	}

	@Override
	public void undo() {
		activeFrame.undo();

	}

	@Override
	public void zoom0() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoom1() {
		activeFrame.zoom1();

	}

	@Override
	public void zoomm() {
		activeFrame.zoomm();
	}

	@Override
	public void zoomp() {
		activeFrame.zoomp();

	}

}
