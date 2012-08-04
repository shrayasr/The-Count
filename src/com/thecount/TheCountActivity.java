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

public class TheCountActivity extends Activity implements OnClickListener 
{
	Button initializeButton;
	
	TextView todayTextView;
	TextView sentTextView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initializeButton = (Button)findViewById(R.id.initButton);
        todayTextView = (TextView)findViewById(R.id.todayTextView);
        sentTextView = (TextView)findViewById(R.id.sentTextView);
        
        SMSObserver obs = new SMSObserver(new Handler(), this);
        
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, obs);
        
        initializeButton.setOnClickListener(this);
        
        doCalculate();
    }
    
    private void doCalculate()
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    	SimpleDateFormat forDB = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Calendar now = Calendar.getInstance();
    	
    	todayTextView.setText(sdf.format(now.getTime()));
    	
    	SQLite csqlHelper;
    	SQLiteDatabase csqlRead;
    	
    	csqlHelper = new SQLite(this);
		csqlRead = csqlHelper.getReadableDatabase();
		
		Cursor c = csqlRead.rawQuery("select count from counts where date = '"+forDB.format(now.getTime())+"'", null);
		
		c.moveToFirst();
		
		sentTextView.setText(""+c.getString(c.getColumnIndex("count")));
		
		csqlRead.close();
    }

	@Override
	public void onClick(View v)
	{
		if (v.getId() == initializeButton.getId())
		{
			this.deleteDatabase("counter.sqlite");
		}
	}
	
	@Override
	protected void onResume()
	{
		doCalculate();
		super.onResume();
	}
}