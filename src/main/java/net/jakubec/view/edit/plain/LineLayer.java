package net.jakubec.view.edit.plain;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class LineLayer extends BasicLayer {
	private Color color;

	private Point end;

	private int lineStroke;

	private Point start;

	public LineLayer(Point start, Point end) {
		this(start, end, Color.blue);
	}

	public LineLayer(Point start, Point end, Color c) {
		this(start, end, c, 1);
	}

	public LineLayer(Point start, Point end, Color c, int lineStroke) {
		super(0, 0);
		this.start = start;
		this.end = end;
		color = c;
		this.lineStroke = lineStroke;
	}

	@Override
	public void draw(Graphics g) {
		if (start == null || end == null) {
			return;
		}
		g.setColor(color);
		((Graphics2D) g).setStroke(new BasicStroke(lineStroke));
		g.drawLine((int) (getX() + start.getX()), (int) (getY() + start.getY()),
				(int) (end.getX() + getX()), (int) (getY() + end.getY()));
		// g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(),
		// (int) end.getY());
	}

}
