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
