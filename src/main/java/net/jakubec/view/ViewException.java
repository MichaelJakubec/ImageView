package net.jakubec.view;

import net.jakubec.view.exception.BasicException;

public class ViewException extends BasicException {

	public static int DELETE_FAILED = 1;

	public ViewException(final int value) {
		super(value);

	}

	@Override
	public String getBasicString() {
		return "view.exception";
	}
}
