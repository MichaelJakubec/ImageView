package net.jakubec.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import net.jakubec.view.Settings.VSettings;
import net.jakubec.view.log.Logger;

/**
 * This class is responsible for closing this application
 * 
 * @author amunra
 * 
 */
public class WindowClosingAdapter extends WindowAdapter {
	/**
	 * parameter if the program should exit or not
	 */
	private final boolean exitSystem;

	public WindowClosingAdapter() {
		this(false);

	}

	/**
	 * Creates a new WindowClosingAdapter
	 * 
	 * @param exitSystem
	 *            if the program should exit
	 */
	public WindowClosingAdapter(final boolean exitSystem) {
		this.exitSystem = exitSystem;
	}

	@Override
	public void windowClosing(final WindowEvent event) {
		VSettings.saveProps();
		event.getWindow().setVisible(false);
		event.getWindow().dispose();

		if (exitSystem) {
			Logger.close();
			System.exit(0);
		}
	}
}
