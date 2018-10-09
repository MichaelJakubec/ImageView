package net.jakubec.view.app.settings;

import net.jakubec.view.exception.BasicException;

public class SettingsException extends BasicException {

	public SettingsException(final int value) {
		super(value);
	}

	@Override
	public String getBasicString() {
		return "exception.settings";
	}
}
