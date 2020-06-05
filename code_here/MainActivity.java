package com.example.rss;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String INTERNET_PERMISSION = Manifest.permission.INTERNET;
    private static final int REQUEST_INTERNET_PERMISSION_CODE = 2020;

    private rssAdapter rssListAdapter;
    private Database database;
    private ImageSave imageSave;
    private ImageLoad imageLoad;
    private GetRss getRss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isPermissionGranted(INTERNET_PERMISSION)) {
            requestPermission(INTERNET_PERMISSION, REQUEST_INTERNET_PERMISSION_CODE);
        }

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        initializeData();
    }

    public void initializeData(){
        database = new Database(this);
        database.open();

        EditText edit = findViewById(R.id.rss_enter);
        edit.setText(database.getLastUrl());

        ArrayList<rss> array = database.getItems();
        rssListAdapter = new rssAdapter(this, array);

        ListView rss_list = findViewById(R.id.rss_list);
        rss_list.setAdapter(rssListAdapter);

        rss_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callBrowser(((rss)rssListAdapter.getItem(position)).getLink());
            }
        });
    }

    @Override
    protected void onDestroy() {
        database.close();

        if (getRss != null) {
            getRss.cancel(true);
        }
        if (imageLoad != null) {
            imageLoad.cancel(true);
        }
        if (imageSave != null) {
            imageSave.cancel(true);
        }

        super.onDestroy();
    }

    public void callBrowser(String url)
    {
        Intent intent = new Intent(this, Browser.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    //for "search" button
    public void onClick(View view) {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean networkStatus = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (networkStatus) {
            getRss = new GetRss(this);
            EditText edit = findViewById(R.id.rss_enter);
            String str = edit.getText().toString();
            getRss.execute(str);
        }
    }

    //GetRss
    public void onLoadFinished(ArrayList<rss> result)
    {
        if (result != null)
        {
            rssListAdapter.setArray(result);
            ListView listView = findViewById(R.id.rss_list);
            imageLoad = new ImageLoad(listView, this);
            imageLoad.execute(rssListAdapter.getItems());
        }
        else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void saveToDb(ArrayList<rss> result, String str) {
        database.clear();
        database.setLastUrl(str);
        database.insertItems(result);
    }
    //GetRss


    //imageLoad
    public void imagesSaveToDb(ArrayList<rss> list)
    {
        imageSave = new ImageSave(database);
        imageSave.execute(list);
    }
    //imageLoad

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{ permission }, requestCode);
    }

    private boolean isPermissionGranted(String permission) {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_INTERNET_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Permisson granted", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Permisson denied", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
