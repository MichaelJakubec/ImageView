package net.jakubec.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import net.jakubec.view.Application;
import net.jakubec.view.BasicPanel;
import net.jakubec.view.ImageView;
import net.jakubec.view.Settings.Settings;
import net.jakubec.view.Settings.SettingsDialog;
import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.exception.VExceptionHandler;
import net.jakubec.view.help.AboutDialog;

public class MenuListener implements ActionListener {
	private BasicPanel panel;

	public MenuListener(final BasicPanel panel) {
		this.panel = panel;
	}

	public void actionPerformed(final ActionEvent event) {
		String eventCode = event.getActionCommand();
		try {
			if (eventCode.equals("open")) {

				JFileChooser chooser = new JFileChooser(VSettings.loadSetting("current.dir"));
				SwingUtilities.updateComponentTreeUI(chooser);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				chooser.showOpenDialog(Application.getMainWindow());

				try {
					if (chooser.getSelectedFile() != null) {
						panel.openImage(chooser.getSelectedFile());
					}
				} catch (Exception e) {
					VExceptionHandler.raiseException(e);
				}

			} else if (eventCode.equals("print")) {
				panel.printImage();

			} else if (eventCode.equals("exit")) {
				VSettings.saveProps();
				System.exit(0);
			} else if (eventCode.equals("dia")) {
				Application.getMainWindow().showDiashowMode();
			} else if (eventCode.equals("next")) {
				File dirPath = Settings.currentDirectory.load();
				String[] images = dirPath.list();
				if (images == null ) {
					return;
				}
				Vector<String> v = new Vector<>();
				for (String image : images) {
					StringTokenizer st = new StringTokenizer(image, ".");
					try {
						st.nextToken();
						if (!st.hasMoreTokens()){
							continue;
						}
						String shelper = st.nextToken();
						int start = ImageView.FILETYPES.indexOf(shelper.toLowerCase());
						if (start != -1) {
							v.add(image);
						}
					} catch (Exception e) {
						VExceptionHandler.raiseException(e);
					}

				}
				int i = v.indexOf(Settings.currentImage.load().getName());
				try {
					panel.openImage(new File(dirPath, v.elementAt(i + 1)));
				} catch (ArrayIndexOutOfBoundsException e) {
					if (v.size() != 0) {
						panel.openImage(new File(dirPath, v.firstElement()));
					}
				}
			} else if (eventCode.equals("previous")) {
				File dirPath = Settings.currentDirectory.load();
				String[] images = dirPath.list();
				if (images == null ) {
					return;
				}
				Vector<String> v = new Vector<>();
				for (String image : images) {
					StringTokenizer st = new StringTokenizer(image, ".");
					try {
						st.nextToken();
						String shelper = st.nextToken();
						int start = ImageView.FILETYPES.indexOf(shelper.toLowerCase());
						if (start != -1) {
							v.add(image);
						}
					} catch (Exception e) {
						VExceptionHandler.raiseException(e);
					}

				}
				int i = v.indexOf(Settings.currentImage.load().getName());
				try {
					panel.openImage(new File(dirPath, v.elementAt(i - 1)));
				} catch (ArrayIndexOutOfBoundsException e) {
					if (v.size() != 0) {
						panel.openImage(new File(dirPath, v.lastElement()));
					}
				}
			} else if (eventCode.equals("undo")) {
				panel.undo();
			} else if (eventCode.equals("zoom+")) {
				panel.zoomp();
			} else if (eventCode.equals("zoom-")) {
				panel.zoomm();
			} else if (eventCode.equals("zoom0")) {
				panel.zoom0();
			} else if (eventCode.equals("zoom1")) {
				panel.zoom1();
			} else if (eventCode.equals("rot+")) {
				panel.rotateImage(true);
			} else if (eventCode.equals("rot-")) {
				panel.rotateImage(false);
			} else if (eventCode.equals("saveAs")) {
				panel.saveAs();
				// try {
				// // File save = ImageSaver.saveAs(origin, VSettings
				// // .loadSetting("current.dir"));
				// // if (save != null) {
				// // openImage(save);
				// // }
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			} else if (eventCode.equals("save")) {
				panel.save();
				// try {
				// File save = ImageSaver.save(origin, VSettings
				// .loadSetting("current.dir"), VSettings
				// .loadSetting("current.image"));
				// if (save != null) {
				// openImage(save);
				// }
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			} else if (eventCode.equals("mike")) {

				Application.getMainWindow().showEditMode();

			} else if (eventCode.equals("full")) {
				panel.fullImage();
			} else if (eventCode.equals("settings")) {
				new SettingsDialog(Application.getMainWindow(), "Settings", true);
			} else if (eventCode.equals("del")) {
				panel.delete();
			} else if (eventCode.equals("help")) {
				new AboutDialog();
			} else if (eventCode.startsWith("plug-")) {
				Application.getMainWindow().startPlugin(eventCode.substring(5));
			}
		} catch (RuntimeException re) {
			VExceptionHandler.raiseException(re);
		}
	}

	public void setCurrentPanel(final BasicPanel current) {
		panel = current;
	}

}
