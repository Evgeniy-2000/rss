package com.example.rss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Database {
    public static final String TAG = "Repository";
    private DatabaseOpenHelper dbhelper;
    private SQLiteDatabase db;

    public Database(Context context) {
        dbhelper = new DatabaseOpenHelper(context);
    }

    public void open() {
        try{
            db = dbhelper.getWritableDatabase();
        }
        catch (SQLiteException ex)
        {
            db = dbhelper.getReadableDatabase();
        }
    }

    public void close() {
        dbhelper.close();
    }

    public void insertItems(ArrayList<rss> list) {
        for (rss item: list) {
            insertItem(item);
        }
    }

    public void clear(){
        db.execSQL("delete from rssitem;");
        db.execSQL("delete from bitmap;");
    }

    public String getLastUrl()
    {
        Cursor cursor = db.query(DatabaseOpenHelper.TABLE_LASTRSSURL, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            String last = cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_LASTRSSURL));
            return  last;
        }
        return "";
    }

    public void setLastUrl(String lastrssurl) {
        db.execSQL("delete from lastrssurl;");
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_LASTRSSURL, lastrssurl);
        db.insert(DatabaseOpenHelper.TABLE_LASTRSSURL, "_", values);
    }

    public void clearImages(){
        db.execSQL("delete from bitmap;");
    }

    public long insertBitmap(Bitmap bitmap, String imageurl) {
        ContentValues valuesBitmap = new ContentValues();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] b = baos.toByteArray();
        String bitmapstr = Base64.encodeToString(b, Base64.DEFAULT);
        valuesBitmap.put(DatabaseOpenHelper.COLUMN_BITMAPBYTES, bitmapstr);
        valuesBitmap.put(DatabaseOpenHelper.COLUMN_IMAGEURL, imageurl);
        return db.insert(DatabaseOpenHelper.TABLE_BITMAP, "_", valuesBitmap);
    }

    public long insertItem(rss item) {
        if (item.getBitmap() != null) {
            ContentValues valuesBitmap = new ContentValues();
            valuesBitmap.put(DatabaseOpenHelper.COLUMN_IMAGEURL, item.getImageurl());
            Bitmap bitmap = item.getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte[] b = baos.toByteArray();
            String bitmapstr = Base64.encodeToString(b, Base64.DEFAULT);
            valuesBitmap.put(DatabaseOpenHelper.COLUMN_BITMAPBYTES, bitmapstr);
            db.insert(DatabaseOpenHelper.TABLE_BITMAP, "_", valuesBitmap);
        }
        return db.insert(DatabaseOpenHelper.TABLE_RSSITEM, "_", getValues(item));
    }

    ContentValues getValues(rss item){
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.COLUMN_TITLE, item.getTitle());
        values.put(DatabaseOpenHelper.COLUMN_PREVIEW, item.getPreview());
        values.put(DatabaseOpenHelper.COLUMN_DATE, item.getDate());
        values.put(DatabaseOpenHelper.COLUMN_LINK, item.getLink());
        values.put(DatabaseOpenHelper.COLUMN_IMAGEURL, item.getImageurl());
        return values;
    }

    public ArrayList<rss> getItems()
    {
        String title;
        String preview;
        String link;
        String imageurl;
        String date;
        ArrayList<rss> result = new ArrayList<>();
        Cursor cursor = db.query(DatabaseOpenHelper.TABLE_RSSITEM, null, null, null, null, null, null);
        if (cursor.moveToFirst())
            do {
                title = cursor.getString(cursor.getColumnIndex("title"));
                preview = cursor.getString(cursor.getColumnIndex("preview"));
                date = cursor.getString(cursor.getColumnIndex("date"));
                link = cursor.getString(cursor.getColumnIndex("link"));
                imageurl = cursor.getString(cursor.getColumnIndex("imageurl"));
                rss rssItem = new rss(title, date, preview, link, imageurl);
                try {
                    if (!imageurl.equals("")) {
                        Log.d(TAG, "select * from bitmap where imageurl = \"" + imageurl + "\";");
                        Cursor cursor1 = db.rawQuery("select * from bitmap where imageurl = \"" + imageurl + "\";", null);
                        if (cursor1.moveToFirst()) {
                            String bitmapBytes = cursor1.getString(cursor1.getColumnIndex(DatabaseOpenHelper.COLUMN_BITMAPBYTES));
                            byte [] encodeByte = Base64.decode(bitmapBytes, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            rssItem.setBitmap(bitmap);
                        }
                    }
                }
                catch (Exception ex) {

                }
                result.add((rssItem));
            }
            while(cursor.moveToNext());
        cursor.close();
        return result;
    }
}
