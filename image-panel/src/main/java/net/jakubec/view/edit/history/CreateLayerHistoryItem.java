package net.jakubec.view.edit.history;

import java.util.List;

import net.jakubec.view.edit.plain.BasicLayer;

public final class CreateLayerHistoryItem implements HistoryItem {

	BasicLayer layer;
	List<BasicLayer> stack;
	
	public CreateLayerHistoryItem(BasicLayer l, List<BasicLayer> st){
		this.layer=l;
		stack=st;
		
	}
	@Override
	public void undo() {
		stack.remove(layer);

	}

}
