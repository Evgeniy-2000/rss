package com.example.rss;

import android.os.AsyncTask;
import java.util.ArrayList;

public class ImageSave extends AsyncTask<ArrayList<rss>, Integer, Integer> {
    private Database database;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
    }


    @Override
    protected Integer doInBackground(ArrayList<rss>... arrayLists) {
        ArrayList<rss> array = arrayLists[0];
        database.clearImages();
        for (rss item : array)
        {
            if (item.getBitmap() != null)
            {
                database.insertBitmap(item.getBitmap(), item.getImageurl());
            }
        }
        return 0;
    }

    public ImageSave(Database database) {
        this.database = database;
    }
}
