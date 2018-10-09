package net.jakubec.view.app;

import net.jakubec.view.app.settings.Settings;
import net.jakubec.view.app.settings.VSettings;
import net.jakubec.view.ViewException;
import net.jakubec.view.ViewPanel;
import net.jakubec.view.dia.Diashow;
import net.jakubec.view.edit.EditPanel;
import net.jakubec.view.listener.ImageDisplayListener;
import net.jakubec.view.plugin.PluginOrganizer;
import net.jakubec.view.plugin.ViewPanelPlugin;
import net.jakubec.view.plugin.ViewPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Locale;

public class ImageView extends JFrame {
	private static final Logger log = LogManager.getLogger(ImageView.class);


	private static final long serialVersionUID = -4865378884812046578L;


	/**
	 * Creates a new ImageView-Frame
	 */
	ImageView() {
		this(null);

	}

	/**
	 * Constructor of a new ImageView with the given path to be displayed
	 *
	 * @param filePath path to the file which should be displayed
	 */
	public ImageView(String filePath) {
		super(VSettings.PROG_NAME);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			String language = Settings.language.load();
			log.debug("Language is set to: {}", language);
			Locale.setDefault(new Locale(Settings.language.load()));

		} catch (Exception e) {
			Locale.setDefault(new Locale("en"));
		}

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		this.setLocation(0, 0);
		this.setSize(gs[0].getDisplayMode().getWidth(), gs[0].getDisplayMode().getHeight());





		if (filePath != null) {
			File f = new File(filePath);
			if (f.exists()) {
				showViewMode(f);
			} else {
				showViewMode(null);
			}
		} else {
			showViewMode(null);
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
	void showDiashowMode() {
		Diashow currentView = new Diashow(this, Settings.diaDirectory.load());
		this.setJMenuBar(null);
		SwingUtilities.invokeLater(() -> setContentPane(currentView));
	}

	/**
	 * show the Edit Mode
	 */
	void showEditMode() {
		EditPanel editPanel = new EditPanel();
		setJMenuBar(MenuFactory.createEditMenu(new MenuListener(editPanel)));
		setContentPane(editPanel);
		this.repaint();
	}

	/**
	 * show the image view mode and opens the given image
	 *
	 * @param f the image to be displayed
	 */
	private void showViewMode(File f) {
		ViewPanel viewPanel = new ViewPanel();
		viewPanel.addImageDisplayListener(new ImageDisplayListener() {
			@Override
			public void imageOpened(File file) {
				Settings.currentImage.save(file);
				Settings.currentDirectory.save(file.getParentFile());
				setTitle(Settings.currentImage.load().getAbsolutePath());
			}

			@Override
			public void zoomLevelChanged(double newZoomLevel) {
				setTitle(Settings.currentImage.load().getAbsolutePath() + " - " + ((int) (newZoomLevel * 100)) + "%");
			}
		});
		if (f != null) {
			try {
				viewPanel.openImage(f);
			} catch (ViewException e) {
				VExceptionHandler.raiseException(e);
			}
		}


		setContentPane(viewPanel);
		MenuListener menuListener = new MenuListener(viewPanel);
		setJMenuBar(MenuFactory.createMenu(menuListener));
	}



	/**
	 * This Method starts the plugin and checks if it uses a own Frame or should
	 * be inserted in the
	 *
	 * @param pluginName the Name of the Plugin which should be started
	 */
	void startPlugin(final String pluginName) {
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
