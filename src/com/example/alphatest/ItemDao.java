package com.example.alphatest;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/*
 * thread-safe singleton, contains methods to read and write data to db
 * */

public class ItemDao {
	
	private DbOpenHelper dbOpenHelper;
	private static ItemDao instance;
	private Context context;
	
	private ItemDao(Context context){
		if(this.context == null){
			this.context = context.getApplicationContext();
		}
		
		dbOpenHelper = new DbOpenHelper(this.context);
	}
	
	public static synchronized ItemDao getInstance(Context context){
		if(instance == null){
			instance = new ItemDao(context);
		}		
		return instance;
	}
	
	
	public synchronized void insertIfNotExists(List<Item> items){
		if(items == null){
			return;
		}
		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		
		for(Item item : items){
			ContentValues cv = new ContentValues();
	        cv.put(DbOpenHelper.TITLE_FIELD, item.getTitle());
	        cv.put(DbOpenHelper.LINK_FIELD, item.getLink());
	        cv.put(DbOpenHelper.PUBDATE_FIELD, item.getPubDate());
	        
	        db.insert(DbOpenHelper.TABLE_NAME, null, cv);
		}

        db.close();
	}
	
	public synchronized boolean contains(Item item){
		Cursor c = null;
		SQLiteDatabase db = null;
	    try {
	        db = dbOpenHelper.getReadableDatabase();
	        String query = "select count(*) from " + DbOpenHelper.TABLE_NAME + " where " + DbOpenHelper.TITLE_FIELD + " = ?";
	        c = db.rawQuery(query, new String[] {item.getTitle()});
	        
	        return c.moveToFirst();
	    }catch(Exception e){
	    	return false;
	    }finally {
	        if (c != null) {
	            c.close();
	        }
	        if (db != null) {
	            db.close();
	        }
	    }
	}
	
	public synchronized List<Item> getItems(){
		Cursor c = null;
		SQLiteDatabase db = null;
	    try {
	    	db = dbOpenHelper.getReadableDatabase();
	        String query = "select * from " + DbOpenHelper.TABLE_NAME;
	        c = db.rawQuery(query, null);
	        
	        List<Item> items = new ArrayList<Item>();
	        if(c.moveToFirst()){
	        	while(!c.isAfterLast()){
	        		String title = c.getString(c.getColumnIndex(DbOpenHelper.TITLE_FIELD));
	        		String link = c.getString(c.getColumnIndex(DbOpenHelper.LINK_FIELD));
	        		String pubDate = c.getString(c.getColumnIndex(DbOpenHelper.PUBDATE_FIELD));
	        		
	        		items.add(new Item(title, pubDate, link));

	                c.moveToNext();
	        	}
	        }
			
			return items;
	    }catch(Exception e){
	    	return null;
	    }finally {
	        if (c != null) {
	            c.close();
	        }
	        if (db != null) {
	            db.close();
	        }
	    }
	}
}
