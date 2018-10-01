package net.jakubec.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

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

	/** Container for the */
	private Container cp;
	/**
	 * The current View of the Programm
	 */
	private BasicPanel currentView;
	/**
	 * The MenuListener
	 */
	private MenuListener menuListener;

	/** The path of the image */
	private File path;

	/**
	 * Creates a new ImageView-Frame
	 */
	ImageView() {

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

		// LookAndFell wird auf Nimbus gestezt
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

		} catch (Exception e) {
		}
		// Aufruf zum setzten des Menüs
		addWindowListener(new WindowClosingAdapter(true));
		showViewMode();
		this.setIconImage(new ImageIcon(VSettings.rootPath + "icon.gif").getImage());
	}

	ImageView(final String[] args) {

	}

	public void open(final File file) {
		currentView.openImage(file);
	}

	/**
	 * opens a new File in the current View specified by the path given as
	 * parameter
	 * 
	 * @param file
	 *            the path to the file that should be opened
	 */
	public void open(final String file) {
		open(new File(file));
	}

	@Override
	public void setTitle(final String title) {
		StringBuilder sb = new StringBuilder(VSettings.PROG_NAME);
		sb.append(" - ");
		sb.append(title);

		super.setTitle(sb.toString());
	}

	/**
	 * shows the Diahow Mode
	 */
	public void showDiashowMode() {
		currentView = new Diashow(this);
		this.setJMenuBar(null);
		setContentPane(currentView.getPanel());
		if (menuListener == null) {
			menuListener = new MenuListener(currentView);
		} else {
			menuListener.setCurrentPanel(currentView);
		}
		((JPanel) currentView).revalidate();
		this.repaint();

	}

	/**
	 * show the Edit Mode
	 */
	public void showEditMode() {
		currentView = new EditPanel();
		setJMenuBar(MenuFactory.createEditMenu(menuListener));
		setContentPane(currentView.getPanel());
		menuListener.setCurrentPanel(currentView);
		this.repaint();

	}

	/**
	 * shows the edit Mode
	 */
	public void showViewMode() {

		currentView = new ViewPanel();

		setContentPane(currentView.getPanel());
		if (menuListener == null) {
			menuListener = new MenuListener(currentView);
		} else {
			menuListener.setCurrentPanel(currentView);
		}
		setJMenuBar(MenuFactory.createMenu(menuListener));
		cp = getContentPane();
		// creats new MenuBar
		cp.add(MenuFactory.newMenuBar( menuListener), BorderLayout.NORTH);

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
			plugin.setup();
		} catch (Exception e) {
			// TODO- ExceptionHandling adden
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
