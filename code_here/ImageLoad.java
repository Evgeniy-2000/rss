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
    protected void onProgressUpdate(Integer... values) {
        View itemView = getViewByPosition(values[0]);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        imageView.setImageBitmap(((rss)(listView.getAdapter().getItem(values[0]))).getBitmap());
    }

    @Override
    protected void onPostExecute(ArrayList<rss> list) {
        mainActivity.imagesSaveToDb(list);
        super.onPostExecute(list);
    }

    public View getViewByPosition(int pos) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        }
        else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
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
