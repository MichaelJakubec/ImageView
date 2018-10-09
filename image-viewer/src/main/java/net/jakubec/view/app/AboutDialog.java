package net.jakubec.view.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.properties.VProperties;

/**
 * This class is the info dialog for the Program
 * 
 * @author amunra
 * 
 */
class AboutDialog extends JDialog {
	/**
	 * This is the inner class displaying the Information of the Computer
	 * 
	 * @author amunra
	 * 
	 */
	private static class InfoPanel extends JPanel implements MouseMotionListener {
		Image img;
		private boolean above = false;
		private int hpwidth;

		public InfoPanel() {
			super();
			this.addMouseMotionListener(this);
			this.setBackground(Color.white);
			img = null;
			Thread t = new Thread(() -> {
				try {
					img = ImageIO.read(new File(VSettings.rootDir, "Logo2.gif"));

				} catch (Exception e) {

				}
			});
			t.start();

		}

		@Override
		public void mouseDragged(final MouseEvent e) {

		}

		@Override
		public void mouseMoved(final MouseEvent e) {
			int y = e.getY();
			int x = e.getX();
			if (y > 85 && y < 103) {
				if (x > (this.getWidth() - hpwidth) / 2 && x < (this.getWidth() + hpwidth) / 2) {
					if (!above) {
						above = true;
						repaint();
					}
					return;
				}
			}
			if (above) {
				above = false;
				repaint();
			}

		}

		@Override
		public void paint(final Graphics g) {
			super.paint(g);
			if (img != null) {
				g.drawImage(img, (this.getWidth() - img.getWidth(null)) / 2,
						(this.getHeight() - img.getHeight(null)) / 2, this);
			}
			Font f = new Font("sans", Font.BOLD, 26);
			g.setFont(f);
			g.setColor(new Color(0x33, 0x33, 0x99));
			FontMetrics fm = getFontMetrics(f);
			String txt = "ImageView Version " + VSettings.PROG_VERSION;
			int width = fm.stringWidth(txt);
			g.drawString(txt, (this.getWidth() - width) / 2, 40);
			f = new Font("sans", Font.BOLD, 16);
			g.setFont(f);
			fm = getFontMetrics(f);
			g.setColor(new Color(0x00, 0x00, 0));
			txt = VProperties.getValue("help.info");
			width = fm.stringWidth(txt);
			g.drawString(txt, (this.getWidth() - width) / 2, 80);
			g.setColor(Color.blue);
			txt = VSettings.PROG_HOMEPAGE;
			width = fm.stringWidth(txt);
			hpwidth = width;
			g.drawString(txt, (this.getWidth() - width) / 2, 100);
			if (above) {
				g.drawLine((this.getWidth() - width) / 2, 103, (this.getWidth() + width) / 2, 103);
			}

		}
	}

	Container cp;

	public AboutDialog() {
		super(Application.getMainWindow(), VProperties.getValue("menu.help.about"), true);
		this.setSize(500, 400);
		JPanel p = new InfoPanel();
		cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		this.setBackground(Color.white);
		JPanel btPanel = new JPanel();
		JButton b = new JButton(VProperties.getValue("button.ok"));
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		});
		btPanel.add(b);
		btPanel.setBackground(Color.WHITE);
		cp.add(btPanel, BorderLayout.SOUTH);
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Info", new InfoPanel());

		JTextArea license = new JTextArea();
		license.setWrapStyleWord(true);
		license.setLineWrap(true);
		license.setText(License.getText());
		license.setEditable(false);
		license.setCaretPosition(0);
		JScrollPane scrollPane = new JScrollPane(license, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setAutoscrolls(false);

		tabs.add(VProperties.getValue("help.license"), scrollPane);
		cp.add(tabs, BorderLayout.CENTER);

		setVisible(true);

	}
}
