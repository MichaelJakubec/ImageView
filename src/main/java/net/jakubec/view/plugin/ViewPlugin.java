package net.jakubec.view.plugin;

import javax.swing.JMenu;

/**
 * This interface is the base for all Plugins. Every Plugin must implement this
 * interface.
 * 
 * @author amunra
 * @since Version 0.1
 * 
 */
public interface ViewPlugin {

	/**
	 * This method loads the menu for this plugin if no menu is wanted this
	 * method should return null. the menu should be labeled with the plugin
	 * name.
	 * 
	 * @return the JMenu- Object of this plugin or null
	 */
	public JMenu getMenu();

	/**
	 * Gets the name of the plugin. The name is used for the plugin Menu and in
	 * the settings dialog.
	 * 
	 * @return the name of the plugin
	 */
	public String getName();

	/**
	 * this method loads the {@link VAbstractSettingsPanel SettingsPanel} for
	 * this Object. If no settings are needed return null
	 * 
	 * @return the SettingsPanel for this Plugin or null if there are no
	 *         settings
	 * 
	 * @see VAbstractSettingsPanel
	 */
	public VAbstractSettingsPanel getSettingsPanel();

	/**
	 * This method is called when the plugin is used by the server;
	 */
	public void setup();

}
