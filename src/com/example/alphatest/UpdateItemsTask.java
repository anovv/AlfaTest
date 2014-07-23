package com.example.alphatest;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

/*
 * asynctask to get items from server and insert them into db*/

public class UpdateItemsTask extends AsyncTask<Void, Void, Void> {

	private OnUpdateItemsListener onUpdateItemsListener;
	private List<Item> items;
	private Context context;
	
	public UpdateItemsTask(OnUpdateItemsListener onUpdateItemsListener, Context context){
		this.onUpdateItemsListener = onUpdateItemsListener;
		this.context = context;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		items = Utils.getItemsFromServer();
		ItemDao.getInstance(context).insertIfNotExists(items);
		return null;
	}
	
	protected void onPostExecute(Void res){
		onUpdateItemsListener.onPostUpdate(items);
	}
}
