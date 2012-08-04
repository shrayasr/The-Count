package com.thecount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SMSObserver extends ContentObserver
{
	private Context _context;
	
	SQLite csqlHelper;
	SQLiteDatabase csqlRead;
	SQLiteDatabase csqlWrite;
	
    static final Uri SMS_STATUS_URI = Uri.parse("content://sms");
    
    static ArrayList<String> smsIds = new ArrayList<String>();

	public SMSObserver(Handler handler, Context context)
	{
		super(handler);
		_context = context;
	}
	
	/*
	 * TODO finish this
	
	public void initDay()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
	    Calendar now = Calendar.getInstance();
	    now.setTime(now.getTime());
		
		Cursor cur = _context.getContentResolver().query(SMS_STATUS_URI, null, null, null, null);
		
		cur.moveToFirst();
		
		csqlHelper = new SQLite(_context.getApplicationContext());
		
		csqlWrite = csqlHelper.getWritableDatabase();
		
		csqlWrite.execSQL("delete from counts where date = '"+sdf.format(now.getTime())+"'");
		csqlWrite.execSQL("insert into counts values ('"+sdf.format(now.getTime())+"',0)");
		
		while (!cur.isAfterLast())
		{
			String protocol = cur.getString(cur.getColumnIndex("protocol")); //protocol
    		
			if (protocol == null)
			{
	    	    Calendar smsSent = Calendar.getInstance();
	    	    
	    		smsSent.setTime(new Date(cur.getLong(cur.getColumnIndex("date"))));
	    		
	    		if ((now.get(Calendar.DAY_OF_MONTH) - smsSent.get(Calendar.DAY_OF_MONTH) == 0) &&
	    				(now.get(Calendar.MONTH) - smsSent.get(Calendar.MONTH) == 0) &&
	    				(now.get(Calendar.YEAR) - smsSent.get(Calendar.YEAR) == 0))
	    		{
	    			
	    		}
			}
			
    		
		}
	}
	*/
	
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
	    
	    String id = cur.getString(cur.getColumnIndex("_id"));
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
	    Calendar now = Calendar.getInstance();
	    Calendar smsSent = Calendar.getInstance();
	    
		now.setTime(now.getTime());
		smsSent.setTime(new Date(cur.getLong(cur.getColumnIndex("date"))));
	    
	    if (smsIds.contains(id))
	    	return;
	    else
	    {
	    	if (smsIds.size() == 10)
		    	smsIds.clear();
	    	smsIds.add(id);
	    }
	    
	    String protocol = cur.getString(cur.getColumnIndex("protocol")); //protocol
	    if (protocol == null)
	    {
	    	Toast.makeText(_context.getApplicationContext(), "Sending",Toast.LENGTH_SHORT).show();
	    	
	    	try
	    	{
	    		csqlHelper = new SQLite(_context.getApplicationContext());
	    		
	    		csqlRead = csqlHelper.getReadableDatabase();
	    		csqlWrite = csqlHelper.getWritableDatabase();
	    		
	    		Cursor c = csqlRead.rawQuery("select count from counts where date = '"+sdf.format(smsSent.getTime())+"'", null);
	    		
	    		if (c.getCount() == 0)
	    		{
	    			Log.v("thecount", "first insert");
	    			
	    			csqlWrite.execSQL("insert into counts values ('"+sdf.format(smsSent.getTime())+"',1)");
	    		}
	    		
	    		else
	    		{
	    			c.moveToFirst();
	    			
	    			int count = Integer.parseInt(c.getString(c.getColumnIndex("count")));
	    			
	    			csqlWrite.execSQL("update counts set count = "+(count+1)+" where date ='"+sdf.format(smsSent.getTime())+"'");
	    			
	    			Log.v("thecount", "Updating");
	    		}
	    		
	    		csqlRead.close();
	    	}
	    	catch (Exception ex)
	    	{
	    		ex.printStackTrace();
	    	}
	    	
	    }
	    //else Toast.makeText(_context.getApplicationContext(), "Receiving", Toast.LENGTH_SHORT).show();
		
		super.onChange(selfChange);
	}

}
