package net.jakubec.view.edit.image;

public class Pixel {

	private short alpha;

	private short blue;

	private short green;

	private short red;

	/**
	 * Creates a new Pixel Object with the color Black and which is totally
	 * transparent
	 */
	public Pixel() {
		alpha = 0;
		red = green = blue = 0;
	}

	public Pixel(int htmlColor) {
		setColor(htmlColor);
	}

	/**
	 * Creates a new Pixel with the specified color.
	 * 
	 * @param r
	 *            the value for the red Color. The allowed range is between 0
	 *            and 255.
	 * @param g
	 *            the value for the green Color. The allowed range is between 0
	 *            and 255.
	 * @param b
	 *            the value for the blue Color. The allowed range is between 0
	 *            and 255.
	 */
	public Pixel(short r, short g, short b) {
		this(r, g, b, (short) 100);
	}

	/**
	 * Creates a new Pixel with the specified color.
	 * 
	 * @param r
	 *            the value for the red Color. The allowed range is between 0
	 *            and 255.
	 * @param g
	 *            the value for the green Color. The allowed range is between 0
	 *            and 255.
	 * @param b
	 *            the value for the blue Color. The allowed range is between 0
	 *            and 255.
	 * @param a
	 *            the value for the transparency. The allowed range is between 0
	 *            and 100
	 */
	public Pixel(short r, short g, short b, short a) {
		if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255 || a < 0 || a > 100) {
			StringBuilder failure = new StringBuilder("");
			if (r < 0 || r > 255) {
				failure.append("Red ");
			}
			if (g < 0 || g > 255) {
				failure.append("Green ");
			}
			if (b < 0 || b > 255) {
				failure.append("Blue ");
			}
			if (a < 0 || a > 100) {
				failure.append("Alpha ");
			}
			throw new IllegalArgumentException("Color parameter out of range: "
					+ failure.toString());
		}
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}

	/**
	 * Returns the value for the transparency
	 * 
	 * @return the alpha value
	 */
	public short getAlpha() {
		return alpha;
	}

	public short getBlue() {
		return blue;
	}

	public short getGreen() {
		return green;
	}

	public short getRed() {
		return red;
	}

	public void setAlpha(byte a) {
		if (a < 0 || a > 100) {
			throw new IllegalArgumentException("The value is out of range (0-100");
		}
		alpha = a;
	}

	public void setBlue(short b) {
		if (b < 0 || b > 255) {
			throw new IllegalArgumentException("The value is out of range (0-100");
		}
		blue = b;
	}

	private final void setColor(int colorCode) {
		blue = (short) (colorCode & 0xFF);

		colorCode = colorCode >> 8;
		green = (short) (colorCode & 0xFF);

		colorCode = colorCode >> 8;
		red = (short) (colorCode & 0xFF);
	}

	public void setGreen(short g) {
		if (g < 0 || g > 255) {
			throw new IllegalArgumentException("The value is out of range (0-100");
		}
		green = g;
	}

	public void setHTMLColor(int colorCode) {
		setColor(colorCode);

	}

	public void setRed(short r) {
		if (r < 0 || r > 255) {
			throw new IllegalArgumentException("The value is out of range (0-100");
		}
		red = r;
	}
}
