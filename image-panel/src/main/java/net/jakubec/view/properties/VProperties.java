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
