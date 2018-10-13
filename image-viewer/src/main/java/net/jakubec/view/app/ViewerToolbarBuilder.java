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

package net.jakubec.view.app;

import net.jakubec.view.ViewPanel;

import javax.swing.*;
import java.awt.event.ActionListener;

class ViewerToolbarBuilder extends ViewPanel.ToolBarBuilder {
	private final ActionListener act;
	ViewerToolbarBuilder(ActionListener act){
		this.act = act;
	}
	@Override
	public void addButtonsToToolbar(JToolBar toolbar) {

		JButton btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/open.gif")));
		btn.setActionCommand("open");
		toolbar.add(btn);
		btn.addActionListener(act);

		btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/previous.gif")));
		btn.setActionCommand("previous");
		toolbar.add(btn);
		btn.addActionListener(act);

		btn = new JButton(new ImageIcon(ViewPanel.class.getResource("/next.gif")));
		btn.setActionCommand("next");
		toolbar.add(btn);
		btn.addActionListener(act);
		toolbar.addSeparator();
		super.addButtonsToToolbar(toolbar);
	}
}
