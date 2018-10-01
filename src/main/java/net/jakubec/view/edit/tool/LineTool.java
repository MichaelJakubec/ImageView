package net.jakubec.view.edit.tool;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.jakubec.view.properties.VProperties;

public class LineTool extends Tool {
	private int lineWidth;

	public int getLineWidth() {
		return lineWidth;
	}

	@Override
	public Component getToolSettingsPanel() {
		JPanel panel = new JPanel();
		JLabel lb = new JLabel(VProperties.getValue("tool.linewidth"));

		return panel;
	}

	@Override
	public TOOL_TYPE getType() {
		return Tool.TOOL_TYPE.Line;
	}
}
