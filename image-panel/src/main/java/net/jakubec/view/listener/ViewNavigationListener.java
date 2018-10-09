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
				case "undo":
					panel.undo();
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
