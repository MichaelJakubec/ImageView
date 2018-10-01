package net.jakubec.view.Settings;

import net.jakubec.view.exception.BasicException;

public class SettingsException extends BasicException {

	public SettingsException(final int value) {
		super(value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getBasicString() {
		return "exception.settings";
	}
}
