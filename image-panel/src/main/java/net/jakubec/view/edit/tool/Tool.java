package net.jakubec.view.edit.tool;

import java.awt.Component;

public abstract class Tool {

	public enum TOOL_TYPE {
		Line, Move
	};

	public abstract Component getToolSettingsPanel();

	/**
	 * This method returns the ToolType
	 * 
	 * @return the type of the tool
	 */
	public abstract TOOL_TYPE getType();

}
