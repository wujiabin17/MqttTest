package com.sprocomm.com.mqtttest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MCDBOpenHelper extends SQLiteOpenHelper {

	private MCDBOpenHelper(Context context) {
		super(context, MobileCycleDB.NAME, null, MobileCycleDB.VERSION);
	}

	private static MCDBOpenHelper instance;

	public static MCDBOpenHelper getInstance(Context context) {
		if (instance == null) {
			synchronized (MCDBOpenHelper.class) {
				if (instance == null) {
					instance = new MCDBOpenHelper(context);
				}
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MobileCycleDB.Account.SQL_CREATE_TABLE);
		db.execSQL(MobileCycleDB.BackTask.SQL_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
