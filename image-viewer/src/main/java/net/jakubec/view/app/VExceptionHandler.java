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

package net.jakubec.view.app;

import javax.swing.JOptionPane;

import net.jakubec.view.exception.BasicException;
import net.jakubec.view.properties.VProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class VExceptionHandler {

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
