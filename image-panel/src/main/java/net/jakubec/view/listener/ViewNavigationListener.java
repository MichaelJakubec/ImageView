package net.jakubec.view.listener;

import net.jakubec.view.BasicPanel;
import net.jakubec.view.Settings.Settings;
import net.jakubec.view.Settings.SettingsException;
import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.ViewException;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

public class ViewNavigationListener implements ActionListener {
	private final BasicPanel panel;

	public static final String FILE_TYPES = "jpg.gif.png.bmp";

	public ViewNavigationListener(BasicPanel viewPanel) {
		this.panel = viewPanel;
	}


	public void actionPerformed(final ActionEvent event) {
		String eventCode = event.getActionCommand();
		try {
			switch (eventCode) {
				case "open":

					JFileChooser chooser = new JFileChooser(VSettings.loadSetting("current.dir"));
					SwingUtilities.updateComponentTreeUI(chooser);
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

					chooser.showOpenDialog(null);

					try {
						if (chooser.getSelectedFile() != null) {
							panel.openImage(chooser.getSelectedFile());
						}
					} catch (Exception e) {
						//TODO exception handling
						e.printStackTrace();
					}

					break;
				case "print":
					panel.printImage();

					break;
				case "exit":
					try {
						VSettings.saveProps();
					} catch (SettingsException e) {
						//TODO exception handling
						e.printStackTrace();
					}
					System.exit(0);
				case "next": {
					File dirPath = Settings.currentDirectory.load();
					String[] images = dirPath.list();
					if (images == null) {
						return;
					}
					Vector<String> v = new Vector<>();
					for (String image : images) {
						StringTokenizer st = new StringTokenizer(image, ".");
						try {
							st.nextToken();
							if (!st.hasMoreTokens()) {
								continue;
							}
							String shelper = st.nextToken();
							int start = FILE_TYPES.indexOf(shelper.toLowerCase());
							if (start != -1) {
								v.add(image);
							}
						} catch (Exception e) {
							//TODO exception handling
							e.printStackTrace();
						}

					}
					int i = v.indexOf(Settings.currentImage.load().getName());
					try {
						try {
							panel.openImage(new File(dirPath, v.elementAt(i + 1)));
						} catch (ArrayIndexOutOfBoundsException e) {
							if (v.size() != 0) {
								panel.openImage(new File(dirPath, v.firstElement()));
							}
						}
					} catch (ViewException e) {
						//TODO exception handling
						e.printStackTrace();
					}
					break;
				}
				case "previous": {
					File dirPath = Settings.currentDirectory.load();
					String[] images = dirPath.list();
					if (images == null) {
						return;
					}
					Vector<String> v = new Vector<>();
					for (String image : images) {
						StringTokenizer st = new StringTokenizer(image, ".");
						try {
							st.nextToken();
							if (!st.hasMoreTokens()) {
								continue;
							}
							String shelper = st.nextToken();
							int start = FILE_TYPES.indexOf(shelper.toLowerCase());
							if (start != -1) {
								v.add(image);
							}
						} catch (Exception e) {
							//TODO exception handling
							e.printStackTrace();
						}

					}
					int i = v.indexOf(Settings.currentImage.load().getName());
					try {
						try {
							panel.openImage(new File(dirPath, v.elementAt(i - 1)));
						} catch (ArrayIndexOutOfBoundsException e) {
							if (v.size() != 0) {
								panel.openImage(new File(dirPath, v.lastElement()));
							}
						}
					} catch (ViewException e) {
						// TODO exception handling
						e.printStackTrace();
					}
					break;
				}
				case "undo":
					panel.undo();
					break;
				case "zoom+":
					panel.zoomp();
					break;
				case "zoom-":
					panel.zoomm();
					break;
				case "zoom0":
					panel.zoom0();
					break;
				case "zoom1":
					panel.zoom1();
					break;
				case "rot+":
					panel.rotateImage(true);
					break;
				case "rot-":
					panel.rotateImage(false);
					break;
				case "saveAs":
					panel.saveAs();
					break;
				case "save":
					panel.save();
					break;
				case "full":
					panel.fullImage();
					break;
				case "del":
					panel.delete();
					break;
			}
		} catch (RuntimeException | ViewException re) {
			//TODO exception handling
			re.printStackTrace();
		}
	}
}
