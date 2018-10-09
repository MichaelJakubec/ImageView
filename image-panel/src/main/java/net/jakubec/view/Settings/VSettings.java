package net.jakubec.view.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;


public final class VSettings {
	/**
	 * Properties for the ImageView where you can Save all available
	 * informations;
	 */
	// private static Properties props = new Properties();
	private static Properties settings = new SettingsProvider();

	public static String rootPath;
	public static File rootDir;
	public static final String PROG_NAME = "ImageView";
	public static final String PROG_VERSION = "0.1";
	public static final String PROG_HOMEPAGE = "http://image.jakubec.net";

	static {
		try {
			URL path = VSettings.class.getProtectionDomain().getCodeSource().getLocation();
			rootDir = new File(path.getFile()).getParentFile();
			rootPath = rootDir.getPath() + System.getProperty("file.separator");

			File file = new File(rootDir, "settingsProvider.prop");
			settings.loadFromXML(new FileInputStream(file));

		} catch (Exception e1) {
			System.err.println("couldn't load load props");
			e1.printStackTrace();
		}

	}

	public static Object loadObjectSetting(final String key) {
		return settings.get(key);
	}

	public static String loadSetting(final String key) {
		try {
			return settings.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveObjectSetting(final String key, final Object value) {
		settings.put(key, value);
	}

	public static void saveProps() throws SettingsException{
		try {
			settings.storeToXML(new FileOutputStream(new File(rootDir, "settingsProvider.prop")),
					"SettingsProvider");

			System.out.println("Properties saved");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception re) {
			throw new SettingsException(1);

		}

	}

	public static void saveSetting(final String key, final String value) {
		settings.setProperty(key, value);
	}

}