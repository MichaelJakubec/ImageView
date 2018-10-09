package net.jakubec.view.plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * This is the class loader of all plugins. To load a class you have to specify
 * the path to the jar file
 * 
 * @author amunra
 * 
 */
public class PluginClassLoader extends ClassLoader {

	private class JarResource {

		private final Map<String, Long> classSizes = new HashMap<String, Long>();

		private final Map<String, byte[]> jarContent = new HashMap<String, byte[]>();

		public JarResource() {

		}

		public JarResource(final String jarFileName) {
			addArchive(jarFileName);
		}

		/**
		 * Adds a new Jar-archive to the instance.
		 * 
		 * @param archiv
		 *            the path to the jar-Archive
		 * @throws java.io.FileNotFoundException
		 *             if the jar-Archive doesn't exists
		 */
		private void addArchive(final String archiv) {
			try {
				File file = new File(archiv);
				if (!file.exists()) throw new FileNotFoundException();
				JarFile jarFile = new JarFile(file);
				Enumeration<JarEntry> enu = jarFile.entries();
				while (enu.hasMoreElements()) {
					JarEntry entry = enu.nextElement();
					classSizes.put(entry.getName(), entry.getSize());
					System.out.println(entry.getName());
				}
				jarFile.close();
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				JarInputStream jis = new JarInputStream(bis);
				JarEntry entry = null;
				while ((entry = jis.getNextJarEntry()) != null) {
					if (entry.isDirectory()) {
						continue;
					}
					long size = entry.getSize();
					if (size < 0) {
						size = classSizes.get(entry.getName());
					}
					byte[] buffer = new byte[(int) size];
					int offset = 0;
					while (size - offset > 0) {
						int chunk = jis.read(buffer, offset, (int) size - offset);
						if (chunk == -1) {
							break;
						}
						offset += chunk;
					}
					jarContent.put(entry.getName(), buffer);
				}
			} catch (NullPointerException e) {
				System.out.println("done.");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void addArchives(final String[] archives) {
			for (String s : archives) {
				addArchive(s);
			}

		}

		public byte[] getResource(final String name) {
			return jarContent.get(name);
		}

	}

	private static final String formatClassUrl(final String jarName) {
		String result = jarName.replace('.', '/');
		return result + ".class";
	}

	private final JarResource jarResource;

	public PluginClassLoader() {
		jarResource = new JarResource();
	}

	public PluginClassLoader(final String jarName) {
		jarResource = new JarResource(jarName);
	}

	public PluginClassLoader(final String[] jarArrays) {
		jarResource = new JarResource(jarArrays[0]);

		for (int i = 1; i < jarArrays.length; i++) {
			jarResource.addArchive(jarArrays[i]);
		}
	}

	public void addArchive(final String jarName) {
		jarResource.addArchive(jarName);
	}

	public void addArchives(final String[] archives) {
		jarResource.addArchives(archives);
	}

	@Override
	public Class<?> loadClass(final String name) throws ClassNotFoundException {
		return loadClass(name, true);
	}

	@Override
	public Class<?> loadClass(final String name, final boolean resolve)
			throws ClassNotFoundException {
		Class<?> result;
		// ist vielleicht schon im CP
		try {
			result = super.findSystemClass(name);
			System.out.println("Klasse im Classpath gefunden");
			return result;
		} catch (ClassNotFoundException e) {

		}
		// lade von der Resource
		byte[] classBytes = jarResource.getResource(formatClassUrl(name));
		if (classBytes == null) throw new ClassNotFoundException("Klasse nicht in der jar");
		result = defineClass(name, classBytes, 0, classBytes.length);
		if (result == null) throw new ClassNotFoundException();
		if (resolve) {
			resolveClass(result);
		}
		return result;
	}

}
