package net.jakubec.view;

import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Locale;

import javax.swing.*;

import net.jakubec.view.Settings.Settings;
import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.dia.Diashow;
import net.jakubec.view.edit.EditPanel;
import net.jakubec.view.exception.VExceptionHandler;
import net.jakubec.view.listener.MenuListener;
import net.jakubec.view.log.Logger;
import net.jakubec.view.menu.MenuFactory;
import net.jakubec.view.plugin.PluginOrganizer;
import net.jakubec.view.plugin.ViewPanelPlugin;
import net.jakubec.view.plugin.ViewPlugin;

public class ImageView extends JFrame {

	public static final String FILETYPES = "jpg.gif.png.bmp";
	private static final long serialVersionUID = -4865378884812046578L;


	/**
	 * The MenuListener
	 */
	private MenuListener menuListener;


	/**
	 * Creates a new ImageView-Frame
	 */
	ImageView() {
		this(null);

	}

	/**
	 * Constructor of a new ImageView with the given path to be displayed
	 * @param filePath path to the file which should be displayed
	 */
	public ImageView(String filePath) {
		super(VSettings.PROG_NAME);
		Logger.init();
		try {
			Locale.setDefault(new Locale(Settings.language.load()));

		} catch (Exception e) {
			Locale.setDefault(new Locale("en"));
		}

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		this.setLocation(0, 0);
		this.setSize(gs[0].getDisplayMode().getWidth(), gs[0].getDisplayMode().getHeight());

		// LookAndFeel wird auf Nimbus gestezt

		// Aufruf zum setzten des Menüs
		addWindowListener(new WindowClosingAdapter(true));
		if (filePath != null ){
			File f = new File(filePath);
			if (f.exists()) {
				showViewMode(f);
			} else {
				showViewMode();
			}
		} else {
			showViewMode();
		}

		this.setIconImage(new ImageIcon(ImageView.class.getResource("/icon.gif")).getImage());
	}


	@Override
	public void setTitle(final String title) {
		String sb = VSettings.PROG_NAME + " - " + title;
		super.setTitle(sb);
	}

	/**
	 * shows the Diahow Mode
	 */
	public void showDiashowMode() {
		Diashow currentView = new Diashow(this);
		this.setJMenuBar(null);
		SwingUtilities.invokeLater(() ->setContentPane(currentView));
	}

	/**
	 * show the Edit Mode
	 */
	public void showEditMode() {
		EditPanel editPanel = new EditPanel();
		Container currentView = editPanel.getPanel();
		setJMenuBar(MenuFactory.createEditMenu(menuListener));
		setContentPane(currentView);
		menuListener.setCurrentPanel(editPanel);
		this.repaint();

	}

	/**
	 * show the image view mode and opens the given image
	 * @param f the image to be displayed
	 */
	private void showViewMode(File f) {
		ViewPanel viewPanel = new ViewPanel();
		if (f != null) {
			viewPanel.openImage(f);
		}
		Container currentView = viewPanel.getPanel();

		setContentPane(currentView);
		if (menuListener == null) {
			menuListener = new MenuListener(viewPanel);
		} else {
			menuListener.setCurrentPanel(viewPanel);
		}
		setJMenuBar(MenuFactory.createMenu(menuListener));
	}



	/**
	 * shows the edit Mode
	 */
	public void showViewMode() {
		showViewMode(null);
	}

	/**
	 * This Method starts the plugin and checks if it uses a own Frame or should
	 * be inserted in the
	 * 
	 * @param pluginName
	 *            the Name of the Plugin which should be started
	 */
	public void startPlugin(final String pluginName) {
		ViewPlugin plugin = null;

		try {
			plugin = PluginOrganizer.getInstance().getPluginByName(pluginName);
			if (plugin != null) {
				plugin.setup();
			}
		} catch (Exception e) {
			VExceptionHandler.raiseException(new Exception(e));
		}
		if (plugin instanceof ViewPanelPlugin) {
			System.out.println("Load Panel");
			Container c = ((ViewPanelPlugin) plugin).getPanel();
			if (c == null) return;
			setContentPane(c);

			c.invalidate();

			this.doLayout();
			this.validate();
		}
	}
}
