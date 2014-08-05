package com.thirdi.sensorsupervisor;

import android.provider.BaseColumns;

public final class DBContract {
	private static final String TEXT_TYPE = " TEXT";
	private static final String REAL_TYPE = " REAL";
	private static final String COMMA_SEP = ",";
	protected static final String SQL_CREATE_ENTRIES = 
		"CREATE TABLE " + DBEntry.TABLE_NAME + " (" +
			DBEntry._ID + "INTEGER PRIMARY KEY," +
			DBEntry.COLUMN_NAME_SENSOR_ID + TEXT_TYPE + COMMA_SEP +
			DBEntry.COLUMN_NAME_SENSOR_NAME + TEXT_TYPE + COMMA_SEP +
			DBEntry.COLUMN_NAME_VALUE_X + REAL_TYPE + COMMA_SEP +
			DBEntry.COLUMN_NAME_VALUE_Y + REAL_TYPE + COMMA_SEP +
			DBEntry.COLUMN_NAME_VALUE_Z + REAL_TYPE + " )";
	
	protected static final String SQL_DELETE_ENTRIES =
			"DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME;

	//To prevent accidental instantiation, empty constructor is required.
	public DBContract() {}

	/* Inner class that defines the table contents */
	public static abstract class DBEntry implements BaseColumns {
		public static final String TABLE_NAME = "sensor_data";
		public static final String COLUMN_NAME_SENSOR_ID = "sensorid";
		public static final String COLUMN_NAME_SENSOR_NAME = "sensorname";
		public static final String COLUMN_NAME_VALUE_X = "valuex";
		public static final String COLUMN_NAME_VALUE_Y = "valuey";
		public static final String COLUMN_NAME_VALUE_Z = "valuez";
	}
}