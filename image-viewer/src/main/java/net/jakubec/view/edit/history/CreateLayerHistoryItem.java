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
