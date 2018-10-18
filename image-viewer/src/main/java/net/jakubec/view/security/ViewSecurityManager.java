/*
 * Copyright 2018 Michael Jakubec
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jakubec.view.security;

import java.security.Permission;
import java.util.PropertyPermission;

/**
 * <p>
 * This is the SecurityManager for the program. The SecurityManager blocks all
 * access attempts of the {@link net.jakubec.view.plugin.ViewPlugin}. To check
 * if the class that wants to access any resource is a
 * {@link net.jakubec.view.plugin.ViewPlugin} you have to inform this class
 * about the {@link net.jakubec.view.plugin.PluginClassLoader} which is used to
 * load the {@link net.jakubec.view.plugin.ViewPlugin}
 * </p>
 * <p>
 * <code>
 * ClassLoader loader = new PluginClassLoader();
 * ViewSecurityManager manager = new ViewSecurityManager(loader)
 * </code>
 * </p>
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
