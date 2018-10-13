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

package net.jakubec.view.listener;

import net.jakubec.view.BasicPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewNavigationListener implements ActionListener {
	private final BasicPanel panel;

	public ViewNavigationListener(BasicPanel viewPanel) {
		this.panel = viewPanel;
	}


	public void actionPerformed(final ActionEvent event) {
		String eventCode = event.getActionCommand();
		try {
			switch (eventCode) {
				case "print":
					panel.printImage();
					break;
				case "zoom+":
					panel.zoomp();
					break;
				case "zoom-":
					panel.zoomm();
					break;
				case "zoom0":
					panel.zoom0();
					break;
				case "zoom1":
					panel.zoom1();
					break;
				case "rot+":
					panel.rotateImage(true);
					break;
				case "rot-":
					panel.rotateImage(false);
					break;
				case "full":
					panel.fullImage();
					break;

			}
		} catch (RuntimeException re) {
			//TODO exception handling
			re.printStackTrace();
		}
	}
}
