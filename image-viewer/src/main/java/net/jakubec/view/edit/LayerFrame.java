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

package net.jakubec.view.edit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.jakubec.view.edit.plain.BasicLayer;
import net.jakubec.view.properties.VProperties;

public final class LayerFrame extends JInternalFrame {
	private class LayerPainter extends JPanel {

		LayerPainter() {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					int row = e.getY() / LAYER_HEIGHT;
					row = layers.size() - row - 1;
					if (selectedLayer != row) {
						if (row < 0 || row >= layers.size())
							return;
						selectedLayer = row;
						repaint();
						LayerEvent le = new LayerEvent(selectedLayer, layers.get(selectedLayer));
						listener.layerChanged(le);
					}

				}
			});
		}

		@Override
		public Dimension getPreferredSize() {
			if (layers == null) {
				return new Dimension(0, 0);
			}
			return new Dimension(1, (layers.size() + 1) * LAYER_HEIGHT);
		}

		@Override
		public void paint(Graphics g) {

			super.paint(g);
			if (layers == null) {
				return;
			}
			Iterator<BasicLayer> it = layers.iterator();
			int count = layers.size() - 1;
			while (it.hasNext()) {
				if (count == layers.size() - 1 - selectedLayer) {
					g.setColor(new Color(255, 200, 200));
					g.fillRect(0, count * LAYER_HEIGHT, getWidth(), LAYER_HEIGHT);
					g.setColor(Color.BLACK);
				}
				BufferedImage img = new BufferedImage(workingWidth, workingHeight,
						BufferedImage.TYPE_INT_ARGB);
				BasicLayer l = it.next();
				l.draw(img.getGraphics());
				g.drawLine(0, count * LAYER_HEIGHT, getWidth(), count * LAYER_HEIGHT);
				g.drawImage(img, 0, count * LAYER_HEIGHT + 1, LAYER_HEIGHT - 1, LAYER_HEIGHT - 1,
						this);

				g.drawString(l.getName(), LAYER_HEIGHT + 20, count * LAYER_HEIGHT + 20);

				count--;

			}
		}
	}

	private static final int LAYER_HEIGHT = 50;
	double factor;

	private List<BasicLayer> layers;

	private LayerSelectionListener listener;

	private JScrollPane scroll;
	private int selectedLayer;
	private int workingHeight;
	private int workingWidth;

	public LayerFrame() {
		this(null, 0, 0);
	}

	public LayerFrame(List<BasicLayer> l, int w, int h) {
		super(VProperties.getValue("edit.layer_manager"), true);
		layers = l;
		this.setSize(200, 200);
		scroll = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setContentPane(scroll);
		scroll.setViewportView(new LayerPainter());
		setVisible(true);

	}

	public void doRepaint() {
		scroll.revalidate();
		this.repaint();

	}

	public void help() {
		scroll.revalidate();

	}

	public void setNewLayers(LayerSelectionListener listener, List<BasicLayer> l, int w, int h) {
		layers = l;
		this.listener = listener;
		workingHeight = h;
		workingWidth = w;
		repaint();
	}

}
