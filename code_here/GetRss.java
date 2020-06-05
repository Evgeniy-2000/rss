package com.example.rss;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class GetRss extends AsyncTask<String, Void, ArrayList<rss>> {
    private MainActivity activity;
    private final String TAG = "randomTag";
    private String link;

    public GetRss(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<rss> doInBackground(String... urls) {
        try {
            link = urls[0];
            return getRss(urls[0]);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<rss> result) {
        super.onPostExecute(result);
        if (result != null) {
            activity.onLoadFinished(result);
            activity.saveToDb(result, link);
        }
        else{
            Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
        }
    }

    //parser
    private ArrayList<rss> getRss(String url)throws SAXException, IOException, ParserConfigurationException {
        String title = "", link = "", description = "";
        ArrayList<rss> items = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' hh.mm", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(url);
        document.getDocumentElement().normalize();
        NodeList itemList = document.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            String URL = "", extraURL = "";

            if (i > 10) {
                break;
            }

            Element item = (Element) itemList.item(i);
            NodeList list = item.getElementsByTagName("link");

            if (list.getLength() > 0) {
                Element linkElement = (Element) list.item(0);
                link = linkElement.getTextContent();
            }
            list = item.getElementsByTagName("title");

            if (list.getLength() > 0) {
                Element titleElement = (Element) list.item(0);
                title = titleElement.getTextContent();
            } else {
                title = "";
            }

            list = item.getElementsByTagName("description");

            if (list.getLength() > 0) {
                Element descriptionElement = (Element) list.item(0);
                description = descriptionElement.getTextContent();
                Pattern p = Pattern.compile("src=\".+?\"");
                Matcher m = p.matcher(description);

                if (m.find()) {
                    String str = m.group();
                    extraURL = str.substring(5, str.length() - 1);
                    Log.d(TAG, extraURL);
                }

                Pattern pattern = Pattern.compile("<.+?>");
                Matcher matcher = pattern.matcher(description);
                String newStr = matcher.replaceAll("");
                description = newStr;
            }
            else {
                description = "";
            }
            list = item.getElementsByTagName("enclosure");

            if (list.getLength() > 0) {
                Element enclosureElement = (Element) list.item(0);
                String type = enclosureElement.getAttribute("type");

                if (type.equals("image/jpeg") || type.equals("image/png")) {
                    URL = enclosureElement.getAttribute("url");
                    items.add(new rss(title, currentDate, description, link, URL));
                }
                else {
                    items.add(new rss(title, currentDate, description, link, extraURL));
                }
            }
            else {
                items.add(new rss(title, currentDate, description, link, ""));
            }
        }
        return items;
    }
}
