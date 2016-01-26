package com.example.skillstest;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.example.skillstest.provider.ContactContentProvider;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor> {

	private int CONTACT_LOADER = 1;

	private ListView mContactList;
	private EditText mSearchText;

	private ContactsAdapter mAdapter;
	private CursorLoader mCursorLoader;

	private String selection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContactList = (ListView) findViewById(R.id.contacts_list);
		mContactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MainActivity.this, DetailsActivity.class);				
				intent.putExtra("id", arg3);
				Log.d("MainActivity", "" + arg3);
				startActivity(intent);

			}
		});

		mSearchText = (EditText) findViewById(R.id.et_search);
		mSearchText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				displayResults(arg0.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		getLoaderManager().initLoader(CONTACT_LOADER, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.add:
			Intent i = new Intent(this, AddActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = new String[] { ContactTable.COLUMN_ID,
				ContactTable.COLUMN_NAME, ContactTable.COLUMN_PHONE,
				ContactTable.COLUMN_IMAGE };

		mCursorLoader = new CursorLoader(this,
				ContactContentProvider.CONTENT_URI_CONTACTS, projection,
				selection, null, "name asc");
		
		return mCursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mAdapter = new ContactsAdapter(this, arg1);
		mContactList.setAdapter(mAdapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.changeCursor(null);
	}

	private void displayResults(String text) {
		selection = ContactTable.COLUMN_NAME + " LIKE '%" + text + "%'";
		getLoaderManager().restartLoader(CONTACT_LOADER, null, this);
	}
}
