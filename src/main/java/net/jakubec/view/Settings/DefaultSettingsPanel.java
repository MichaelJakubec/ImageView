package net.jakubec.view.Settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import net.jakubec.view.gui.basic.CompoundIcon;
import net.jakubec.view.plugin.VAbstractSettingsPanel;
import net.jakubec.view.properties.VProperties;

public class DefaultSettingsPanel extends VAbstractSettingsPanel {
	ButtonGroup localisation = new ButtonGroup();
	JCheckBox automaticUpdate = new JCheckBox();

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInfoText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return VProperties.getValue("settings.appearence");
	}

	@Override
	public void onOkey() {

		Settings.language.save(localisation.getSelection().getActionCommand());
		Settings.automaticUpdate.save(automaticUpdate.isSelected());
	}

	@Override
	public void onSetup() {
		Locale.setDefault(new Locale(""));
		JPanel locationPanel = new JPanel();
		String lang = Settings.language.load();
		if (lang == null) {
			lang = "en";
		}
		JRadioButton bt = new JRadioButton(VProperties.getValue("settings.language.english"));
		if (lang.equals("en")) {
			bt.setSelected(true);
		}
		Icon ico = UIManager.getIcon("RadioButton.icon");
		CompoundIcon icon = new CompoundIcon(ico,
				new ImageIcon(VSettings.rootPath + "english.gif"), 3);
		bt.setIcon(icon);
		locationPanel.setLayout(new GridLayout(2, 1));
		localisation.add(bt);
		bt.setActionCommand("en");
		locationPanel.add(bt);
		bt = new JRadioButton(VProperties.getValue("settings.language.german"));
		icon = new CompoundIcon(ico, new ImageIcon(VSettings.rootPath + "german.gif"), 3);
		bt.setIcon(icon);
		if (lang.equals("de")) {
			bt.setSelected(true);
		}
		bt.setActionCommand("de");
		localisation.add(bt);
		locationPanel.add(bt);

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
