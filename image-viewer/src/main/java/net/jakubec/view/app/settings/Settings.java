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

package net.jakubec.view.app.settings;

import java.io.File;

/**
 * This class is used to store and load the Settings.
 * 
 * @author amunra
 * 
 * @param <T>
 *            The Class which Type is stored
 */
public class Settings<T> {
	/**
	 * The settings where the current directory is saved. The current directory
	 * is the directory where the last displayed image of the view mode is
	 * stored.
	 */
	public static Settings<File> currentDirectory = new Settings<>("current.dir", File.class,
			new File(System.getProperty("user.home")));
	/**
	 * the setting where the current image is saved. The current image is the
	 * last displayed image in the view mode.
	 */
	public static Settings<File> currentImage = new Settings<>("current.image", File.class,
			new File(""));
	/**
	 * stores the last directory from where an image is added to the diashow.
	 */
	public static Settings<File> diaDirectory = new Settings<>("dia.dir", File.class, new File(
			System.getProperty("user.home")));
	/**
	 * stores the current active language
	 */
	public static Settings<String> language = new Settings<>("settings.language",
			String.class, "en");

	/**
	 * stores if the program should update automatically.
	 */
	public static Settings<Boolean> automaticUpdate = new Settings<>("settings.autoUpdate",
			Boolean.class, true);

	/**
	 * the key in the settings.properties file
	 */
	private final String key;
	/**
	 * the default value of this setting
	 */
	private final T defaultValue;
	/**
	 * the Type of this setting.
	 */
	private final Class clazz;

	/**
	 * Contructs a new Settings with the spezified key and the given default
	 * value. The default vlaue is used if the settings value couldn't be found
	 * in the setting.properties
	 * 
	 * @param key
	 *            the key in the settings.properties
	 * @param clazz
	 *            the class of the
	 * @param defaultValue
	 *            the default value of this setting
	 */
	private Settings(final String key, final Class clazz, final T defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
		this.clazz = clazz;
	}

	/**
	 * loads the current settings value. If no value is saved in the
	 * settings.properties the default value is returned.
	 * 
	 * @return the current settings value or the default value
	 */
	@SuppressWarnings("unchecked")
	public T load() {
		String ret = VSettings.loadSetting(key);

		if (ret == null || ret.equals("null")) return defaultValue;
		if (clazz.equals(String.class)) return (T) ret;
		else if (clazz.equals(File.class)) {
			File f = new File(ret);
			if (!f.exists()) return defaultValue;
			return (T) f;
		} else if (clazz.equals(Boolean.class)) {
			Boolean bool = Boolean.valueOf(ret);
			return (T) bool;
		}
		return (T) ret;

	}

	/**
	 * saves a new value for the sttings
	 * 
	 * @param value
	 *            the new value which should be stored
	 */
	public void save(final T value) {
		if (clazz.equals(String.class)) {
			VSettings.saveSetting(key, (String) value);
		} else if (clazz.equals(File.class)) {
			if (value == null) {
				saveDefaultValue();
				return;
			}
			String val = value.toString();
			VSettings.saveSetting(key, val);
		} else if (clazz.equals(Boolean.class)) {
			if (value == null) {
				saveDefaultValue();
				return;
			}
			String val = value.toString();
			VSettings.saveSetting(key, val);
		}
	}

	/**
	 * saves the default value.
	 */
	private void saveDefaultValue() {
		if (defaultValue == null) {
			VSettings.saveSetting(key, "null");
		} else {
			save(defaultValue);
		}

	}
}
