package net.jakubec.view.listener;

import net.jakubec.view.edit.plain.BasicLayer;

public class LayerEvent {

	private BasicLayer basicLayer;

	private int layerNumber;

	public LayerEvent(int number, BasicLayer layer) {
		layerNumber = number;
		basicLayer = layer;
	}

	public BasicLayer getLayer() {
		return basicLayer;
	}

	public int getLayerNumber() {
		return layerNumber;
	}

}
