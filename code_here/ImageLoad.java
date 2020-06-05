package com.example.rss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import java.net.URL;
import java.util.ArrayList;

public class ImageLoad extends AsyncTask<ArrayList<rss>, Integer, ArrayList<rss>> {
    private  MainActivity mainActivity;
    private ListView listView;
    private  int current;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<rss> list) {
        mainActivity.imagesSaveToDb(list);
        super.onPostExecute(list);
    }

    public ImageLoad(ListView listView, MainActivity mainActivity) {
        this.listView = listView;
        this.mainActivity = mainActivity;
        current = 0;
    }

    @Override
    protected ArrayList<rss> doInBackground(ArrayList<rss>... arrayLists) {
        ArrayList<rss> array = arrayLists[0];
        String link = "";
        Bitmap bmp;
        try {
            for (rss item : array) {
                link = item.getImageurl();
                bmp = BitmapFactory.decodeStream(new URL(link).openConnection().getInputStream());
                item.setBitmap(bmp);
                publishProgress(current);
                current++;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return array;
    }
}
