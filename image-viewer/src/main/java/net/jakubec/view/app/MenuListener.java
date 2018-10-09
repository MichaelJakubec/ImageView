package net.jakubec.view.app;

import net.jakubec.view.BasicPanel;
import net.jakubec.view.ViewException;
import net.jakubec.view.app.settings.Settings;
import net.jakubec.view.app.settings.SettingsDialog;
import net.jakubec.view.app.settings.SettingsException;
import net.jakubec.view.app.settings.VSettings;
import net.jakubec.view.listener.ViewNavigationListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

class MenuListener extends ViewNavigationListener {
	private final BasicPanel panel;

	private static final String FILE_TYPES = "jpg.gif.png.bmp";

	MenuListener(final BasicPanel panel) {
		super(panel);
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String eventCode = e.getActionCommand();

		if (eventCode.equals("dia")) {
			Application.getMainWindow().showDiashowMode();
		} else if (eventCode.equals("mike")) {
			Application.getMainWindow().showEditMode();
		} else if (eventCode.equals("settings")) {
			new SettingsDialog(Application.getMainWindow(), "Settings", true);
		} else if (eventCode.startsWith("plug-")) {
			Application.getMainWindow().startPlugin(eventCode.substring(5));
		} else if (eventCode.equals("help")) {
			new AboutDialog();
		} else if (eventCode.equals("open")) {
			JFileChooser chooser = new JFileChooser(Settings.currentDirectory.load());
			SwingUtilities.updateComponentTreeUI(chooser);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			chooser.showOpenDialog(null);

			try {
				if (chooser.getSelectedFile() != null) {
					panel.openImage(chooser.getSelectedFile());
				}
			} catch (Exception ex) {
				//TODO exception handling
				ex.printStackTrace();
			}
		} else if (eventCode.equals("exit")) {

			try {
				VSettings.saveProps();
			} catch (SettingsException ex) {
				//TODO exception handling
				ex.printStackTrace();
			}
			System.exit(0);
		} else if (eventCode.equals("next")) {

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
				} catch (Exception ex) {
					//TODO exception handling
					ex.printStackTrace();
				}

			}
			int i = v.indexOf(Settings.currentImage.load().getName());
			try {
				try {
					panel.openImage(new File(dirPath, v.elementAt(i + 1)));
				} catch (ArrayIndexOutOfBoundsException ex) {
					if (v.size() != 0) {
						panel.openImage(new File(dirPath, v.firstElement()));
					}
				}
			} catch (ViewException ex) {
				//TODO exception handling
				ex.printStackTrace();
			}
		} else if (eventCode.equals("previous")) {
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
				} catch (Exception ex) {
					//TODO exception handling
					ex.printStackTrace();
				}

			}
			int i = v.indexOf(Settings.currentImage.load().getName());
			try {
				try {
					panel.openImage(new File(dirPath, v.elementAt(i - 1)));
				} catch (ArrayIndexOutOfBoundsException ex) {
					if (v.size() != 0) {
						panel.openImage(new File(dirPath, v.lastElement()));
					}
				}
			} catch (ViewException ex) {
				// TODO exception handling
				ex.printStackTrace();
			}


		} else {
			super.actionPerformed(e);
		}
	}


}
