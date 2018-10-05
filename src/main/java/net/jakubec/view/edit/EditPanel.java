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

	public EditPanel() {
		super();
		VToolBox toolBox = new VToolBox(this);
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(final KeyEvent e) {
			//TODO compile error	activeFrame.keyPressed(e);

			}

			@Override
			public void keyReleased(final KeyEvent e) {
				// TODO compile error activeFrame.keyReleased(e);
			}

			@Override
			public void keyTyped(final KeyEvent e) {
				// TODO compile error activeFrame.keyTyped(e);

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

	// public void dohelp() {
	//	layerManager.help();
	// }

	@Override
	public void fullImage() {
		getAllFrames();

	}

	EditMode getEditMode() {
		return mode;
	}

	LayerFrame getLayerManager() {
		return layerManager;
	}

	@Override
	public Container getPanel() {
		return this;
	}

	@Override
	public void openImage(final File image) {

		Application.getMainWindow().setTitle(VSettings.PROG_NAME);
		try {
			BufferedImage origin = ImageIO.read(image);
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
