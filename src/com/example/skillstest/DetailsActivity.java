package com.example.skillstest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.skillstest.R.layout;
import com.example.skillstest.provider.ContactContentProvider;

public class DetailsActivity extends Activity {

	private int EDIT_CONTACT = 1;

	private long mId;

	private ImageView mImageView;
	private TextView mNameText, mPhoneText;
	
	private String mName;
	private String mPhone;
	private String mImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);

		Intent i = getIntent();
		mId = i.getLongExtra("id", -1);

		mImageView = (ImageView) findViewById(R.id.details_image);
		mNameText = (TextView) findViewById(R.id.details_name);
		mPhoneText = (TextView) findViewById(R.id.details_phone);

		populateDetails();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit:
			Intent intent = new Intent(this, AddActivity.class);
			Bundle b = new Bundle();
			b.putLong("id", mId);
			b.putString("name", mName);
			b.putString("phone", mPhone);
			b.putString("image", mImage);
			intent.putExtras(b);
			startActivityForResult(intent, EDIT_CONTACT);
			return true;
		case R.id.delete:
			getContentResolver().delete(Uri.withAppendedPath(ContactContentProvider.CONTENT_URI_CONTACTS, String.valueOf(mId)), null, null);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)	{
			populateDetails();
		}
	}

	private void populateDetails() {
		String[] projection = new String[] { ContactTable.COLUMN_IMAGE,
				ContactTable.COLUMN_NAME, ContactTable.COLUMN_PHONE };
		Cursor cursor = getContentResolver().query(
				Uri.withAppendedPath(
						ContactContentProvider.CONTENT_URI_CONTACTS,
						String.valueOf(mId)), projection, null, null, null);
		
		if(cursor.moveToNext())	{
			mImage = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_IMAGE));
			mName = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_NAME));
			mPhone = cursor.getString(cursor.getColumnIndex(ContactTable.COLUMN_PHONE));
		}		
		
		cursor.close();
		
		if(TextUtils.isEmpty(mImage))	{
			mImageView.setImageResource(R.drawable.ic_launcher);
		} else {
			mImageView.setImageURI(Uri.parse(mImage));
		}
		
		mNameText.setText(mName);
		mPhoneText.setText(mPhone);
	}
}
