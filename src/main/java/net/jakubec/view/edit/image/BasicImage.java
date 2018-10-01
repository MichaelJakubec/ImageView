package net.jakubec.view.edit.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BasicImage {
	private int height;

	private Color[][] pixels;
	private int width;

	public BasicImage(int w, int h) {
		pixels = new Color[w][h];
		height = h;
		width = w;
	}

	public void drawImage(Graphics g, int x, int y) {
		BufferedImage img = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
		// img.setRGB(x, y, rgb);

	}
}
