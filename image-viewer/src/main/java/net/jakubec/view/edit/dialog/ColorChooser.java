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

package net.jakubec.view.edit.dialog;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.jakubec.view.gui.basic.OkCancleDialog;
import net.jakubec.view.properties.VProperties;

public class ColorChooser extends OkCancleDialog {
	private JColorChooser chooser;

	private Color selectedColor;

	// private Color tempColor;

	public ColorChooser(Color col) {
		super(VProperties.getValue("edit.choose_color"));
		setSize(500, 400);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		setLocation((gs[0].getDisplayMode().getWidth() - 500) / 2, (gs[0].getDisplayMode()
				.getHeight() - 400) / 2);

		selectedColor = col;
		chooser = new JColorChooser(col);

		chooser.getSelectionModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {

			}

		});
		setPanel(chooser);

	}

	/**
	 * returns the selectedColor. If selected color is null it means no Color is
	 * selected.
	 * 
	 * @return the selectedColor or null;
	 */
	public Color getSelectedColor() {
		return selectedColor;
	}

	@Override
	public void okeyClicked() {
		selectedColor = chooser.getColor();
	}
}
