package net.jakubec.view.dia;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.jakubec.view.Application;
import net.jakubec.view.BasicPanel;
import net.jakubec.view.ImageView;
import net.jakubec.view.Settings.Settings;
import net.jakubec.view.properties.VProperties;

public class Diashow extends JPanel implements ActionListener, MouseListener,
		ListSelectionListener, BasicPanel {
	/**
	 * Comperator for the Files. Which compares two Files.
	 * 
	 * @author amunra
	 * 
	 * @param <F>
	 */
	private class FileComparator<F extends File> implements Comparator<F> {

		@Override
		public int compare(final F a, final F b) {
			if (a.isDirectory()) {
				if (b.isDirectory()) return compareFiles(a, b);
				else
					return -1;
			} else {
				if (b.isDirectory()) return 1;
				else
					return compareFiles(a, b);
			}

		}

		private int compareFiles(final F a, final F b) {
			return a.compareTo(b);
		}

	}

	private class FileListRenderer extends DefaultListCellRenderer {
		final ImageIcon longIcon = new ImageIcon("folderSmall.gif");

		@Override
		public Component getListCellRendererComponent(final JList list, final Object value,
				final int index, final boolean isSelected, final boolean cellHasFocus) {
			String text;

			JLabel label = new JLabel();
			if (value instanceof File) {
				text = ((File) value).getName();
				if (((File) value).isDirectory()) {
					label.setIcon(longIcon);
				}

			} else {
				text = value.toString();
			}
			label.setText(text);

			if (isSelected) {
				label.setBackground(list.getSelectionBackground());
				label.setForeground(list.getSelectionForeground());
			} else {
				label.setBackground(Color.white);
				label.setForeground(list.getForeground());
			}
			label.setEnabled(list.isEnabled());
			label.setFont(list.getFont());
			label.setOpaque(true);

			return label;
		}

	}

	private JButton bt;

	private final JPanel picturePanel;
	private ImageView frame;
	private Label label;
	private File path;
	private final JList filelist;
	private final JList showfiles;
	private final DefaultListModel listmodel;
	private final DefaultListModel showmodel;
	private int[] selectedindex;
	private final ButtonGroup group;
	private JRadioButton radio;
	private final JTextField autotime;
	private final JTextField zufalltime;
	private final JTextField dirPath;
	private final JCheckBox delRandom;
	private BufferedImage bgBuffer;
	private Thread th;

	private final boolean redraw = false;

	Diashow() {
		super();
		// setSize(800, 600);
		setBackground(Color.LIGHT_GRAY);
		picturePanel = new JPanel() {
			@Override
			protected void paintComponent(final Graphics g) {
				if (bgBuffer != null) {
					g.drawImage(bgBuffer, 0, 0, null);
				} else {
					super.paintComponent(g);
				}
			}
		};
		// addWindowListener(new WindowClosingAdapter(true));

		bt = new JButton(VProperties.getValue("dia.search"));
		bt.setActionCommand("search");
		label = new Label("Suchen in:");
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 0, 5);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(label, c);

		bt.addActionListener(this);
		// bt.setBounds(70, 10, 100, 20);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(bt, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;

		dirPath = new JTextField();
		dirPath.setEditable(false);
		add(dirPath, c);
		listmodel = new DefaultListModel();

		filelist = new JList(this.listmodel);
		filelist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		filelist.setSelectedIndex(1);
		filelist.setCellRenderer(new FileListRenderer());
		filelist.addListSelectionListener(this);
		filelist.addMouseListener(this);

		JScrollPane scrollPane = new JScrollPane(filelist);
		scrollPane.setPreferredSize(new Dimension(Application.getMainWindow().getWidth() / 3,
				filelist.getPreferredSize().height));

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 0;
		c.weighty = 1;

		add(scrollPane, c);
		bt = new JButton(VProperties.getValue("dia.add"));
		bt.setActionCommand("add");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(bt, c);

		bt = new JButton(VProperties.getValue("dia.add_all"));
		bt.setActionCommand("add_all");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(bt, c);

		bt = new JButton(VProperties.getValue("dia.remove"));
		bt.setActionCommand("remove");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(bt, c);
		bt = new JButton(VProperties.getValue("dia.remove_all"));
		bt.setActionCommand("remove_all");

		bt.addActionListener(this);
		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 3;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(bt, c);
		showmodel = new DefaultListModel();
		showfiles = new JList(showmodel);

		JScrollPane showPane = new JScrollPane(showfiles);
		showPane.setPreferredSize(new Dimension(Application.getMainWindow().getWidth() / 3,
				filelist.getPreferredSize().height));
		showfiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		showfiles.addListSelectionListener(this);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 7;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 0.5;
		c.weighty = 0;

		add(showPane, c);

		bt = new JButton(VProperties.getValue("dia.start"));
		bt.setActionCommand("start");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(bt, c);

		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridBagLayout());
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 3;
		c.gridy = 5;
		c.gridwidth = 4;
		c.gridheight = 5;
		c.weightx = 0;
		c.weighty = 0;
		add(radioPanel, c);

		group = new ButtonGroup();
		radio = new JRadioButton(VProperties.getValue("dia.auto_mouse"));

		radio.setActionCommand("automaticMouse");
		group.add(radio);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		radioPanel.add(radio, c);

		radio = new JRadioButton(VProperties.getValue("dia.auto"), true);
		radio.setActionCommand("automatischr");
		group.add(radio);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		radioPanel.add(radio, c);
		radio = new JRadioButton(VProperties.getValue("dia.random_mouse"));
		radio.setActionCommand("zufallm");
		group.add(radio);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		radioPanel.add(radio, c);
		radio = new JRadioButton(VProperties.getValue("dia.random"));
		radio.setActionCommand("zufallr");
		group.add(radio);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		radioPanel.add(radio, c);

		autotime = new JTextField("5");
		autotime.setHorizontalAlignment(JTextField.RIGHT);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		radioPanel.add(autotime, c);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		label = new Label("sec.");

		radioPanel.add(label, c);

		zufalltime = new JTextField("5");
		zufalltime.setHorizontalAlignment(JTextField.RIGHT);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		radioPanel.add(zufalltime, c);

		label = new Label("sec.");
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		radioPanel.add(label, c);

		delRandom = new JCheckBox(VProperties.getValue("dia.delrand"));
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 10;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		add(delRandom, c);
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 3;
		c.gridy = 11;
		c.gridwidth = 4;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 0;
		c.weighty = 0;
		add(picturePanel, c);
		picturePanel.setBackground(Color.black);
		picturePanel.setForeground(Color.black);
		path = Settings.diaDirectory.load();
		this.dirPath.setText(path.getAbsolutePath());
		setVisible(true);

		System.out.println("Diashow");
	}

	public Diashow(final ImageView imageView) {
		this();
		path = Settings.diaDirectory.load();
		frame = imageView;
		openDir();

	}

	public void actionPerformed(final ActionEvent event) {

		if (event.getActionCommand().equals("search")) {
			JFileChooser chooser = new JFileChooser(path);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int selected = chooser.showOpenDialog(this);

			switch (selected) {
				case JFileChooser.CANCEL_OPTION:
				case JFileChooser.ERROR_OPTION:
					return;
				default:
					// Nothing todo
			}
			path = chooser.getSelectedFile();
			dirPath.setText(path.getAbsolutePath());
			Settings.diaDirectory.save(path);

			// VSettings.saveStringSetting("dia.dir", path.getAbsolutePath());
			openDir();

		} else if (event.getActionCommand().equals("add")) {
			Object[] showfiles = filelist.getSelectedValues();
			for (int i = 0; i < showfiles.length; i++) {
				showmodel.addElement(showfiles[i]);
			}
		} else if (event.getActionCommand().equals("add_all")) {
			filelist.clearSelection();
			File[] showfiles = new File[listmodel.size()];
			listmodel.copyInto(showfiles);
			for (int i = 0; i < showfiles.length; i++) {
				showmodel.addElement(showfiles[i]);
			}
		} else if (event.getActionCommand().equals("remove_all")) {
			showmodel.clear();

		} else if (event.getActionCommand().equals("remove")) {
			int[] index = showfiles.getSelectedIndices();
			for (int i = index.length - 1; i >= 0; i--) {

				showmodel.removeElementAt(index[i]);
			}
		} else if (event.getActionCommand().equals("start")) {
			String helper = group.getSelection().getActionCommand();
			Object[] files = new Object[showmodel.size()];
			showmodel.copyInto(files);
			ArrayList<Object> list = new ArrayList<Object>();
			for (int i = 0; i < files.length; i++) {
				list.add(files[i]);
			}
			if (helper.equals("automaticMouse")) {
				System.out.println("Automatisch Mouse");
				new Diapresentation(frame, list, false, false, -1);
			} else if (helper.equals("automatischr")) {
				int time;
				try {
					time = Integer.parseInt(autotime.getText());
				} catch (Exception e) {
					time = 5;
				}
				new Diapresentation(frame, list, false, false, time);
			} else if (helper.equals("zufallm")) {

				new Diapresentation(frame, list, true, delRandom.isSelected(), -1);
			} else if (helper.equals("zufallr")) {

				int time;
				try {
					time = Integer.parseInt(zufalltime.getText());
				} catch (Exception e) {
					time = 5;
				}
				new Diapresentation(frame, list, true, delRandom.isSelected(), time);

			}
			Application.getMainWindow().setVisible(false);
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	private void drawPicture() {

		th = new Thread() {
			@Override
			public void run() {
				BufferedImage temp;
				try {
					filelist.setSelectedIndex(selectedindex[selectedindex.length - 1]);
					Object obj = filelist.getSelectedValue();
					File img;
					if (obj instanceof File) {
						img = (File) obj;
					} else
						return;

					temp = ImageIO.read(img);
					if (temp == null) throw new Exception("");
				} catch (Exception e) {

					int width = picturePanel.getWidth();
					int height = picturePanel.getHeight();
					temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					Graphics g = temp.getGraphics();

					g.setColor(Color.white);
					g.drawString("No Image", 0, height / 2);

				}
				int windowX = picturePanel.getWidth();
				int windowY = picturePanel.getHeight();
				int pictX = temp.getWidth();
				int pictY = temp.getHeight();
				int newHeight = windowY;
				int newWidth = (int) ((double) windowY * (double) pictX / pictY);

				if (newWidth > windowX) {
					newWidth = windowX;
					newHeight = (int) ((double) pictY * (double) windowX / pictX);
				}

				bgBuffer = new BufferedImage(windowX, windowY, temp.getType());

				Graphics g = bgBuffer.getGraphics();
				g.setColor(Color.gray);
				g.fillRect(0, 0, windowX, windowY);
				g.drawImage(temp, (windowX - newWidth) / 2, (windowY - newHeight) / 2, newWidth,
						newHeight, null);
				picturePanel.repaint();

			}
		};
		th.start();

	}

	@Override
	public void fullImage() {
		// TODO Auto-generated method stub

	}

	@Override
	public Container getPanel() {

		return this;
	}

	public void mouseClicked(final MouseEvent event) {
		if (event.getSource().equals(filelist)) {
			if (event.getClickCount() == 2) {
				filelist.setSelectedIndices(selectedindex);
				Object[] showfiles = filelist.getSelectedValues();
				for (int i = 0; i < showfiles.length; i++) {
					showmodel.addElement(showfiles[i]);
				}
			} else if (event.getClickCount() == 1) {
				// selectedindex = filelist.getSelectedIndices();
				// drawPicture();
			}
		} else if (event.getSource().equals(this.showfiles)) {
			if (event.getClickCount() == 2) {
				Object[] selected = showfiles.getSelectedValues();

				for (Object o : selected) {
					this.showmodel.removeElement(o);
				}
			}
		}

	}

	public void mouseEntered(final MouseEvent event) {

	}

	public void mouseExited(final MouseEvent event) {

	}

	public void mousePressed(final MouseEvent event) {

	}

	public void mouseReleased(final MouseEvent event) {

	}

	private void openDir() {

		File[] files = path.listFiles();
		Arrays.sort(files, new FileComparator<File>());
		listmodel.removeAllElements();
		for (int i = 0; i < files.length; i++) {
			listmodel.addElement(files[i]);
		}
	}

	@Override
	public void openImage(final File selected) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printImage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rotateImage(final boolean clockwise) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setImage(final BufferedImage img) {
		// TODO Auto-generated method stub

	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void valueChanged(final ListSelectionEvent event) {
		if (event.getSource().equals(filelist)) {
			selectedindex = filelist.getSelectedIndices();
			drawPicture();
		}
	}

	@Override
	public void zoom0() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoom1() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomm() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zoomp() {
		// TODO Auto-generated method stub

	}
}
