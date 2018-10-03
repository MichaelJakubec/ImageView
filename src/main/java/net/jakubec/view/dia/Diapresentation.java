package net.jakubec.view.dia;

import net.jakubec.view.ImageView;
import net.jakubec.view.Settings.Settings;
import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.log.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class shows the diapresentation.
 *
 * @author amunra
 */
public class Diapresentation extends JWindow implements KeyListener {
	/**
	 * the height of the monitor
	 */
	private final int height;
	/**
	 * the width of the monitor
	 */
	private final int width;
	/**
	 * The time to wait till the next image
	 */
	private final int wait;
	private final ImageList list;

	/**
	 * boolean if the pictures should be displayerd in random order
	 */
	private boolean zufall = false;

	private boolean opened = true;
	/**
	 * checks if an Image should be displayed again or not in random mode
	 */
	private boolean delrandom = true;


	private final Container cp;
	private BufferedImage img = new BufferedImage(1, 1, 1);
	private BufferedImage nextImg = new BufferedImage(1, 1, 1);
	private Thread thread;

	private static JFrame frame = new JFrame();
	private final ImageView view;


	private volatile CountDown countDown;


	public Diapresentation(final ImageView view, final ArrayList<File> list,
						   final boolean zufall, final boolean delrand, final int wait) {
		super(frame);

		cp = this.getContentPane();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		this.setSize(gs[0].getDisplayMode().getWidth(), gs[0].getDisplayMode().getHeight());

		cp.setBackground(Color.black);
		setBackground(Color.black);
		this.setFocusable(true);
		this.view = view;
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
		this.list = new ImageList(list);
		frame.setVisible(true);
		this.setVisible(true);
		nextPhoto();
		if (wait != -1) {
			this.wait = wait * 1000;
			newThread();
		} else {
			this.wait = wait;
			repaint();
			drawPhoto();
			SwingUtilities.invokeLater(this::repaint);

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
		switch (event.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				pauseCountDown();
				stopDia();
				return;
			case KeyEvent.VK_SPACE:
				if (wait != -1) {
					if (this.countDown == null) {
						newThread();
					} else {
						pauseCountDown();
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
				break;
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

	private void nextPhoto() {
		try {
			File file = this.list.nextPhoto();
			if (file != null) {
				this.nextImg = ImageIO.read(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
			nextPhoto();
		}
	}

	private void previousPhoto() {
		try {
			pauseCountDown();
			this.list.previousPhoto();
			this.nextImg = ImageIO.read(this.list.nextPhoto());
			drawPhoto();
		} catch (IOException e) {
			e.printStackTrace();
			previousPhoto();
		}
	}

	public void pauseCountDown() {
		if (countDown != null) {
			countDown.cancel();
		}
	}

	public void keyReleased(final KeyEvent event) {

	}

	public void keyTyped(final KeyEvent event) {

	}

	public void newThread() {
		Logger.logMessage("Thread start");
		this.countDown = new CountDown();
		thread = new Thread(this.countDown);
		thread.start();
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


	private void stopDia() {
		Logger.logMessage("stopDia");
		this.pauseCountDown();
		if (this.countDown != null) {
			this.countDown.stop();
		}

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

	private class ImageList {

		/**
		 * List with all Images to display
		 */
		private final ArrayList<File> list;
		private final Random r = new Random(1000);
		private final LinkedList<File> shownImages = new LinkedList<>();
		private int previous = -1;
		private int picture = -1;


		private ImageList(ArrayList<File> list) {
			this.list = list;
		}

		synchronized void previousPhoto() {
			if (previous == -1) {
				previous =0;
			}
			previous+=2;
		}

		synchronized File nextPhoto() {
			if (previous >=  0) {
				File photo = shownImages.get(previous--);
				Logger.logMessage("Show Previous Photo:" + previous + " " + photo.getName());
				return photo;
			}
			File nextFile;

			if (zufall) {
				nextFile = nextRandom();
			} else {
				nextFile = nextImage();
			}
			if (nextFile == null) {
				return null;
			}
			this.shownImages.add(0, nextFile);
			Logger.logMessage("nextImage: " + nextFile.getName() + " " + shownImages.size());

			if (nextImg == null) {
				return nextPhoto();
			}
			return nextFile;


		}

		synchronized private File nextImage() {
			picture++;
			if (picture >= list.size()) {
				stopDia();
				return null;
			}
			opened = false;

			return list.get(picture);

		}

		synchronized private File nextRandom() throws IllegalArgumentException {

			if (delrandom && !opened) {
				list.remove(picture);
			}
			if (list.size() <= 0) {
				stopDia();
				return null;
			}
			Logger.logMessage("New Random picture");
			picture = r.nextInt(list.size());
			opened = false;

			return list.get(picture);
		}
	}


	private class CountDown implements Runnable {
		private volatile boolean run = true;
		private volatile boolean stop = false;
		private long start;

		public void cancel() {
			start = System.currentTimeMillis();
		}

		public void stop() {
			this.run = false;
		}

		public void run() {

			repaint();
			int nextWait = wait;

			while (run) {
				drawPhoto();
				start = System.currentTimeMillis();
				nextPhoto();
				long end = System.currentTimeMillis();
				nextWait = wait - (int) (end - start);
				while (nextWait > 0) {
					try {
						Logger.logMessage("Sleep " + nextWait);
						Thread.sleep(nextWait);
					} catch (InterruptedException IE) {
					}
					end = System.currentTimeMillis();
					nextWait = wait - (int) (end - start);
				}
			}
		}

	}
}

