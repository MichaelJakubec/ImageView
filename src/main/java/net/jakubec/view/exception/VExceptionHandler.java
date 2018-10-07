package net.jakubec.view.exception;

import javax.swing.JOptionPane;

import net.jakubec.view.Application;

import net.jakubec.view.properties.VProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VExceptionHandler {

	public static Logger log = LogManager.getLogger(VExceptionHandler.class);

	public static void raiseException(final BasicException be) {
		raiseException(be, be.getErrorMsg());
	}

	public static void raiseException(final Exception e) {
		raiseException(e, "An Unhandled Exception Occurred");

	}

	public static void raiseException(final Exception error, final String message) {
		new ExceptionDialog(error, message);
		log.error(message, error);
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
