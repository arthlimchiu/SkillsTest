package com.example.skillstest;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsAdapter extends CursorAdapter {

	public ContactsAdapter(Context context, Cursor c) {
		super(context, c, 0);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		ViewHolder holder = (ViewHolder) arg0.getTag();
		
		String image = arg2.getString(arg2.getColumnIndex(ContactTable.COLUMN_IMAGE));
		String name = arg2.getString(arg2.getColumnIndex(ContactTable.COLUMN_NAME));
		String phone = arg2.getString(arg2.getColumnIndex(ContactTable.COLUMN_PHONE));
		
		if(TextUtils.isEmpty(image))	{
			holder.mImage.setImageResource(R.drawable.ic_launcher);
		} else {
			holder.mImage.setImageURI(Uri.parse(image));
		}
		
		holder.mName.setText(name);
		holder.mPhone.setText(phone);
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		View v = LayoutInflater.from(arg0).inflate(R.layout.item_contact, arg2, false);
		
		ViewHolder holder = new ViewHolder();
		holder.mImage = (ImageView) v.findViewById(R.id.item_image);
		holder.mName = (TextView) v.findViewById(R.id.item_name);
		holder.mPhone = (TextView) v.findViewById(R.id.item_phone);
		
		v.setTag(holder);
		
		return v;
	}

	class ViewHolder	{
		ImageView mImage;
		TextView mName;
		TextView mPhone;
	}

}
