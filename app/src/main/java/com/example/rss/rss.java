package com.example.rss;

import android.graphics.Bitmap;

public class rss {
    private String title;
    private String date;
    private String content;
    private String link;
    private String linkImage;
    private Bitmap image;

    public rss(String title, String date, String content, String link, String linkImage) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.link = link;
        this.linkImage = linkImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getPreview() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public String getImageurl() {
        return linkImage;
    }

    public Bitmap getBitmap() {
        return image;
    }

    public void setBitmap(Bitmap bitmap) {
        this.image = bitmap;
    }
}
