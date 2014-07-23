package com.example.alphatest;

/*
 * callback for server queries
 * */

import java.util.List;

public interface OnUpdateItemsListener {
	
	public void onPostUpdate(List<Item> items);
}
