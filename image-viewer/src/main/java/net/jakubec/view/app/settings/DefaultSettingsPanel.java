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

package net.jakubec.view.app.settings;

import java.awt.*;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import net.jakubec.view.plugin.VAbstractSettingsPanel;
import net.jakubec.view.properties.VProperties;

public class DefaultSettingsPanel extends VAbstractSettingsPanel {
	ButtonGroup localisation = new ButtonGroup();
	JCheckBox automaticUpdate = new JCheckBox();

	@Override
	public Icon getIcon() {
		// we return null as we do not want an icon to be displayed
		return null;
	}

	@Override
	public String getInfoText() {

		return "";
	}

	@Override
	public String getTitle() {
		return VProperties.getValue("settings.appearence");
	}

	@Override
	public void onOkay() {

		Settings.language.save(localisation.getSelection().getActionCommand());
		Settings.automaticUpdate.save(automaticUpdate.isSelected());
	}

	@Override
	public void onSetup() {
		Locale.setDefault(new Locale(""));
		JPanel locationPanel = new JPanel(new GridBagLayout());
		String lang = Settings.language.load();
		if (lang == null) {
			lang = "en";
		}
		JRadioButton bt = new JRadioButton();
		if (lang.equals("en")) {
			bt.setSelected(true);
		}

		Icon icon = new ImageIcon(DefaultSettingsPanel.class.getResource("/english.gif"));


		localisation.add(bt);
		bt.setActionCommand("en");
		locationPanel.add(bt, new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(12,12,5,5),0,0));
		locationPanel.add(
				new JLabel(VProperties.getValue("settings.language.english"), icon, SwingConstants.LEFT),
				new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(12,0,5,5),0,0)
		);

		icon = new ImageIcon(DefaultSettingsPanel.class.getResource("/german.gif"));
		bt = new JRadioButton();
		bt.setActionCommand("de");
		localisation.add(bt);
		if (lang.equals("de")) {
			bt.setSelected(true);
		}

		locationPanel.add(bt, new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,12,5,5),0,0));
		locationPanel.add(
				new JLabel(VProperties.getValue("settings.language.german"), icon, SwingConstants.LEFT),
				new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,5),0,0)
		);
		locationPanel.setBorder(new TitledBorder(VProperties.getValue("settings.language")));
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		this.add(locationPanel, c);
		JPanel updatePanel = new JPanel();
		updatePanel.setLayout(new GridLayout(1, 1));
		automaticUpdate.setSelected(Settings.automaticUpdate.load());
		automaticUpdate.setText(VProperties.getValue("settings.update.text"));
		updatePanel.setBorder(new TitledBorder(VProperties.getValue("settings.update")));
		updatePanel.add(automaticUpdate);
		c.gridy = 1;
		add(updatePanel, c);

	}
}
