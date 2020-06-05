package com.example.rss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class rssAdapter extends ArrayAdapter {
    private final Context context;
    private ArrayList<rss> items;
    private TextView title;
    private TextView date;
    private TextView preview;
    private ImageView image;

    public rssAdapter(@NonNull Context context, ArrayList<rss> items) {
        super(context, R.layout.rss_view, items);
        this.context = context;
        this.items = items;
    }

    public void setArray(ArrayList<rss> list)
    {
        items.clear();
        for (rss item : list) {
            items.add(item);
        }
        notifyDataSetChanged();
    }

    public ArrayList<rss> getItems() {
        return items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.rss_view, parent, false);
        rss item = items.get(position);
        //TODO: image
        title = view.findViewById(R.id.title);
        date  = view.findViewById(R.id.date);
        preview = view.findViewById(R.id.content);
        image = view.findViewById(R.id.imageView);
        title.setText(item.getTitle());
        date.setText(item.getDate());
        preview.setText(item.getPreview());

        if (item.getBitmap() != null) {
            image.setImageBitmap(item.getBitmap());
        }
        else {
            image.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }
        return view;
    }

}
