package com.thecount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLite extends SQLiteOpenHelper
{
	
	private static final String DATABASE_NAME = "counter.sqlite";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "CREATE TABLE \"counts\" (\"date\" VARCHAR(10) NOT NULL , \"count\" INTEGER NOT NULL )";

	public SQLite(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.v("thecount", "Creating database & tables");
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}

}
