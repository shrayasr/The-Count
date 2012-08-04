package com.thecount;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TheCountActivity extends Activity 
{
	TextView todayTextView;
	TextView sentTextView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        todayTextView = (TextView)findViewById(R.id.todayTextView);
        sentTextView = (TextView)findViewById(R.id.sentTextView);
        
        SMSObserver obs = new SMSObserver(new Handler(), this);
        
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, obs);
        
        doCalculate();
    }
    
    private void doCalculate()
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	SimpleDateFormat forDB = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Calendar now = Calendar.getInstance();
    	
    	todayTextView.setText("Texts sent today : "+sdf.format(now.getTime()));
    	
    	SQLite csqlHelper;
    	SQLiteDatabase csqlRead;
    	
    	csqlHelper = new SQLite(this);
		csqlRead = csqlHelper.getReadableDatabase();
		
		Cursor c = csqlRead.rawQuery("select count from counts where date = '"+forDB.format(now.getTime())+"'", null);
		
		if (!c.moveToFirst())
			sentTextView.setText("No Texts sent today");
		else
			sentTextView.setText(""+c.getString(c.getColumnIndex("count")));
		
		csqlRead.close();
    }
	
	@Override
	protected void onResume()
	{
		doCalculate();
		super.onResume();
	}
}