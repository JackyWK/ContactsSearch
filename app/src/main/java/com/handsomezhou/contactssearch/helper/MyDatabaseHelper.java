package com.handsomezhou.contactssearch.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper
{
	final String CREATE_ContactTable_SQL =
			"CREATE TABLE contact (" +
					"    _id              INTEGER PRIMARY KEY AUTOINCREMENT," +
					"    contact_name     TEXT    NOT NULL," +
					"    contact_bdnumber TEXT    NOT NULL," +
					"    contact_pbnumber TEXT    NOT NULL," +
					"    sort_key         TEXT" +
					");";
	final String CREATE_SmsTable_SQL=
			"CREATE TABLE minid_sms (" +
					"    _id          INTEGER PRIMARY KEY AUTOINCREMENT," +
					"    sms_bdnumber TEXT    NOT NULL," +
					"    sms_pbnumber TEXT," +
					"    sms_date     TEXT," +
					"    sms_readnot  INTEGER DEFAULT 2," +
					"    sms_type     INTEGER," +
					"    sms_content  TEXT," +
					"    sms_count    INTEGER DEFAULT 1," +
					"    sms_success  INTEGER DEFAULT 1" +
					");";
	public MyDatabaseHelper(Context context, String name, int version)
	{
		super(context, name, null, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// 第一次使用数据库时自动建表
		db.execSQL(CREATE_ContactTable_SQL);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db
			, int oldVersion, int newVersion)
	{
		System.out.println("--------onUpdate Called--------"
				+ oldVersion + "--->" + newVersion);
	}
}
