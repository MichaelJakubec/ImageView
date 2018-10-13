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


import net.jakubec.view.dia.Diapresentation;
import net.jakubec.view.filedrop.FileDrop;
import net.jakubec.view.listener.ImageDisplayListener;
import net.jakubec.view.listener.ViewNavigationListener;
import net.jakubec.view.properties.VProperties;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
	 * the current file object that is displayed
	 */
	private File currentImage;

	private boolean showScrollbars = true;

	/**
	 * Constructor for a new ViewPanel without the default toolbar.
	 */
	public ViewPanel() {
		this(false);
	}

	public ViewPanel(boolean showToolbar) {

		this.setLayout(new BorderLayout());

		cp = this;
		cp.setBackground(Color.black);


		addMouseListener(this);
		addMouseMotionListener(this);
		vBar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 0);
		vBar.addAdjustmentListener(this);
		hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 0);
		hBar.addAdjustmentListener(this);
		cp.add(vBar, BorderLayout.EAST);
		cp.add(hBar, BorderLayout.SOUTH);
		imp = new ImagePainter();
		cp.add(imp, BorderLayout.CENTER);
		if (showToolbar) {
			cp.add(new ViewPanel.ToolBarBuilder().buildMenuBar(new ViewNavigationListener(this)), BorderLayout.NORTH);
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
				if (move < 0) {
					zoomp();
				} else if (move > 0) {
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

		//add(cp, BorderLayout.CENTER);
	}

	@Override
	public void adjustmentValueChanged(final AdjustmentEvent e) {
		calcZoom(true);
	}

	/**
	 * Specifies whether the scrollbars should be visible in case the image is larger than the screen or not. Scrollbars are only displayed if the image is larger than the screen. If the image is smaller than the screen the scrollbars are never shown, regardless of whether the
	 * {@link #isScrollBarsVisible()} returns true or not.
	 * @param show if true scrollbars are shown when needed otherwise not.
	 */
	public void setScrollBarsVisible(boolean show){
		this.showScrollbars = show;
		hBar.setVisible(show);
		vBar.setVisible(show);
	}

	/**
	 * Determins whether the scrollbars get shown if needed or not
	 * @return true if scrollbars get shown, false otherwise
	 */
	public boolean isScrollBarsVisible() {
		return showScrollbars;
	}

	/**
	 * calculates the image with the current zoom factor
	 */
	private void calcZoom(boolean fast) {
		sWidth = imp.getWidth();
		sHeight = imp.getHeight();
		if (sWidth == 0 || sHeight == 0 || origin == null) return;


		for (ImageDisplayListener l:listenerList.getListeners(ImageDisplayListener.class)){
			l.zoomLevelChanged(factor);
		}
		// TODO zoom listener
//		Application.getMainWindow()
//				.setTitle(
//						Settings.currentImage.load().getAbsolutePath() + " - "
//								+ (int) (factor * 100) + "%");
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
			hBar.setVisible(showScrollbars);
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
			vBar.setVisible(showScrollbars);
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
		if (fast) {
			refresher.startRefresh();
		} else {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}
		g2d.drawImage(subImage, 0, 0, (int) Math.round(iWidth), (int) Math.round(iHeight), null);
		cp.repaint();
	}

	public void addImageDisplayListener(ImageDisplayListener listener){
		this.listenerList.add(ImageDisplayListener.class, listener);
	}

	private void removeImageDisplayListener(ImageDisplayListener listener){
		this.listenerList.remove(ImageDisplayListener.class, listener);
	}

	public void showToolbar(ToolBarBuilder viewerToolbarBuilder) {
		Component old = ((BorderLayout)cp.getLayout()).getLayoutComponent(BorderLayout.NORTH);
		if (old != null) {
			this.cp.remove(old);
		}
		cp.add(viewerToolbarBuilder.buildMenuBar(new ViewNavigationListener(this)), BorderLayout.NORTH);
		this.invalidate();
		this.revalidate();
	}

	/**
	 * Draws the Image
	 *
	 * @param origin The Original Image
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

			for (ImageDisplayListener l:listenerList.getListeners(ImageDisplayListener.class)){
				l.zoomLevelChanged(factor);
			}

			img = new BufferedImage((int) iWidth, (int) iHeight, origin.getType());
			Graphics2D g2d = img.createGraphics();
			// Zeichnet das Orignial Bild skalliert
			g2d.drawImage(origin, 0, 0, (int) Math.round(iWidth), (int) Math.round(iHeight), null);
		}
		xmiddle = origin.getWidth() / 2;
		ymiddle = origin.getHeight() / 2;
		cp.repaint();

	}

	@Override
	public void fullImage() {
		ArrayList<File> arr = new ArrayList<>();
		arr.add(currentImage);
		Window topFrame = SwingUtilities.getWindowAncestor(this);
		new Diapresentation(topFrame, arr, false, false, -1, null);
		System.out.println("fullImage");
	}


	@Override
	public void setBackground(Color color) {
		//cp.setBackground(color);
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
	public void openImage(final File image) throws ViewException {
		if (image == null || image.equals(new File(""))) return;
		if (image.equals(currentImage)) return;

		try {

			origin = ImageIO.read(image);

			hBar.setVisible(false);
			vBar.setVisible(false);
			drawImage(origin);
			this.currentImage = image;
			for (ImageDisplayListener l: listenerList.getListeners(ImageDisplayListener.class)){
				l.imageOpened(image);
			}
		} catch (Exception e) {
			throw new ViewException(e, ViewException.OPEN_FAILED);
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


//		rotatedImage = new BufferedImage(origin.getHeight(), origin.getWidth(),
//				(fileExtension.equalsIgnoreCase("jpg") ? origin.getType()
//						: BufferedImage.TYPE_INT_ARGB));

		rotatedImage = new BufferedImage(origin.getHeight(), origin.getWidth(), origin.getType());

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
	public void setImage(final BufferedImage img) {
		origin = img;
		drawImage(origin);

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

		void startRefresh() {
			synchronized (lock) {
				start = System.currentTimeMillis();
				if (!run) {
					run = true;
					new Thread(this::run).start();
				}
			}
		}

		private void run() {
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
					if (sleep > 0) {
						Thread.sleep(sleep);
					}

				} while (doContinue);
				SwingUtilities.invokeLater(() -> calcZoom(false));
			} catch (InterruptedException e) {
				e.printStackTrace();
				synchronized (lock) {
					run = false;
				}
			}
		}
	}

	/**
	 * Creates a new MenuBar for the View
	 *
	 * @return the MenuBar for the view
	 */

	public static class ToolBarBuilder {

		private ViewNavigationListener act;

		public void addButtonsToToolbar(JToolBar toolbar) {




			JButton btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/zoom+.gif")));
			btn.setActionCommand("zoom+");
			toolbar.add(btn);
			btn.addActionListener(act);

			btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/zoom-.gif")));
			btn.setActionCommand("zoom-");
			toolbar.add(btn);
			btn.addActionListener(act);

			btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/zoom.gif")));
			btn.setActionCommand("zoom0");
			toolbar.add(btn);
			btn.addActionListener(act);

			btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/zoom100.gif")));
			btn.setActionCommand("zoom1");
			toolbar.add(btn);
			btn.addActionListener(act);
			toolbar.addSeparator();

			btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/Rot-.gif")));
			btn.setActionCommand("rot-");
			toolbar.add(btn);
			btn.addActionListener(act);
			btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/Rot+.gif")));
			btn.setActionCommand("rot+");
			toolbar.add(btn);
			btn.addActionListener(act);
		}


		private JToolBar buildMenuBar(ViewNavigationListener listener) {
			this.act = listener;
			JToolBar menuBar = new JToolBar();
			addButtonsToToolbar(menuBar);
			return menuBar;
		}


	}


}
