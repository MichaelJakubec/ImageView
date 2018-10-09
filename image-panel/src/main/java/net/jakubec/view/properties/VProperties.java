package net.jakubec.view.properties;

import java.util.ResourceBundle;

/**
 * This class represents the Interface to the LanguageProperties
 * 
 * @author amunra
 * 
 */
public final class VProperties {
	private static final String BASE_NAME = "net.jakubec.view.properties.MenuResource";
	private static ResourceBundle bundle;

	static {
		try {
			bundle = ResourceBundle.getBundle(BASE_NAME);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * returns the String value for the specified key
	 * 
	 * @param key
	 *            the key of the value
	 * @return the String value
	 */
	public static String getValue(final String key) {
		try {
			return bundle.getString(key);
		} catch (Exception e) {
			return key.substring(key.lastIndexOf('.') + 1);
		}

	}

}
