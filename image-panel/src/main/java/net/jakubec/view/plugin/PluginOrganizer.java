package net.jakubec.view.plugin;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;


/**
 * This class is responsible for the organizing the {@link ViewPlugin}. It loads
 * all installed plugins and
 * 
 * @author amunra
 * 
 */
public final class PluginOrganizer {

	private static PluginOrganizer instance;

	/**
	 * get the current Instance of the PluginOrganizer
	 * 
	 * @return the current instance
	 */
	public static PluginOrganizer getInstance() {
		return instance;
	}

	public static PluginOrganizer getInstance(final PluginClassLoader loader) {
		if (instance == null) {
			instance = new PluginOrganizer(loader);
		}

		return instance;
	}

	/**
	 * the URLClassLoader, that loads all the plugins
	 */
	private final PluginClassLoader pluginLoader;
	/**
	 * List of all available plugins
	 */
	private final List<ViewPlugin> plugins;

	/**
	 * Constructor for a new PluginOrganizer. It loads all plugins from the
	 * current Directory
	 */
	private PluginOrganizer(final PluginClassLoader loader) {
		plugins = new LinkedList<ViewPlugin>();
		//TODO
		File pluginFolder = new File("");
		JOptionPane.showMessageDialog(null, pluginFolder.getAbsolutePath());
		pluginFolder = new File(pluginFolder, "plugins");
		if (!pluginFolder.exists() || !pluginFolder.isDirectory()) {
			pluginLoader = null;
			return;
		}

		File[] files = pluginFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File file) {
				return file.getName().endsWith("jar");
			}
		});
		String[] pluginJars = new String[files.length];
		for (int i = 0; i < files.length; i++) {

			pluginJars[i] = files[i].getAbsolutePath();

		}

		pluginLoader = loader;
		loader.addArchives(pluginJars);

		try {
			Class cl = pluginLoader.loadClass("image.plugin.MyPlugin");
			ViewPlugin plug = (ViewPlugin) cl.newInstance();
			plugins.add(plug);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	/**
	 * returns the plugin spezified by the given name. If no plugin is found
	 * this method returns null.
	 * 
	 * @param name
	 *            of the plugin
	 * @return the viewPlugin
	 */
	public ViewPlugin getPluginByName(final String name) {
		for (Iterator<ViewPlugin> it = plugins.iterator(); it.hasNext();) {
			ViewPlugin p = it.next();
			if (p.getName().equals(name)) return p;
		}
		return null;
	}

	/**
	 * returns a list of all available plugins.
	 * 
	 * @return the list of plugins
	 */
	public List<ViewPlugin> getPlugins() {

		return plugins;
	}

}
