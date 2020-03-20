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

package net.jakubec.view.exception;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BasicException extends Exception {
	private static final String BASE_NAME = "net.jakubec.view.Exception";
	private static ResourceBundle bundle;

	static {
		try {
			bundle = ResourceBundle.getBundle(BASE_NAME);
		} catch (Exception e) {
			try {
				bundle = ResourceBundle.getBundle(BASE_NAME, Locale.ENGLISH);
			} catch (Exception e2) {
				e2.printStackTrace();
				// VExceptionHandler.raiseException(e2, "ResourceFileNotFound");
			}
		}
	}
	private final int errorValue;

	public final String text;

	public BasicException(Exception e, final int value) {
		super(e);
		if (value == 0) {
			errorValue = 0;
			text = "Unknown Error";
		} else {
			errorValue = value;
			text = null;
		}
	}

	public BasicException(final String msg) {
		errorValue = 0;
		text = msg;
	}

	public BasicException(int value) {
		this(null, value);
	}

	public abstract String getBasicString();

	public final String getErrorMsg() {
		if (bundle == null) return "The Resource file Exception_en.properties is missing";
		if (errorValue == 0) return text;
		return bundle.getString(getBasicString() + "." + errorValue);
	}
}
