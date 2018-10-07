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

	public BasicException(final int value) {
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

	public abstract String getBasicString();

	public final String getErrorMsg() {
		if (bundle == null) return "The Resource file Exception_en.properties is missing";
		if (errorValue == 0) return text;
		return bundle.getString(getBasicString() + "." + errorValue);
	}
}
