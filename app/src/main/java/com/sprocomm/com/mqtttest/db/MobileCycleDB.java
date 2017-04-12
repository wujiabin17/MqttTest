package com.sprocomm.com.mqtttest.db;

public interface MobileCycleDB {
	String NAME = "hm.db";
	int VERSION = 1;

	public interface Account {
		String TABLE_NAME = "account";

		String COLUMN_ID = "_id";
		String COLUMN_ACCOUNT = "account";
		String COLUMN_NAME = "name";
		String COLUMN_SEX = "sex";
		String COLUMN_ICON = "icon";
		String COLUMN_SIGN = "sign";
		String COLUMN_AREA = "area";
		String COLUMN_TOKEN = "token";
		String COLUMN_CURRENT = "current";

		String SQL_CREATE_TABLE = "create table " + TABLE_NAME + " ("
				+ COLUMN_ID + " integer primary key autoincrement, "
				+ COLUMN_ACCOUNT + " text," + COLUMN_NAME + " text,"
				+ COLUMN_SEX + " integer," + COLUMN_ICON + " text,"
				+ COLUMN_SIGN + " text," + COLUMN_AREA + " text,"
				+ COLUMN_TOKEN + " text," + COLUMN_CURRENT + " integer" + ")";
	}
}
