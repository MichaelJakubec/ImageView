package net.jakubec.view.gui.basic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.jakubec.view.properties.VProperties;

public abstract class OkCancleDialog extends JDialog {

	public OkCancleDialog(String title) {
		super();
		setModal(true);

		JButton button = new JButton(VProperties.getValue("button.ok"));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				okeyClicked();
				setVisible(false);
				dispose();
			}

		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);
		button = new JButton(VProperties.getValue("button.cancel"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});

		buttonPanel.add(button);
		getContentPane().add(buttonPanel, BorderLayout.PAGE_END);

	}

	public abstract void okeyClicked();

	public void setPanel(Component c) {
		getContentPane().add(c, BorderLayout.CENTER);
	}
}
