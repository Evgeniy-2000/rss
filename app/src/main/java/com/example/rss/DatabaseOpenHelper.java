package com.example.rss;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final int SCHEMA = 18;
    public static final String TABLE_RSSITEM = "rssitem";
    public static final String COLUMN_LASTRSSURL = "last";
    public static final String COLUMN_LINK = "link";
    public static final String TABLE_BITMAP = "bitmap";
    public static final String COLUMN_PREVIEW = "preview";
    public static final String TABLE_LASTRSSURL = "lastrssurl";
    public static final String COLUMN_TITLE = "title";
    private static final String DATABASE_NAME = "mydatabase.db";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IMAGEURL = "imageurl";
    public static final String COLUMN_BITMAPBYTES = "bitmapbytes";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists rssitem (_id integer primary key autoincrement, title String, preview String, date String, link String, imageurl String);");
        db.execSQL("create table if not exists bitmap (_id integer primary key autoincrement, imageurl String, bitmapbytes String);");
        db.execSQL("create table if not exists lastrssurl (_id integer primary key autoincrement, last String);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists rssitem");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }
}
