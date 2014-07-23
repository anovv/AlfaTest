package com.example.alphatest;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

/*
 * async task to read items from db*/

public class FetchItemsFromDbTask extends AsyncTask<Void, Void, Void>{
	
	private OnUpdateItemsListener onUpdateItemsListener;
	private List<Item> items;
	private Context context;
	
	public FetchItemsFromDbTask(OnUpdateItemsListener onUpdateItemsListener, Context context){
		this.onUpdateItemsListener = onUpdateItemsListener;
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		items = ItemDao.getInstance(context).getItems();
		return null;
	}
	
	protected void onPostExecute(Void res){
		onUpdateItemsListener.onPostUpdate(items);
	}
}
