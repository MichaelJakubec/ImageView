package net.jakubec.view.edit.tool;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import net.jakubec.view.edit.EditPanel;
import net.jakubec.view.edit.EditPanel.EditMode;
import net.jakubec.view.edit.dialog.ColorChooser;

public class VToolBox extends JInternalFrame {

	private class ColorPanel extends JPanel {
		@Override
		public Dimension getMinimumSize() {
			return new Dimension(100, 100);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(100, 100);
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(Color.black);
			int[] x = new int[] { 3, 67, 67, 96, 96, 35, 35, 3 };
			int[] y = new int[] { 3, 3, 35, 35, 96, 96, 67, 67 };
			g.drawPolygon(x, y, 8);
			// draw Foreground color
			g.setColor(EditPanel.foreground);
			g.fillRect(6, 6, 58, 58);
			// draw Background color
			g.setColor(EditPanel.background);
			x = new int[] { 64, 94, 94, 38, 38, 64 };
			y = new int[] { 38, 38, 93, 93, 64, 64 };
			g.fillPolygon(x, y, 6);

			// Draw the switch fore- background color
			g.setColor(Color.BLACK);
			g.drawLine(14, 75, 14, 88);
			g.drawLine(14, 88, 27, 88);
			x = new int[] { 14, 18, 10 };
			y = new int[] { 75, 82, 82 };
			g.fillPolygon(x, y, 3);
			x = new int[] { 27, 21, 21 };
			y = new int[] { 88, 84, 92 };
			g.fillPolygon(x, y, 3);
		}
	}

	private class ColorSelectionListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getModifiers() != MouseEvent.BUTTON1_MASK) {
				return;
			}
			int x = e.getX();
			int y = e.getY();
			if (x > 6 && x < 64 && y > 3 && y < 64) {

				ColorChooser chooser = new ColorChooser(EditPanel.foreground);
				chooser.setVisible(true);
				EditPanel.foreground = chooser.getSelectedColor();
				repaint();
			} else if (x > 38 && x < 94 && y > 38 && y < 94) {
				ColorChooser chooser = new ColorChooser(EditPanel.foreground);
				chooser.setVisible(true);
				EditPanel.background = chooser.getSelectedColor();
				repaint();
			} else if (x > 9 && x < 28 && y > 74 && y < 93) {
				Color temp = EditPanel.background;
				EditPanel.background = EditPanel.foreground;
				EditPanel.foreground = temp;

				repaint();
			}

		}
	}

	ButtonGroup bt = new ButtonGroup();

	Container cp;

	public VToolBox(final EditPanel pa) {
		super("", false, false, false, false);
		JPanel panel = new JPanel();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setSize(100 + getInsets().left + getInsets().right, 400);

		cp = getContentPane();
		panel.setLayout(new GridLayout(7, 2));
		JToggleButton b = new JToggleButton("L");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pa.setEditMode(EditPanel.EditMode.LINE);
			}
		});
		bt.add(b);
		panel.add(b);

		b = new JToggleButton("M");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pa.setEditMode(EditPanel.EditMode.MOVE);
			}
		});
		bt.add(b);
		panel.add(b);

		cp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		cp.add(panel, c);
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		JPanel colorPanel = new ColorPanel();
		colorPanel.addMouseListener(new ColorSelectionListener());
		cp.add(colorPanel, c);

		setVisible(true);

	}
}
