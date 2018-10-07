package net.jakubec.view;

import net.jakubec.view.Settings.Settings;
import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.dia.Diapresentation;
import net.jakubec.view.exception.VExceptionHandler;
import net.jakubec.view.filedrop.FileDrop;
import net.jakubec.view.listener.MenuListener;
import net.jakubec.view.menu.MenuFactory;
import net.jakubec.view.properties.VProperties;
import net.jakubec.view.save.ImageSaver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewPanel extends JPanel implements BasicPanel, AdjustmentListener, MouseListener,
		MouseMotionListener {

	private class ImagePainter extends JPanel {

		@Override
		public void paintComponent(final Graphics g) {
			if (img.getWidth() < 2) {
				drawImage(origin);
			}
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(img, ((getWidth() - img.getWidth()) / 2),
					((getHeight() - img.getHeight()) / 2), null);
		}
	}

	/**
	 * Container for the
	 */
	private final Container cp;
	/**
	 * The factor of the image which is scaled
	 */
	private double factor;
	/**
	 * Horizontal ScrollBar
	 */
	private final JScrollBar hBar;
	/**
	 * The old MouseEvent
	 */
	private MouseEvent oldEvent;
	/**
	 * The shown image
	 */
	private BufferedImage img = new BufferedImage(1, 1, 1);
	/**
	 * Class for image painting
	 */
	private final ImagePainter imp;
	/**
	 * The original image
	 */
	private BufferedImage origin = null;
	/**
	 * The height of the Screen
	 */
	private int sHeight;
	/**
	 * The width of the Screen
	 */
	private int sWidth;
	/**
	 * vertical ScrollBar
	 */
	private final JScrollBar vBar;
	/**
	 * The y position of the middle point of the view on the Photo
	 */
	private int xmiddle = 0;
	/**
	 * The y position of the middle point of the view on the Photo
	 */
	private int ymiddle = 0;

	private final Refresher refresher = new Refresher();

	/**
	 * Constructor for a new ViewPanel
	 */
	public ViewPanel() {
		this(true);
	}
	public ViewPanel(boolean showToolbar){
		setLayout(new BorderLayout());
		addMouseListener(this);
		addMouseMotionListener(this);
		vBar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 0);
		vBar.addAdjustmentListener(this);
		hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 0);
		hBar.addAdjustmentListener(this);
		this.add(vBar, BorderLayout.EAST);
		this.add(hBar, BorderLayout.SOUTH);
		imp = new ImagePainter();
		this.add(imp, BorderLayout.CENTER);
		cp = this;
		if (showToolbar) {
			cp.add(MenuFactory.newMenuBar(new MenuListener(this)), BorderLayout.NORTH);
		}
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				calcZoom(true);
			}
		});
		addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(final MouseWheelEvent e) {
				int move = e.getWheelRotation();
				if (move > 0) {
					zoomp();
				} else if (move < 0) {
					zoomm();
				}
			}
		});
		hBar.setVisible(false);
		vBar.setVisible(false);
		new FileDrop(this, files -> {
			if (files.length > 0) {
				openImage(files[0]);
			}
		});
	}

	@Override
	public void adjustmentValueChanged(final AdjustmentEvent e) {
		calcZoom(true);
	}

	/**
	 * calculates the image with the current zoom factor
	 */
	private void calcZoom(boolean fast) {
		sWidth = imp.getWidth();
		sHeight = imp.getHeight();
		if (sWidth == 0 || sHeight == 0 || origin == null) return;
		Application.getMainWindow()
				.setTitle(
						Settings.currentImage.load().getAbsolutePath() + " - "
								+ (int) (factor * 100) + "%");
		int originHeight = origin.getHeight();
		int originWidth = origin.getWidth();


		double pWidth = originWidth; // The width of the part of the Original
		// image that can be shown on the screen
		double pHeight = originHeight; // The height of the part of the Original
		// image that can be shown on the screen

		double iHeight = originHeight * factor; // The height of the drawn Image
		double iWidth = originWidth * factor; // The width of the drawn Image
		if (iWidth > sWidth) {
			pWidth = sWidth / iWidth * originWidth;
			iWidth = sWidth;
			hBar.setMinimum((int) -((originWidth - pWidth) / 2));
			hBar.setMaximum((int) ((originWidth - pWidth) / 2));
			hBar.setVisible(true);
		} else {
			hBar.setMinimum(0);
			hBar.setMaximum(0);
			hBar.setVisible(false);
		}
		if (iHeight > sHeight) {
			pHeight = sHeight / iHeight * originHeight;
			iHeight = sHeight;
			int min = -(int) ((originHeight - pHeight) / 2);
			int max = (int) ((originHeight - pHeight) / 2);
			vBar.setMinimum(min);
			vBar.setMaximum(max);
			vBar.setVisible(true);
		} else {
			vBar.setMinimum(0);
			vBar.setMaximum(0);
			vBar.setVisible(false);

		}

		BufferedImage subImage;
		try {
			subImage = origin.getSubimage((int) (xmiddle + hBar.getValue() - pWidth / 2),
					(int) (ymiddle + vBar.getValue() - pHeight / 2), (int) pWidth, (int) pHeight);
		} catch (Exception e) {

			return;
		}
		if (iWidth < 2) {
			zoomp();
		}
		img = new BufferedImage((int) iWidth, (int) iHeight, origin.getType());

		Graphics2D g2d = img.createGraphics();
		// Zeichnet das Orignial Bild skalliert
		if (fast){
			//g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			refresher.startRefresh();
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		g2d.drawImage(subImage, 0, 0, (int) Math.round(iWidth), (int) Math.round(iHeight), null);
		cp.repaint();

	}

	@Override
	public void delete() {

		int result = JOptionPane.showConfirmDialog(this,
				VProperties.getValue("ask.delete.question"),
				VProperties.getValue("ask.delete.title"), JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {

			File img = Settings.currentImage.load();

			if (!img.delete()) {
				ViewException ve = new ViewException(ViewException.DELETE_FAILED);
				VExceptionHandler.raiseException(ve, ve.getErrorMsg());
			}
		}

	}



	/**
	 * Draws the Image
	 * 
	 * @param origin
	 *            The Original Image
	 */
	private void drawImage(final BufferedImage origin) {
		sWidth = imp.getWidth();
		sHeight = imp.getHeight();
		if (sWidth == 0 || sHeight == 0 || origin == null) return;
		double iHeight = origin.getHeight();
		double iWidth = origin.getWidth();
		double hFactor = 1.0;
		double wFactor = 1.0;

		if (sWidth >= origin.getWidth() && sHeight >= origin.getHeight()) {
			img = origin;
			factor = 1.0;

		} else {
			if (sWidth < origin.getWidth()) {
				wFactor = (double) sWidth / (double) origin.getWidth();
				iWidth = sWidth;
			}
			if (sHeight < origin.getHeight()) {
				hFactor = (double) sHeight / (double) origin.getHeight();
				iHeight = sHeight;
			}

			if (wFactor < hFactor) {
				iHeight = origin.getHeight() * wFactor;
				factor = wFactor;
			} else if (hFactor < wFactor) {
				iWidth = origin.getWidth() * hFactor;
				factor = hFactor;
			} else {
				factor = wFactor;
			}
			img = new BufferedImage((int) iWidth, (int) iHeight, origin.getType());
			Graphics2D g2d = img.createGraphics();
			// Zeichnet das Orignial Bild skalliert
			g2d.drawImage(origin, 0, 0, (int) Math.round(iWidth), (int) Math.round(iHeight), null);
		}

		Application.getMainWindow()
				.setTitle(
						Settings.currentImage.load().getAbsolutePath() + " - "
								+ (int) (factor * 100) + "%");
		xmiddle = origin.getWidth() / 2;
		ymiddle = origin.getHeight() / 2;
		cp.repaint();

	}

	@Override
	public void fullImage() {
		ArrayList<File> arr = new ArrayList<>();
		arr.add(Settings.currentImage.load());
		new Diapresentation(Application.getMainWindow(), arr, false, false, -1);
		System.out.println("fullImage");
	}

	@Override
	public Container getPanel() {

		return this;
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		// Nothing to do
	}

	@Override
	public void mouseDragged(final MouseEvent e) {
		int divX = e.getX() - oldEvent.getX();
		int divY = e.getY() - oldEvent.getY();

		hBar.setValue((int) (hBar.getValue() - divX * 1.0 / factor));
		vBar.setValue((int) (vBar.getValue() - divY * 1.0 / factor));
		oldEvent = e;

		calcZoom(true);

	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// Nothing to do

	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// nothing to do

	}

	@Override
	public void mouseMoved(final MouseEvent arg0) {
		// Nothing to do
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		oldEvent = e;
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		// nothing to do

	}

	@Override
	public void openImage(final File image) {
		if (image == null || image.equals(new File(""))) return;

		Settings.currentImage.save(image);
		Settings.currentDirectory.save(image.getParentFile());

		JFrame frame = Application.getMainWindow();
		if (frame != null) {
			frame.setTitle(Settings.currentDirectory.load() + System.getProperty("file.separator")
					+ Settings.currentImage.load().getName());
		}
		try {

			origin = ImageIO.read(image);

			Application.setProgramIcon(origin);
			hBar.setVisible(false);
			vBar.setVisible(false);
			drawImage(origin);
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
		if (origin == null) return;
		BufferedImage rotatedImage;
		String[] currentFilename = Settings.currentImage.load().getName().split("\\.");
		String fileExtension = "";
		if (currentFilename.length < 2) {
			fileExtension = currentFilename[currentFilename.length - 1];
		}

		rotatedImage = new BufferedImage(origin.getHeight(), origin.getWidth(),
				(fileExtension.equalsIgnoreCase("jpg") ? origin.getType()
						: BufferedImage.TYPE_INT_ARGB));

		Graphics2D g = (Graphics2D) rotatedImage.getGraphics();
		if (clockwise) {
			g.translate(rotatedImage.getWidth(), 0);
			g.rotate(Math.toRadians(90.0));
		} else {
			g.translate(0, rotatedImage.getHeight());
			g.rotate(Math.toRadians(-90.0));

		}

		g.drawImage(origin, 0, 0, null);
		origin = rotatedImage;
		drawImage(origin);

	}

	@Override
	public void save() {
		try {
			String currentImage = VSettings.loadSetting("current.image");
			if (currentImage != null) {
				ImageSaver.save(img, VSettings.loadSetting("current.dir"), currentImage);
			} else {
				VExceptionHandler.raiseMessage("The file couldn't be saved");
			}
		} catch (IOException e) {
			VExceptionHandler.raiseException(e, "The file couldn't be saved");
		}

	}

	@Override
	public void saveAs() {
		try {
			ImageSaver.saveAs(img, VSettings.loadSetting("current.dir"));
		} catch (IOException e) {
			VExceptionHandler.raiseException(e, "The file couldn't be saved");
		}
	}

	@Override
	public void setImage(final BufferedImage img) {
		origin = img;
		drawImage(origin);

	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoom0() {
		hBar.setVisible(false);
		vBar.setVisible(false);
		this.revalidate();
		drawImage(origin);
	}

	@Override
	public void zoom1() {
		factor = 1;
		calcZoom(false);

	}

	@Override
	public void zoomm() {

		if (factor < 1.5) {
			if (factor > 1.0 && factor < 1.05) {
				factor = 1;
			} else {
				factor -= 0.05;
			}
		} else if (factor < 2.5) {
			factor -= 0.1;
		} else {
			factor -= 0.2;
		}

		if (factor <= 0.009) {
			factor = 0.01;
		}
		calcZoom(true);
	}


	@Override
	public void zoomp() {
		if (factor < 1.5) {
			if (factor < 1.0 && factor > 0.95) {
				factor = 1;
			} else {
				factor += 0.05;
			}
		} else if (factor < 2.5) {
			factor += 0.1;
		} else {
			factor += 0.2;
		}
		calcZoom(true);

	}


	private class Refresher {
		private final Object lock = new Object();

		private static final int WAIT = 100;

		private long start;

		private boolean run;

		void startRefresh(){
			synchronized (lock) {
				start = System.currentTimeMillis();
				if (!run){
					run = true;
					new Thread(this::run).start();
				}
			}
		}

		private void run(){
			try {
				Thread.sleep(WAIT);

			boolean doContinue = true;
			do {
				long now = System.currentTimeMillis();
				long sleep;
				synchronized (lock) {
					sleep = now - (start + WAIT);
					if (sleep - 5 < 0) {
						doContinue = false;
						run = false;
					}
				}
					if (sleep > 0){
						Thread.sleep(sleep);
					}

			}while(doContinue);
				SwingUtilities.invokeLater(() ->	calcZoom(false));
			} catch (InterruptedException e) {
				e.printStackTrace();
				synchronized (lock) {
					run = false;
				}
			}


		}




	}

}
