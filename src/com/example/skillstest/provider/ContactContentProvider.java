package com.example.skillstest.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.skillstest.ContactDatabaseHelper;
import com.example.skillstest.ContactTable;

public class ContactContentProvider extends ContentProvider {
	
	private ContactDatabaseHelper database;
	
	// Used for UriMatcher
	private static final int CONTACTS = 1;
	private static final int CONTACT_ID = 2;
	
	private static final String AUTHORITY = "com.example.skillstest.provider";
	private static final String PATH_CONTACTS = "contacts"; // the table name
	
	public static final Uri CONTENT_URI_CONTACTS = Uri.parse("content://" + AUTHORITY + "/" + PATH_CONTACTS);
	
	private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sUriMatcher.addURI(AUTHORITY, PATH_CONTACTS, CONTACTS);
		sUriMatcher.addURI(AUTHORITY, PATH_CONTACTS + "/#", CONTACT_ID);
	}
	
	@Override
	public boolean onCreate() {
		database = new ContactDatabaseHelper(getContext());
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sUriMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		int rowsDeleted;
		
		String id;
		
		switch(uriType)	{
		case CONTACTS:
			rowsDeleted = db.delete(ContactTable.TABLE_NAME, selection, selectionArgs);
			break;
		case CONTACT_ID:
			id = uri.getLastPathSegment();
			
			if(TextUtils.isEmpty(selection))	{
				rowsDeleted = db.delete(ContactTable.TABLE_NAME, ContactTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(ContactTable.TABLE_NAME, ContactTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sUriMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		
		long id;
		String path;
		
		switch(uriType)	{
		case CONTACTS:
			id = db.insert(ContactTable.TABLE_NAME, null, values);
			path = PATH_CONTACTS;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(path + "/" + id);
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		
		int uriType = sUriMatcher.match(uri);
		switch(uriType)	{
		case CONTACTS:
			builder.setTables(ContactTable.TABLE_NAME);
			break;
		case CONTACT_ID:
			builder.setTables(ContactTable.TABLE_NAME);
			builder.appendWhere(ContactTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		}
		
		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sUriMatcher.match(uri);
		SQLiteDatabase db = database.getWritableDatabase();
		int rowsUpdated;
		
		String id;
		
		switch(uriType)	{
		case CONTACTS:
			rowsUpdated = db.update(ContactTable.TABLE_NAME, values, selection, selectionArgs);
			break;
		case CONTACT_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))	{
				rowsUpdated = db.update(ContactTable.TABLE_NAME, values, ContactTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = db.update(ContactTable.TABLE_NAME, values, ContactTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return rowsUpdated;
	}

}
