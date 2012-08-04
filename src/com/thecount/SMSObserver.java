package com.thecount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SMSObserver extends ContentObserver
{
	private Context _context;
	
    static final Uri SMS_STATUS_URI = Uri.parse("content://sms");
    
    static ArrayList<String> smsIds = new ArrayList<String>();

	public SMSObserver(Handler handler, Context context)
	{
		super(handler);
		_context = context;
	}
	
	@Override
	public boolean deliverSelfNotifications()
	{
		return true;
	}
	
	@Override
	public void onChange(boolean selfChange)
	{
		Cursor cur = _context.getContentResolver().query(SMS_STATUS_URI, null, null, null, null);
		
	    cur.moveToNext();
	    
	    String time = cur.getString(cur.getColumnIndex("date")); //date
	    String id = cur.getString(cur.getColumnIndex("_id")); //id
	    
	    Calendar now = Calendar.getInstance();
		now.setTime(now.getTime());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		Date smsSentDate = new Date(cur.getLong(cur.getColumnIndex("date")));
		Date todaysDate = now.getTime();
		
	    Log.v("thecount","diff:"+(todaysDate.getDay() - smsSentDate.getDay()));
	    
	    if (smsIds.contains(id))
	    	return;
	    else
	    {
	    	if (smsIds.size() > 10)
		    	smsIds.clear();
	    	smsIds.add(id);
	    }
	    
	    String protocol = cur.getString(cur.getColumnIndex("protocol")); //protocol
	    if (protocol == null)
	    	Toast.makeText(_context.getApplicationContext(), "Sending",Toast.LENGTH_SHORT).show();
	    else Toast.makeText(_context.getApplicationContext(), "Receiving", Toast.LENGTH_SHORT).show();
		
		super.onChange(selfChange);
	}

}
