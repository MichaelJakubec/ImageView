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

package net.jakubec.view.plugin;

import javax.swing.Icon;
import javax.swing.JPanel;

public abstract class VAbstractSettingsPanel extends JPanel{
	
	/**
	 * This method is called when the SettingsDialog is created
	 * Here all the GUI components of this Panel should be created
	 */
	public abstract void onSetup();
	/**
	 * Is called when okay is pressed
	 * Everything which should be stored should happen here
	 */
	public abstract void onOkay();
	/**
	 * returns the Title of this SettingsTab.
	 * if null or "" is returned the tab is not displayed in the SettingsDialog
	 * @return the title of this Panel 
	 */
	public abstract String getTitle();
	/** 
	 * return the Icon of this tab or null if no icon is wanted
	 * @return the icon of the Tab
	 */
	public abstract Icon getIcon();

	/**
	 * returns an tool tip text for this panel
	 * @return the tool tip text to be displayed
	 */
	public abstract String getInfoText();
	
}
