package net.jakubec.view.Settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import net.jakubec.view.plugin.PluginOrganizer;
import net.jakubec.view.plugin.VAbstractSettingsPanel;
import net.jakubec.view.plugin.ViewPlugin;
import net.jakubec.view.properties.VProperties;

public class SettingsDialog extends JDialog implements ActionListener {

	private static class VButtonBorder implements Border {

		@Override
		public Insets getBorderInsets(final Component arg0) {
			return new Insets(5, 0, 0, 0);

		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}

		@Override
		public void paintBorder(final Component com, final Graphics g, final int x, final int y,
				final int w, final int h) {
			g.drawLine(20, 2, w - 20, 2);

		}

	}

	private ArrayList<VAbstractSettingsPanel> panels = new ArrayList<>();

	private JTabbedPane tabs = new JTabbedPane();

	public SettingsDialog() {
		super();
		construct();

	}

	public SettingsDialog(final Frame owner) {
		super(owner);
		construct();
	}

	public SettingsDialog(final Frame owner, final boolean modal) {
		super(owner, modal);
		construct();
	}

	public SettingsDialog(final Frame owner, final String title) {
		super(owner, title);
		construct();
	}

	public SettingsDialog(final Frame owner, final String title, final boolean modal) {
		super(owner, title, modal);
		construct();
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			for (VAbstractSettingsPanel v : panels) {
				v.onOkay();
			}
		}
		this.setVisible(false);
		this.dispose();
	}

	private void construct() {
		this.setSize(700, 500);
		this.getContentPane().setLayout(new BorderLayout());
		createButtonPanel();
		readList();
		fillTabs();

		setupPanels();
		this.setVisible(true);

	}

	private void createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new VButtonBorder());
		JButton bt = new JButton(VProperties.getValue("button.ok"));
		bt.addActionListener(this);
		bt.setActionCommand("ok");
		panel.add(bt);
		bt = new JButton(VProperties.getValue("button.cancel"));
		bt.addActionListener(this);
		panel.add(bt);
		this.getContentPane().add(panel, BorderLayout.SOUTH);

	}

	private void fillTabs() {
		for (VAbstractSettingsPanel pan : panels) {
			if (pan.getTitle() == null || pan.getTitle().equals("")) {
				continue;
			}
			tabs.addTab(pan.getTitle(), pan.getIcon(), pan, pan.getInfoText());
		}

		this.getContentPane().add(tabs, BorderLayout.CENTER);
	}

	private void readList() {
		panels.add(new DefaultSettingsPanel());
		for (ViewPlugin viewPlugin : PluginOrganizer.getInstance().getPlugins()) {
			panels.add(viewPlugin.getSettingsPanel());
		}

	}

	private void setupPanels() {
		SwingUtilities.invokeLater( () -> {
			for (VAbstractSettingsPanel v : panels) {
				v.onSetup();

			}
		});

	}
}
