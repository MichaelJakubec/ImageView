package net.jakubec.view.dia;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JWindow;

import net.jakubec.view.ImageView;
import net.jakubec.view.Settings.Settings;
import net.jakubec.view.Settings.VSettings;

/**
 * This class shows the diapresentation.
 * 
 * @author amunra
 * 
 */
public class Diapresentation extends JWindow implements Runnable, KeyListener {
	/** the height of the monitor */
	private final int height;
	/** the width of the monitor */
	private final int width;
	/** The time to wait till the next image */
	private int wait;
	/**
	 * the current number of the displayed Image
	 */
	private int picture = -1;
	/** boolean if the pictures should be displayerd in random order */
	private boolean zufall = false;

	private boolean opened = true;
	/**
	 * checks if an Image should be displayed again or not in random mode
	 */
	private boolean delrandom = true;

	private boolean run = true;
	/**
	 * List with all Images to display
	 */
	private final ArrayList<File> list;

	private final Container cp;
	private BufferedImage img = new BufferedImage(1, 1, 1);
	private BufferedImage nextImg = new BufferedImage(1, 1, 1);
	private final Random r = new Random();
	private Thread thread;

	private static JFrame frame = new JFrame();
	private final ImageView view;
	private int replayImage = 0;
	private final List<File> shownFotos = new LinkedList<>();

	/**
	 * 
	 * @param view
	 * @param list
	 * @param zufall
	 * @param delrand
	 */
	Diapresentation(final ImageView view, final ArrayList<File> list, final boolean zufall,
			final boolean delrand) {
		super(frame);
		cp = this.getContentPane();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		this.setSize(gs[0].getDisplayMode().getWidth(), gs[0].getDisplayMode().getHeight());

		cp.setBackground(Color.black);
		setBackground(Color.black);
		this.setFocusable(true);
		this.view = view;
		wait = -1;
		this.zufall = zufall;
		frame.addKeyListener(this);
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(final FocusEvent arg0) {
				frame.requestFocus();
			}

			@Override
			public void focusLost(final FocusEvent arg0) {

			}

		});
		delrandom = delrand;
		height = getHeight();
		width = getWidth();
		this.list = list;
		frame.setVisible(true);
		this.setVisible(true);
	}

	public Diapresentation(final ImageView view, final ArrayList<File> list,
			final boolean zufall, final boolean delrand, final int wait) {
		this(view, list, zufall, delrand);
		this.wait = wait;
		if (wait != -1) {
			this.wait = wait * 1000;
			newThread();
		} else {
			repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException IE) {
			}
			nextPhoto();
			drawPhoto();
			repaint();
		}
	}

	private void drawPhoto() {

		double iHeight = nextImg.getHeight();
		double iWidth = nextImg.getWidth();
		double hFactor = 1.0;
		double wFactor = 1.0;

		if (width >= nextImg.getWidth() && height >= nextImg.getHeight()) {
			img = nextImg;
		} else {
			if (width < nextImg.getWidth()) {
				wFactor = width / (double) nextImg.getWidth();
				iWidth = width;
			}
			if (height < nextImg.getHeight()) {
				hFactor = height / (double) nextImg.getHeight();
				iHeight = height;
			}

			if (wFactor < hFactor) {
				iHeight = nextImg.getHeight() * wFactor;
			} else if (hFactor < wFactor) {
				iWidth = nextImg.getWidth() * hFactor;
			}
			img = new BufferedImage((int) iWidth, (int) iHeight, BufferedImage.TYPE_INT_ARGB);
			// img=new BufferedImage((int)iWidth,(int)iHeight,origin.getType());
			Graphics2D g2d = img.createGraphics();
			g2d.drawImage(nextImg, 0, 0, (int) Math.round(iWidth), (int) Math.round(iHeight), null);
		}

		cp.repaint();
		repaint();
	}

	public void keyPressed(final KeyEvent event) {
		switch(event.getKeyCode()){
			case KeyEvent.VK_ESCAPE:
				run = false;
				stopDia();
				return;
			case  KeyEvent.VK_SPACE:
				if (wait != -1) {
					if (run) {
						run = false;
					} else {
						run = true;
						thread = new Thread(this);
						thread.start();
					}
				} else {
					nextPhoto();
					drawPhoto();
				}
				break;
			case KeyEvent.VK_LEFT:
				previousPhoto();
				drawPhoto();
				break;
			case KeyEvent.VK_RIGHT:
				nextPhoto();
				drawPhoto();
			default:
			if (wait == -1) {
				nextPhoto();
				drawPhoto();
			} else {
				stopDia();
				return;
			}
		}
	}

	public void keyReleased(final KeyEvent event) {

	}

	public void keyTyped(final KeyEvent event) {

	}

	public void newThread() {
		thread = new Thread(this);
		thread.start();
	}

	private File nextImage() {

		if (picture == list.size() - 1) {
			stopDia();
			return null;
		}

		picture++;
		opened = false;

		return list.get(picture);

	}

	private void previousPhoto(){
		if (zufall) {
			replayImage ++;
			File nextFile = shownFotos.get(replayImage);
			try {
				this.nextImg = ImageIO.read(nextFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void nextPhoto() {
		File nextFile;
		if (zufall) {
			nextFile = nextRandom();
		} else {
			nextFile = nextImage();
		}
		if (nextFile == null){
			return;
		}
		try {

			this.shownFotos.add(0,nextFile);
			this.nextImg = ImageIO.read(nextFile);
			if (nextImg == null) {
				nextPhoto();
			}
		} catch (IOException e) {
			nextPhoto();
		}

	}

	private File nextRandom() throws IllegalArgumentException {

		if (replayImage > 0){
			File nextImage = shownFotos.get(replayImage);
			replayImage--;
			return nextImage;
		}

		if (delrandom && !opened) {
			list.remove(picture);
		}
		if (list.size() <= 0) {
			stopDia();
			return null;
		}
		picture = r.nextInt(list.size());
		opened = false;

		return list.get(picture);
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);

		try {
			g.drawImage(img, (width - img.getWidth()) / 2 + getInsets().left,
					(height - img.getHeight()) / 2 + getInsets().top, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException IE) {
		}
		repaint();
		int nextWait = wait;
		while (run) {
			drawPhoto();
			long start = System.currentTimeMillis();
			nextPhoto();
			long end = System.currentTimeMillis();
			nextWait = wait - (int) (end - start);
			if (nextWait > 0) {
				try {
					Thread.sleep(nextWait);
				} catch (InterruptedException IE) {

				}
			}

		}
	}

	private void stopDia() {
		shownFotos.clear();
		run = false;

		VSettings.saveProps();

		setVisible(false);
		frame.setVisible(false);
		frame.dispose();
		dispose();
		view.setVisible(true);
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				view.repaint();
			}
		};
		t.start();
		view.showViewMode();
		view.open(Settings.currentImage.load());
		view.repaint();
	}

	private void switchImage() {

	}
}
