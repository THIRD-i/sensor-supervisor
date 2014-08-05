package com.thirdi.sensorsupervisor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	//If you change the database schema, you must increment the database version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "SensorData.db";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DBContract.SQL_CREATE_ENTRIES);
	}

	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		//Discard data and start over when upgraded. (Needs to be changed.)
		database.execSQL(DBContract.SQL_DELETE_ENTRIES);
		onCreate(database);
	}

	public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		onUpgrade(database, oldVersion, newVersion);
	}
}