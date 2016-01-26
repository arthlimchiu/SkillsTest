package com.example.skillstest;

import com.example.skillstest.provider.ContactContentProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddActivity extends Activity {
	
	private int PICK_IMAGE = 1;
	
	private long mId = -1;
	
	private ImageView mImageView;
	private EditText mNameText, mPhoneText;
	
	private Uri mImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		mImageView = (ImageView) findViewById(R.id.iv_image);
		mNameText = (EditText) findViewById(R.id.et_name);
		mPhoneText = (EditText) findViewById(R.id.et_phone);
		mImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, PICK_IMAGE);
			}
		});
		
		Bundle b = getIntent().getExtras();
		if(b != null)	{
			mId = b.getLong("id", -1);
			mNameText.setText(b.getString("name"));
			mPhoneText.setText(b.getString("phone"));
			
			String image = b.getString("image");
			if(!TextUtils.isEmpty(image))	{
				mImage = Uri.parse(image);
				mImageView.setImageURI(mImage);
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())	{
		case R.id.save:
			checkForErrors();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK)	{
			mImage = data.getData();
			mImageView.setImageURI(mImage);
		}
	}
	
	private void checkForErrors()
	{
		String name = mNameText.getText().toString();
		String phone = mPhoneText.getText().toString();
		
		if(TextUtils.isEmpty(name) || name.equals(""))	{
			Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
		} else if(TextUtils.isEmpty(phone) || phone.equals(""))	{
			Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
		} else {
			if (mId == -1)	{
				addContact(name, phone);
			} else {
				editContact(name, phone);
			}
		}
	}
	
	private void addContact(String name, String phone)	{
		ContentValues cv = new ContentValues();
		
		cv.put(ContactTable.COLUMN_NAME, name);
		cv.put(ContactTable.COLUMN_PHONE, phone);
		
		if(mImage != null)	{
			cv.put(ContactTable.COLUMN_IMAGE, mImage.toString());
		}
		
		getContentResolver().insert(ContactContentProvider.CONTENT_URI_CONTACTS, cv);
		
		finish();
	}
	
	private void editContact(String name, String phone) {
		ContentValues cv = new ContentValues();
		
		cv.put(ContactTable.COLUMN_NAME, name);
		cv.put(ContactTable.COLUMN_PHONE, phone);
		
		if(mImage != null)	{
			cv.put(ContactTable.COLUMN_IMAGE, mImage.toString());
		}
		
		getContentResolver().update(Uri.withAppendedPath(ContactContentProvider.CONTENT_URI_CONTACTS, String.valueOf(mId)), cv, null, null);
		
		setResult(Activity.RESULT_OK);
		finish();
	}
}
