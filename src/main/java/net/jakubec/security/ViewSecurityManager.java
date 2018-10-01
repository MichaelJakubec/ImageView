package net.jakubec.security;

import java.security.Permission;
import java.util.PropertyPermission;

/**
 * This is the SecurityManager for the program. The SecurityManager blocks all
 * access attempts of the {@link net.jakubec.view.plugin.ViewPlugin}. To check
 * if the class that wants to access any resource is a
 * {@link net.jakubec.view.plugin.ViewPlugin} you have to inform this class
 * about the {@link net.jakubec.view.plugin.PluginClassLoader} which is used to
 * load the {@link net.jakubec.view.plugin.ViewPlugin}
 * <p/>
 * <code>
 * <pre>
 * ClassLoader loader = new PluginClassLoader();
 * ViewSecurityManager manager = new ViewSecurityManager(loader)
 * </pre>
 * </code>
 * <p/>
 * 
 * @author amunra
 * @since Version 0.1
 * 
 */
public class ViewSecurityManager extends SecurityManager {
	private final ClassLoader pluginLoader;

	/**
	 * Constructor for a new ViewSecurityManager.
	 * 
	 * @param c
	 *            The class Loader of the Plugins
	 */
	public ViewSecurityManager(final ClassLoader c) {
		pluginLoader = c;
	}

	@Override
	public void checkPermission(final Permission per) {

		Class[] calls = this.getClassContext();
		for (Class c : calls) {
			if (pluginLoader.equals(c.getClassLoader())) {
				if (per instanceof PropertyPermission) {
					if (((PropertyPermission) per).getActions().equals("read")) return;
				}
				throw new SecurityException();
			}
		}
	}

}
