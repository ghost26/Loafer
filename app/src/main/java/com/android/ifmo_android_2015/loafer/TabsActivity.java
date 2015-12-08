package com.android.ifmo_android_2015.loafer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import java.net.MalformedURLException;

import timepad.Timepad;

public class TabsActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        TabHost tabHost = getTabHost();
        Intent mapIntent = new Intent(this, EventsMapActivity.class);
        Intent listIntent = new Intent(this, EventsListActivity.class);
        Intent settingsIntent = new Intent(this, SettingsActivity.class);

        TabHost.TabSpec tabMap = tabHost.newTabSpec("Map").setIndicator("Карта").setContent(mapIntent);
        TabHost.TabSpec tabList= tabHost.newTabSpec("List").setIndicator("Список").setContent(listIntent);
        TabHost.TabSpec tabSettings = tabHost.newTabSpec("Settings").setIndicator("Настройки").setContent(settingsIntent);

        tabHost.addTab(tabMap);
        tabHost.addTab(tabList);
        tabHost.addTab(tabSettings);
        try {
            Intent db = new Intent(this, DataBaseInitService.class);
            db.putExtra("URL", Timepad.createCityUrl("Москва", 10, 0).toString());
            startService(db);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
