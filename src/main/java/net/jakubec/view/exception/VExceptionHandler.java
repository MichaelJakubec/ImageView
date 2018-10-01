package net.jakubec.view.exception;

import javax.swing.JOptionPane;

import net.jakubec.view.Application;
import net.jakubec.view.log.Logger;
import net.jakubec.view.properties.VProperties;

public class VExceptionHandler {

	public static void raiseException(final BasicException be) {
		raiseException(be, be.getErrorMsg());
	}

	public static void raiseException(final Exception e) {
		raiseException(e, "An Unhandled Exception Occurred");

	}

	public static void raiseException(final Exception error, final String message) {
		new ExceptionDialog(error, message);

		StackTraceElement[] stackTrace = error.getStackTrace();
		StringBuilder sb = new StringBuilder();
		sb.append("Message: ");
		sb.append(error.getMessage());
		sb.append("\nName:");
		sb.append(error.getClass().getName());
		for (StackTraceElement aStackTrace : stackTrace) {
			sb.append("\n Class: ");
			sb.append(aStackTrace.getClassName());
			sb.append(" - Method: ");
			sb.append(aStackTrace.getMethodName());
			sb.append(" - Line: ");
			sb.append(aStackTrace.getLineNumber());
			sb.append(" ");
			sb.append(aStackTrace.getFileName());

		}
		Logger.logException(message, sb.toString(), error);
	}

	/**
	 * shows a message dialog with the given message
	 * @param message the message to be displayed
	 */
	public static void raiseMessage(String message) {
		JOptionPane.showMessageDialog(Application.getMainWindow(), message, VProperties
				.getValue("error"), JOptionPane.INFORMATION_MESSAGE);
	}
}
