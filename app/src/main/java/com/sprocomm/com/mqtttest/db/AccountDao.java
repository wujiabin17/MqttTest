package com.sprocomm.com.mqtttest.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sprocomm.com.mqtttest.domain.Account;

public class AccountDao {
	private MCDBOpenHelper helper;

	public AccountDao(Context context) {
		helper = MCDBOpenHelper.getInstance(context);
	}

	public List<Account> getAllAccount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + MobileCycleDB.Account.TABLE_NAME;
		Cursor cursor = db.rawQuery(sql, null);

		List<Account> list = null;
		if (cursor != null) {
			while (cursor.moveToNext()) {
				if (list == null) {
					list = new ArrayList<Account>();
				}
				Account account = new Account();

				account.setAccount(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_ACCOUNT)));
				account.setArea(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_AREA)));
				account.setCurrent(cursor.getInt(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_CURRENT)) == 1);
				account.setIcon(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_ICON)));
				account.setName(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_NAME)));
				account.setSex(cursor.getInt(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_SEX)));
				account.setSign(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_SIGN)));
				account.setToken(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_TOKEN)));
				list.add(account);
			}
		}
		return list;
	}

	public Account getCurrentAccount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + MobileCycleDB.Account.TABLE_NAME + " where "
				+ MobileCycleDB.Account.COLUMN_CURRENT + "=1";
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Account account = new Account();

				account.setAccount(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_ACCOUNT)));
				account.setArea(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_AREA)));
				account.setCurrent(cursor.getInt(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_CURRENT)) == 1);
				account.setIcon(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_ICON)));
				account.setName(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_NAME)));
				account.setSex(cursor.getInt(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_SEX)));
				account.setSign(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_SIGN)));
				account.setToken(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_TOKEN)));
				return account;
			}
		}
		return null;
	}

	public Account getByAccount(String account) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from " + MobileCycleDB.Account.TABLE_NAME + " where "
				+ MobileCycleDB.Account.COLUMN_ACCOUNT + "=?";
		Cursor cursor = db.rawQuery(sql, new String[] { account });

		if (cursor != null) {
			while (cursor.moveToNext()) {
				Account a = new Account();

				a.setAccount(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_ACCOUNT)));
				a.setArea(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_AREA)));
				a.setCurrent(cursor.getInt(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_CURRENT)) == 1);
				a.setIcon(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_ICON)));
				a.setName(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_NAME)));
				a.setSex(cursor.getInt(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_SEX)));
				a.setSign(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_SIGN)));
				a.setToken(cursor.getString(cursor
						.getColumnIndex(MobileCycleDB.Account.COLUMN_TOKEN)));
				return a;
			}
		}
		return null;
	}

	public void addAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MobileCycleDB.Account.COLUMN_ACCOUNT, account.getAccount());
		values.put(MobileCycleDB.Account.COLUMN_AREA, account.getArea());
		values.put(MobileCycleDB.Account.COLUMN_ICON, account.getIcon());
		values.put(MobileCycleDB.Account.COLUMN_NAME, account.getName());
		values.put(MobileCycleDB.Account.COLUMN_SEX, account.getSex());
		values.put(MobileCycleDB.Account.COLUMN_SIGN, account.getSign());
		values.put(MobileCycleDB.Account.COLUMN_TOKEN, account.getToken());
		values.put(MobileCycleDB.Account.COLUMN_CURRENT, account.isCurrent() ? 1 : 0);

		db.insert(MobileCycleDB.Account.TABLE_NAME, null, values);
	}

	public void updateAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(MobileCycleDB.Account.COLUMN_AREA, account.getArea());
		values.put(MobileCycleDB.Account.COLUMN_ICON, account.getIcon());
		values.put(MobileCycleDB.Account.COLUMN_NAME, account.getName());
		values.put(MobileCycleDB.Account.COLUMN_SEX, account.getSex());
		values.put(MobileCycleDB.Account.COLUMN_SIGN, account.getSign());
		values.put(MobileCycleDB.Account.COLUMN_TOKEN, account.getToken());
		values.put(MobileCycleDB.Account.COLUMN_CURRENT, account.isCurrent() ? 1 : 0);

		String whereClause = MobileCycleDB.Account.COLUMN_ACCOUNT + "=?";
		String[] whereArgs = new String[] { account.getAccount() };
		db.update(MobileCycleDB.Account.TABLE_NAME, values, whereClause, whereArgs);
	}

	public void deleteAccount(Account account) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String whereClause = MobileCycleDB.Account.COLUMN_ACCOUNT + "=?";
		String[] whereArgs = new String[] { account.getAccount() };
		db.delete(MobileCycleDB.Account.TABLE_NAME, whereClause, whereArgs);
	}
}