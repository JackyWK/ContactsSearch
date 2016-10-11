package com.handsomezhou.contactssearch.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.handsomezhou.contactssearch.helper.MyDatabaseHelper;
import com.handsomezhou.contactssearch.model.Contacts;

/**
 * Created by lenovo on 2016/10/9.
 */
public class ContactProvier extends ContentProvider {
    private MyDatabaseHelper dbOpenhelper;

    @Override
    public boolean onCreate() {
        dbOpenhelper = new MyDatabaseHelper(this.getContext(), "BDcommunication_contact.db", 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbOpenhelper.getReadableDatabase();
        return db.query("contact", projection, selection, selectionArgs, sortOrder, null, null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbOpenhelper.getReadableDatabase();
        long rowId = db.insert("contact", Contacts.Contact.sortKey, values);
        // 如果插入成功返回uri
        if (rowId > 0) {
            // 在已有的 Uri的后面追加ID
            Uri wordUri = ContentUris.withAppendedId(uri, rowId);
            // 通知数据已经改变
            //getContext()方法获得调用insert方法的Context对象，再利用这个Context对象来获取一个ContentResolver的对象。
            // notifyChange()方法则用来通知注册在此URI上的观察者（observer）数据发生了改变;null表示发送消息给任何人
            getContext().getContentResolver().notifyChange(wordUri, null);
            return wordUri;
        } else throw new IllegalArgumentException("未知Uri:" + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
