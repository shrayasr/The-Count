package com.thecount;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class SMSLogger extends Activity
{
	
	public void registerChange()
	{
		Uri uriSms = Uri.parse("content://sms/");
		
		Cursor cur = getContentResolver().query(uriSms, null, null, null, null);
		
		while (cur.moveToNext())
		{
			String protocol = cur.getString(cur.getColumnIndex("protocol"));
			String date = cur.getString(cur.getColumnIndex("date"));
			
			Log.v("thecount",protocol+"|"+date);
		}
	}
	
}
