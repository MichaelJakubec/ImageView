package net.jakubec.view.history;

import net.jakubec.view.edit.plain.BasicLayer;

public class MoveHistoryItem implements HistoryItem {

	
	private BasicLayer plain;
	
	int divX;
	int divY;
	
	public MoveHistoryItem(BasicLayer p, int x, int y){
		divX= x;
		divY= y;
		plain= p;
	}
	
	@Override
	public void undo() {
		plain.moveImage(-divX, -divY);

	}

}
