package net.jakubec.view.app;

import net.jakubec.view.BasicPanel;
import net.jakubec.view.Settings.SettingsDialog;
import net.jakubec.view.listener.ViewNavigationListener;

import java.awt.event.ActionEvent;

class MenuListener extends ViewNavigationListener {
	public MenuListener(final BasicPanel panel) {
		super(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String eventCode = e.getActionCommand();

		if (eventCode.equals("dia")) {
			Application.getMainWindow().showDiashowMode();
		} else if (eventCode.equals("mike")) {
			Application.getMainWindow().showEditMode();
		} else if (eventCode.equals("settings")) {
			new SettingsDialog(Application.getMainWindow(), "Settings", true);
		} else if (eventCode.startsWith("plug-")) {
			Application.getMainWindow().startPlugin(eventCode.substring(5));
		} else if (eventCode.equals("help")) {
			new AboutDialog();
		} else {
			super.actionPerformed(e);
		}
	}


}
