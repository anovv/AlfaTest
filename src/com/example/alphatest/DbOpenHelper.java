package com.example.alphatest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
	
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "alphatest";	
	public static final String TABLE_NAME = "items";
	
	public static final String TITLE_FIELD = "title";
	public static final String LINK_FIELD = "link";
	public static final String PUBDATE_FIELD = "pub_date";
	
	private static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" + TITLE_FIELD + " TEXT UNIQUE, " + LINK_FIELD
			+ " TEXT," + PUBDATE_FIELD + " TEXT)";
	
	public DbOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
