package com.example.skillstest;

import android.database.sqlite.SQLiteDatabase;

public class ContactTable {

	public static final String TABLE_NAME = "contacts";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PHONE = "phone";
	public static final String COLUMN_IMAGE = "image";
	
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_NAME
			+ "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_NAME + " text, "
			+ COLUMN_PHONE + " text, "
			+ COLUMN_IMAGE + " text"
			+ ")";
	
	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase database)
	{
		// Put upgrade statements here
	}
}
