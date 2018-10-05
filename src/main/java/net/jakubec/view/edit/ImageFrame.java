package net.jakubec.view.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import net.jakubec.view.edit.plain.BasicLayer;
import net.jakubec.view.edit.plain.ImageLayer;
import net.jakubec.view.edit.plain.LineLayer;
import net.jakubec.view.exception.VExceptionHandler;
import net.jakubec.view.history.CreateLayerHistoryItem;
import net.jakubec.view.history.HistoryItem;
import net.jakubec.view.history.MoveHistoryItem;
import net.jakubec.view.listener.LayerEvent;
import net.jakubec.view.listener.LayerSelectionListener;

public class ImageFrame extends JInternalFrame implements LayerSelectionListener {

	private class ImagePainter extends JPanel {
		private static final int RECT_SIZE = 5;

		private void drawImageZoomed(Graphics g, int x, int y) {
			int sHeight = getHeight();
			int sWidth = getWidth();


			double pWidth = workingWidth; // The width of the part of the
			// Original image that can be shown on the screen
			double pHeight = workingHeight; // The height of the part of the
			// Original image that can be shown on the screen

			double iHeight = workingHeight * factor; // The height of the drawn Image
			double iWidth = workingWidth * factor; // The width of the drawn Image
			if (iWidth > sWidth) {
				pWidth = sWidth / iWidth * workingWidth;
				iWidth = sWidth;
				hBar.setMinimum((int) -((workingWidth - pWidth) / 2));
				hBar.setMaximum((int) ((workingWidth - pWidth) / 2));
				hBar.setVisible(true);
			} else {
				hBar.setMinimum(0);
				hBar.setMaximum(0);
				hBar.setVisible(false);
			}
			if (iHeight > sHeight) {
				pHeight = sHeight / iHeight * workingHeight;
				iHeight = sHeight;
				int min = -(int) ((workingHeight - pHeight) / 2);
				int max = (int) ((workingHeight - pHeight) / 2);
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
				subImage = img.getSubimage((int) (xMiddle + hBar.getValue() - pWidth / 2),
						(int) (yMiddle + vBar.getValue() - pHeight / 2), (int) pWidth,
						(int) pHeight);
			} catch (Exception e) {
				VExceptionHandler.raiseException(e);
				return;
			}
			if (x < 0) {
				x = 0;
			}
			if (y < 0) {
				y = 0;
			}
			g.drawImage(subImage, x, y, (int) iWidth, (int) iHeight, null);

		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			int scaledWorkingWidth = (int) (workingWidth * factor);
			int scaledWorkingHeight = (int) (workingHeight * factor);

			int x = (int) ((getWidth() - scaledWorkingWidth) / 2.0);
			int y = (int) ((getHeight() - scaledWorkingHeight) / 2.0);

			Color lastStartColor = Color.white;
			for (int i = 0; i * RECT_SIZE < scaledWorkingWidth; i++) {
				lastStartColor = lastStartColor.equals(Color.WHITE) ? Color.LIGHT_GRAY
						: Color.WHITE;
				g.setColor(lastStartColor);
				int drawWidth;
				if ((i + 1) * RECT_SIZE >= scaledWorkingWidth) {
					drawWidth = scaledWorkingWidth - i * RECT_SIZE;
				} else {
					drawWidth = RECT_SIZE;
				}
				for (int j = 0; j * RECT_SIZE < scaledWorkingHeight; j++) {
					g.setColor((g.getColor().equals(Color.WHITE) ? Color.LIGHT_GRAY : Color.WHITE));
					int drawHeight;
					if ((j + 1) * RECT_SIZE >= scaledWorkingHeight) {
						drawHeight = scaledWorkingHeight - j * RECT_SIZE;
					} else {
						drawHeight = RECT_SIZE;
					}

					g.fillRect(x + i * RECT_SIZE, y + j * RECT_SIZE, drawWidth, drawHeight);

				}
			}
			img = new BufferedImage(workingWidth, workingHeight, BufferedImage.TYPE_INT_ARGB);

			for (BasicLayer plain : plains) {
				plain.draw(img.getGraphics());
			}
			drawImageZoomed(g, x, y);

			if (active) {
				drawActiveTool(g, x, y);
			}

		}
	}

	private boolean active;

	private int aktiveLayer;

	private Point currentMouse;

	private double factor = 1.0;

	/**
	 * Horizontal ScrollBar
	 */
	private JScrollBar hBar;

	private Stack<HistoryItem> history;

	private BufferedImage img;

	private Point mousePressed;

	private ImagePainter painter;

	private EditPanel parent;

	private ArrayList<BasicLayer> plains = new ArrayList<>();

	private Point start;

	/**
	 * vertical ScrollBar
	 */
	private JScrollBar vBar;

	/**
	 * the height of the editable area. The real height of the frame can be
	 * different
	 */
	private int workingHeight;

	/**
	 * the height of the editable area. The real height of the frame can be
	 * different
	 */
	private int workingWidth;
	private int xMiddle;

	private int yMiddle;

	public ImageFrame(EditPanel parent, BufferedImage img) {
		this(parent, img, null);

	}

	public ImageFrame(EditPanel parent, BufferedImage img, String title) {
		super("", true, true, true, true);
		construct(parent, img, title);
	}

	private void construct(EditPanel parent, BufferedImage img, String title) {
		this.parent = parent;
		history = new Stack<>();
		plains.add(new ImageLayer(img));

		if (title != null) {
			setTitle(title);
		}
		Insets s = this.getInsets();
		vBar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 0);
		vBar.addAdjustmentListener(arg0 -> repaint());
		hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 0);
		hBar.addAdjustmentListener(arg0 -> repaint());
		setVisible(true);
		this
				.setSize((int) (s.left + s.right + vBar.getPreferredSize().getWidth() + img
						.getWidth()), (int) (s.top + s.bottom + hBar.getPreferredSize().getHeight()
						+ img.getHeight() + 20));

		workingWidth = img.getWidth();
		workingHeight = img.getHeight();
		xMiddle = workingWidth / 2;
		yMiddle = workingHeight / 2;
		this.img = new BufferedImage(workingWidth, workingHeight, BufferedImage.TYPE_INT_ARGB);

		painter = new ImagePainter();
		painter.addKeyListener(new KeyAdapter() {
		});
		painter.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				ImageFrame.this.mouseDragged(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {

			}
		});
		painter.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressed = e.getPoint();
				start = mousePressed;

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				switch (parent.getEditMode()) {
					case MOVE:
						MoveHistoryItem newHis = new MoveHistoryItem(plains.get(aktiveLayer),
								(int) ((e.getX() - start.getX()) / factor),
								(int) ((e.getY() - start.getY()) / factor));
						history.push(newHis);
						break;
					case LINE:
						Point startLine = screenToImageCoordinates(start);
						Point endLine = screenToImageCoordinates(e.getPoint());
						LineLayer newLayer = new LineLayer(startLine, endLine, EditPanel.foreground, 5);

						HistoryItem item = new CreateLayerHistoryItem(newLayer, plains);
						plains.add(newLayer);
						history.add(item);
						repaint();
						parent.getLayerManager().doRepaint();
						aktiveLayer = plains.size() - 1;
						break;
					default:
						// do nothing

				}

				active = false;

			}
		});
		painter.setBackground(Color.LIGHT_GRAY);
		getContentPane().add(painter, BorderLayout.CENTER);

		getContentPane().add(vBar, BorderLayout.WEST);
		getContentPane().add(hBar, BorderLayout.SOUTH);
		this.parent.getLayerManager().setNewLayers(this, plains, workingWidth, workingHeight);
		setVisible(true);

	}

	private void drawActiveTool(Graphics g, int x, int y) {

		switch (parent.getEditMode()) {
		case LINE:
			double xLineStart = start.getX();
			double xLineEnd = currentMouse.getX();
			double yLineStart = start.getY();
			double yLineEnd = currentMouse.getY();
			if (workingHeight * factor < painter.getHeight()) {
				// double x= (painter.getHeight()-workingHeight*factor)/2;

				if (yLineStart < y) {
					yLineStart = y;
				} else if (yLineStart > y + workingHeight * factor) {
					yLineStart = y + workingHeight * factor;
				}
				if (yLineEnd < y) {
					yLineStart = y;
				} else if (yLineEnd > y + workingHeight * factor) {
					yLineEnd = y + workingHeight * factor;
				}

			}
			if (workingWidth * factor < painter.getWidth()) {
				// double x= (painter.getHeight()-workingHeight*factor)/2;

				if (xLineStart < x) {
					xLineStart = x;
				} else if (xLineStart > x + workingWidth * factor) {
					xLineStart = x + workingWidth * factor;
				}
				if (xLineEnd < x) {
					xLineStart = x;
				} else if (yLineEnd > x + workingWidth * factor) {
					xLineEnd = x + workingWidth * factor;
				}

			}

			g.drawLine((int) xLineStart, (int) yLineStart, (int) xLineEnd, (int) yLineEnd);
		}
	}





	@Override
	public void layerChanged(LayerEvent e) {

	}

	private void mouseDragged(MouseEvent e) {
		EditPanel.EditMode m = parent.getEditMode();

		switch (m) {
		case LINE:
			active = true;
			currentMouse = e.getPoint();
			repaint();
			break;
		case MOVE:
			Point tmp = e.getPoint();
			int divX = (int) ((tmp.getX() - mousePressed.getX()) / factor);
			int divY = (int) ((tmp.getY() - mousePressed.getY()) / factor);
			mousePressed = tmp;

			plains.get(aktiveLayer).moveImage(divX, divY);
			repaint();
			break;
		default:
			// do nothing;

		}

	}





	private Point screenToImageCoordinates(Point enter) {
		int x, y;
		int sWidth = painter.getWidth();
		int sHeight = painter.getHeight();
		int pWidth, pHeight;
		int xImgStart, yImgStart;
		if (workingWidth * factor > sWidth) {
			x = 0;
			pWidth = (int) (painter.getWidth() / (workingWidth * factor) * workingWidth);
			xImgStart = xMiddle + hBar.getValue() - pWidth / 2;

		} else {
			x = (int) ((sWidth - workingWidth * factor) / 2.0);
			xImgStart = 0;
		}
		if (workingHeight * factor > sHeight) {
			y = 0;
			pHeight = (int) (painter.getHeight() / (workingHeight * factor) * workingHeight);
			yImgStart = yMiddle + vBar.getValue() - pHeight / 2;
		} else {
			y = (int) ((sHeight - workingHeight * factor) / 2.0);
			yImgStart = 0;

		}
		double xCoord;
		double yCoord;

		xCoord = enter.getX() - x + xImgStart * factor;
		yCoord = enter.getY() - y + yImgStart * factor;
		if (xCoord < 0) {
			xCoord = 0;
		} else if (xCoord > workingWidth * factor) {
			xCoord = workingWidth * factor;
		}

		if (yCoord < 0) {
			yCoord = 0;
		} else if (yCoord > workingHeight * factor) {
			yCoord = workingHeight * factor;
		}

		xCoord = xCoord / factor;
		yCoord = yCoord / factor;

		return new Point((int) xCoord - 1, (int) yCoord - 1);
	}

	public void undo() {
		if (history.size() > 0) {
			HistoryItem his = history.pop();
			his.undo();
			this.repaint();
		}
	}

	public void zoom1() {
		factor = 1.0;
		setTitle(String.valueOf((int) (factor * 100)) + "%");

		repaint();
	}

	public void zoomm() {
		factor -= 0.1;
		setTitle(String.valueOf((int) (factor * 100)) + "%");

		repaint();
	}

	public void zoomp() {
		factor += 0.1;
		setTitle(String.valueOf((int) (factor * 100)) + "%");

		repaint();
	}

}
