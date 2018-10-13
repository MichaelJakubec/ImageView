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

package net.jakubec.view.dia;

import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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


import net.jakubec.view.ViewException;


import net.jakubec.view.ViewPanel;
import net.jakubec.view.properties.VProperties;

public class Diashow extends JPanel implements ActionListener,
		ListSelectionListener {
	/**
	 * Comperator for the Files. Which compares two Files.
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

	private final ViewPanel picturePanel = new ViewPanel();
	private Window frame;
	private File path;
	private final JList<File> filelist;
	private final JList<File> showfiles;
	private final DefaultListModel<File> listmodel;
	private final DefaultListModel<File> showmodel;
	private int[] selectedindex;
	private final ButtonGroup group = new ButtonGroup();
	private final JTextField autotime  = new JTextField("5");
	private final JTextField zufalltime  = new JTextField("5");
	private final JTextField dirPath = new JTextField();
	private final JCheckBox delRandom = new JCheckBox(VProperties.getValue("dia.delrand"));


	private Diashow(File path) {
		super();
		setBackground(Color.LIGHT_GRAY);

		showmodel = new DefaultListModel<>();
		showfiles = new JList<>(showmodel);

		listmodel = new DefaultListModel<>();
		filelist = new JList<>(listmodel);

		this.setLayout(new GridBagLayout());
		JPanel left = getLeftPanel();
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		this.add(left, c);



		JScrollPane showPane = new JScrollPane(showfiles);
		showfiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		showfiles.addListSelectionListener(this);

		c.gridx = 2;
		this.add(showPane,c );

		c.gridx = 1;
		c.weightx = 0;
		this.add(getCenterPanel(), c);



		this.path = path;
		this.dirPath.setText(path.getAbsolutePath());
		setVisible(true);


	}

	private JPanel getCenterPanel() {
		JPanel center = new JPanel(new GridBagLayout());

		GridBagConstraints  c = new GridBagConstraints();
		JButton bt = new JButton(VProperties.getValue("dia.add"));
		bt.setActionCommand("add");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx= 0.5;
		center.add(bt, c);

		bt = new JButton(VProperties.getValue("dia.remove"));
		bt.setActionCommand("remove");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 2;
		c.gridy = 0;
		center.add(bt, c);


		bt = new JButton(VProperties.getValue("dia.add_all"));
		bt.setActionCommand("add_all");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 1;
		center.add(bt, c);


		bt = new JButton(VProperties.getValue("dia.remove_all"));
		bt.setActionCommand("remove_all");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.LINE_END;
		c.gridx = 2;
		c.gridy = 1;
		center.add(bt, c);






		bt = new JButton(VProperties.getValue("dia.start"));
		bt.setActionCommand("start");
		bt.addActionListener(this);
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 0;
		center.add(bt, c);

		JPanel radioPanel = generateAutomationPanel();
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 4;
		center.add(radioPanel, c);


		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		center.add(delRandom, c);
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 6;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weightx = 1;
		c.weighty = 1;
		center.add(picturePanel, c);
		picturePanel.setBackground(Color.black);
		picturePanel.setForeground(Color.black);

		return center;
	}

	private JPanel generateAutomationPanel() {
		GridBagConstraints c = new GridBagConstraints();
		JPanel radioPanel = new JPanel(new GridBagLayout());
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 3;
		c.gridy = 4;
		c.gridwidth = 4;
		c.gridheight = 5;
		c.weightx = 0;
		c.weighty = 0;


		JRadioButton radio = new JRadioButton(VProperties.getValue("dia.auto_mouse"));

		radio.setActionCommand("automaticMouse");
		group.add(radio);

		c.anchor = GridBagConstraints.LINE_START;
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
		JLabel label = new JLabel("sec.");

		radioPanel.add(label, c);

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

		label = new JLabel("sec.");
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		radioPanel.add(label, c);
		return radioPanel;
	}

	private JPanel getLeftPanel() {
		JPanel left = new JPanel(new GridBagLayout());


		JButton bt = new JButton(VProperties.getValue("dia.search"));
		bt.setActionCommand("search");
		JLabel label = new JLabel("Suchen in:");

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
		left.add(label, c);

		bt.addActionListener(this);


		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 0;
		left.add(bt, c);

		c.anchor = GridBagConstraints.LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx =1;

		dirPath.setEditable(false);
		left.add(dirPath, c);

		filelist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		filelist.setSelectedIndex(1);
		filelist.setCellRenderer(new FileListRenderer());
		filelist.addListSelectionListener(this);
		filelist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Diashow.this.mouseClicked(e);
			}
		});

		JScrollPane scrollPane = new JScrollPane(filelist);

		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weighty = 1;

		left.add(scrollPane, c);
		return left;
	}

	public Diashow(final Window imageView, File path) {
		this(path);
		frame = imageView;
		openDir();

	}

	public void actionPerformed(final ActionEvent event) {

		switch (event.getActionCommand()) {
			case "search":
				JFileChooser chooser = new JFileChooser(path);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int selected = chooser.showOpenDialog(this);

				switch (selected) {
					case JFileChooser.CANCEL_OPTION:
					case JFileChooser.ERROR_OPTION:
						return;
				}
				path = chooser.getSelectedFile();
				dirPath.setText(path.getAbsolutePath());
				openDir();

				break;
			case "add": {
				List<File> showfiles = filelist.getSelectedValuesList();
				for (File showfile : showfiles) {
					showmodel.addElement(showfile);
				}
				break;
			}
			case "add_all": {
				filelist.clearSelection();
				File[] showfiles = new File[listmodel.size()];
				listmodel.copyInto(showfiles);
				for (File showfile : showfiles) {
					showmodel.addElement(showfile);
				}
				break;
			}
			case "remove_all":
				showmodel.clear();

				break;
			case "remove":
				int[] index = showfiles.getSelectedIndices();
				for (int i = index.length - 1; i >= 0; i--) {

					showmodel.removeElementAt(index[i]);
				}
				break;
			case "start":
				String helper = group.getSelection().getActionCommand();
				File[] files = new File[showmodel.size()];
				showmodel.copyInto(files);
				ArrayList<File> list = new ArrayList<>(Arrays.asList(files));
				switch (helper) {
					case "automaticMouse":
						System.out.println("Automatisch Mouse");
						new Diapresentation(frame, list, false, false, -1, null);
						break;
					case "automatischr": {
						int time;
						try {
							time = Integer.parseInt(autotime.getText());
						} catch (Exception e) {
							time = 5;
						}
						new Diapresentation(frame, list, false, false, time, null);
						break;
					}
					case "zufallm":

						new Diapresentation(frame, list, true, delRandom.isSelected(), -1, null);
						break;
					case "zufallr": {

						int time;
						try {
							time = Integer.parseInt(zufalltime.getText());
						} catch (Exception e) {
							time = 5;
						}
						new Diapresentation(frame, list, true, delRandom.isSelected(), time, null);

						break;
					}
				}
				break;
		}
	}


	private void mouseClicked(final MouseEvent event) {
		if (event.getSource().equals(filelist)) {
			if (event.getClickCount() == 2) {
				filelist.setSelectedIndices(selectedindex);
				List<File> showfiles = filelist.getSelectedValuesList();
				for (File showfile : showfiles) {
					showmodel.addElement(showfile);
				}
			} else if (event.getClickCount() == 1) {
				System.out.println("Image selected");
				// selectedindex = filelist.getSelectedIndices();
				// drawPicture();
			}
		} else if (event.getSource().equals(this.showfiles)) {
			if (event.getClickCount() == 2) {
				List<File> selected = showfiles.getSelectedValuesList();

				for (Object o : selected) {
					this.showmodel.removeElement(o);
				}
			}
		}

	}


	private void openDir() {

		File[] files = path.listFiles();
		if (files == null) {
			return;
		}
		Arrays.sort(files, new FileComparator<>());
		listmodel.removeAllElements();
		for (File file : files) {
			listmodel.addElement(file);
		}
	}



	@Override
	public void valueChanged(final ListSelectionEvent event) {
		if (event.getSource().equals(filelist)) {
			selectedindex = filelist.getSelectedIndices();
			if (selectedindex != null && selectedindex.length> 0) {
				File f = listmodel.getElementAt(selectedindex[0]);
				try {
					this.picturePanel.openImage(f);
				} catch (ViewException e) {
					//TODO exception handling
					e.printStackTrace();
				}
			}
		}
	}
}
