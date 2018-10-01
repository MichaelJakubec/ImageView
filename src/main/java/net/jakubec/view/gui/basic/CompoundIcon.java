package net.jakubec.view.gui.basic;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import com.sun.java.swing.Painter;

/**
 * This class is used to combine two icons for a RadioButton.
 * 
 * @author amunra
 * 
 */
public class CompoundIcon implements Icon {

	// public static void main(final String[] args) {
	//
	// try {
	// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	// //
	// UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
	// } catch (ClassNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (InstantiationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (UnsupportedLookAndFeelException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// JFrame frame = new JFrame();
	//
	// JRadioButton bt = new JRadioButton("HELO");
	// Icon ico = UIManager.getIcon("RadioButton.icon");
	// bt.setIcon(new CompoundIcon(ico, new ImageIcon("D:\\Jakubec\\jira.gif"),
	// 3));
	// frame.getContentPane().setLayout(new FlowLayout());
	// frame.getContentPane().add(bt);
	// bt = new JRadioButton("HELO");
	// bt.setIcon(new CompoundIcon(ico, new ImageIcon("D:\\Jakubec\\jira.gif"),
	// 3));
	// bt.setEnabled(true);
	// bt.setSelected(true);
	// frame.getContentPane().add(bt);
	// bt = new JRadioButton("HELO");
	// bt.setIcon(new CompoundIcon(ico, new ImageIcon("D:\\Jakubec\\jira.gif"),
	// 3));
	// bt.setEnabled(false);
	// bt.setSelected(true);
	// frame.getContentPane().add(bt);
	// bt = new JRadioButton("HELO");
	// bt.setIcon(new CompoundIcon(ico, new ImageIcon("D:\\Jakubec\\jira.gif"),
	// 3));
	// bt.setEnabled(false);
	// bt.setSelected(false);
	// frame.getContentPane().add(bt);
	// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	// frame.setSize(600, 600);
	// frame.setVisible(true);
	//
	// }

	private final Icon left, right;

	private final int gap;

	/**
	 * creates a new CompoundIcon with the two given Icons and the divined gap.
	 * The gap defines the distance between the two icons
	 * 
	 * @param left
	 *            the left Icon
	 * @param right
	 *            the right Icon
	 * @param gap
	 *            the gap between the two icons
	 */
	public CompoundIcon(final Icon left, final Icon right, final int gap) {
		if (left == null || right == null) throw new NullPointerException();
		this.left = left;
		this.right = right;
		this.gap = gap;

	}

	/**
	 * returns the height of this compound Icon. The Height is the Maximum
	 * height if the two icon +2
	 * 
	 * @return the Height of the Icon
	 */
	public int getIconHeight() {
		return Math.max(left.getIconHeight(), right.getIconHeight()) + 2;
	}

	/**
	 * The width of the icon. The width is the sum of the width of the two Icons
	 * plus the gap
	 */
	public int getIconWidth() {
		return left.getIconWidth() + gap + right.getIconWidth();
	}

	public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
		int h = getIconHeight();
		// System.out.println("|" + left.getClass() + "|");
		if (left.getClass().toString().equals("class com.sun.java.swing.plaf.nimbus.NimbusIcon")
				&& UIManager.getLookAndFeel().getClass()
						.equals(com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel.class)) {
			int y2 = y + (h - left.getIconHeight()) / 2;
			Painter painter;
			JRadioButton bt = (JRadioButton) c;
			if (bt.isFocusPainted()) {
				if (bt.isEnabled()) {
					if (bt.isSelected()) {
						painter = (Painter) UIManager.get("RadioButton[Selected].iconPainter");
					} else {
						painter = (Painter) UIManager.get("RadioButton[Enabled].iconPainter");
					}
				} else {
					if (bt.isSelected()) {
						painter = (Painter) UIManager
								.get("RadioButton[Disabled+Selected].iconPainter");
					} else {
						painter = (Painter) UIManager.get("RadioButton[Disabled].iconPainter");
					}
				}
			} else {
				if (bt.isEnabled()) {
					if (bt.isSelected()) {
						painter = (Painter) UIManager.get("RadioButton[Selected].iconPainter");
					} else {
						painter = (Painter) UIManager.get("RadioButton[Enabled].iconPainter");
					}
				} else {
					if (bt.isSelected()) {
						painter = (Painter) UIManager
								.get("RadioButton[Disabled+Selected].iconPainter");
					} else {
						painter = (Painter) UIManager.get("RadioButton[Disabled].iconPainter");
					}
				}
			}
			if (painter != null) {
				JComponent jc = c instanceof JComponent ? (JComponent) c : null;
				Graphics2D gfx = (Graphics2D) g;
				gfx.translate(x, y2);
				painter.paint(gfx, jc, left.getIconWidth(), left.getIconHeight());
				gfx.translate(-x, -y2);
			}

		} else {
			left.paintIcon(c, g, x, y + (h - left.getIconHeight()) / 2);
		}
		right.paintIcon(c, g, x + left.getIconWidth() + gap, y + (h - right.getIconHeight()) / 2);

	}
}

/*
 * RadioButton[Enabled].iconPainter
 * RadioButton[Focused+MouseOver+Selected].iconPainter
 * RadioButton[Focused+MouseOver].iconPainter
 * RadioButton[Focused+Pressed+Selected].iconPainter
 * RadioButton[Focused+Pressed].iconPainter
 * RadioButton[Focused+Selected].iconPainter RadioButton[Focused].iconPainter
 * RadioButton[MouseOver+Selected].iconPainter
 * RadioButton[MouseOver].iconPainter RadioButton[Pressed+Selected].iconPainter
 * RadioButton[Pressed].iconPainter RadioButton[Selected].iconPainter
 */
