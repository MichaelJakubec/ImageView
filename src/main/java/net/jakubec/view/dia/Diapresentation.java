package net.jakubec.view.dia;


import net.jakubec.view.Settings.SettingsException;
import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.ViewPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.EventListenerList;
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
	 * The time to wait till the next image
	 */
	private final int wait;
	private final ImageList list;

	private static final Logger log = LogManager.getLogger(Diapresentation.class);

	private final EventListenerList listeners = new EventListenerList();

	/**
	 * boolean if the pictures should be displayerd in random order
	 */
	private boolean zufall;

	private boolean opened = true;
	/**
	 * checks if an Image should be displayed again or not in random mode
	 */
	private boolean delrandom;

	private Window view;

	private ViewPanel panel = new ViewPanel(false);
	private BufferedImage nextImg = new BufferedImage(1, 1, 1);

	private volatile CountDown countDown;


	public Diapresentation(final Window view, final ArrayList<File> list,
						   final boolean zufall, final boolean delrand, final int wait, DiaPresentationListener listener) {
		super(view);

		this.setContentPane(this.panel);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		this.setSize(gs[0].getDisplayMode().getWidth(), gs[0].getDisplayMode().getHeight());
		if (listener != null) {
			this.listeners.add(DiaPresentationListener.class, listener);
		}


		setBackground(Color.black);
		this.setFocusable(true);
		this.view = view;
		this.zufall = zufall;
		view.addKeyListener(this);
		this.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(final FocusEvent arg0) {
				view.requestFocus();
			}

			@Override
			public void focusLost(final FocusEvent arg0) {

			}

		});
		delrandom = delrand;
		this.list = new ImageList(list);
		// view.setVisible(true);
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
		this.panel.setImage(this.nextImg);
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
						stopThread();
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
				}
		}
	}

	private void stopThread() {
		if (this.countDown != null) {
			this.countDown.stop();
			this.countDown = null;
		}
	}

	private void nextPhoto() {
		File file = this.list.nextPhoto();
		if (file != null) {
			try {
				this.nextImg = ImageIO.read(file);
			} catch (IOException e) {
				System.out.println(file + " can't be opened");
				e.printStackTrace();
				nextPhoto();
			}
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

	private void pauseCountDown() {
		if (countDown != null) {
			countDown.cancel();
		}
	}

	public void keyReleased(final KeyEvent event) {

	}

	public void keyTyped(final KeyEvent event) {

	}

	private void newThread() {
		log.debug("Thread start");
		this.countDown = new CountDown();
		Thread thread = new Thread(this.countDown);
		thread.start();
	}


	private void stopDia() {
		log.debug("stopDia");
		this.pauseCountDown();
		stopThread();

		try {
			VSettings.saveProps();
		} catch (SettingsException e) {
			//TODO Exception Handling
			e.printStackTrace();
		}

		setVisible(false);
		view.removeKeyListener(this);
		dispose();

		fireDiaEvent(new DiaEvent(DiaEvent.DiaEventType.FINISHED));

	}

	private void fireDiaEvent(DiaEvent diaEvent) {
		for(DiaPresentationListener listener: listeners.getListeners(DiaPresentationListener.class)) {
			listener.presentationChanged(diaEvent);
		}
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
				log.debug("Show Previous Photo: {} {}", previous, photo.getName());
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
			log.debug("nextImage: {} {}", nextFile.getName(), shownImages.size());

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
			log.debug("New Random picture");
			picture = r.nextInt(list.size());
			opened = false;

			return list.get(picture);
		}
	}


	private class CountDown implements Runnable {
		private volatile boolean run = true;
		private long start;

		void cancel() {
			start = System.currentTimeMillis();
		}

		void stop() {
			this.run = false;
		}

		@Override
		public void run() {

			repaint();


			while (run) {
				drawPhoto();
				start = System.currentTimeMillis();
				nextPhoto();
				long end = System.currentTimeMillis();
				int nextWait = wait - (int) (end - start);
				while (nextWait > 0) {
					try {
						log.debug("Sleep " + nextWait);
						Thread.sleep(nextWait);
					} catch (InterruptedException IE) {
						//Ignore exception
					}
					end = System.currentTimeMillis();
					nextWait = wait - (int) (end - start);
				}
			}
		}

	}
}

