package net.jakubec.view;

import net.jakubec.view.exception.BasicException;

public class ViewException extends BasicException {

	public static int DELETE_FAILED = 1;

	public static int OPEN_FAILED = 2;

	public static final int SAVE_FAILED = 3;

	public ViewException(final int value) {
		super(value);

	}

	public ViewException(Exception e, int value ) {
		super(e, value);

	}

	@Override
	public String getBasicString() {
		return "view.exception";
	}
}
