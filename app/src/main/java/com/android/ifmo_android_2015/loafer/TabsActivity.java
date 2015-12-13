package com.android.ifmo_android_2015.loafer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class TabsActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Log.d("LOL", "CATCH IN TABS");
        Log.d("LOL", "PUT TO BUNDLES");

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
    }
}
