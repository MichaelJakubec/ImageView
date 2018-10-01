package net.jakubec.view.plugin;

import java.awt.Container;

import javax.swing.JMenuBar;

/**
 * This is the interface for a plugin which should be displayed in the main View
 * of the Program
 * 
 * @author amunra
 * @since Version 0.1
 * 
 */
public interface ViewPanelPlugin extends ViewPlugin {

	/**
	 * returns the GUI of this plugin. If you don't wan't to have a GUI please
	 * don't use this interface. Use instead ViewPlugin
	 * 
	 * @return the GUI of this interface
	 */
	public abstract Container getPanel();

	/**
	 * Returns the MenuBar of this Plugin. If you don't need a spezial Menu
	 * return null;
	 * 
	 * @return the JMenuBar of this Plugin or null
	 */
	public abstract JMenuBar getViewMenuBar();

}
