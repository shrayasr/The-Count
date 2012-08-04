package com.thecount;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class TheCountActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SMSObserver obs = new SMSObserver(new Handler(), this);
        
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, obs);
    }
}