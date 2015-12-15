package com.android.ifmo_android_2015.loafer;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabsActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Intent intent = getIntent();

        TabHost tabHost = getTabHost();
        Intent listIntent = new Intent(this, EventsListActivity.class);
        Intent settingsIntent = new Intent(this, SettingsActivity.class);

        settingsIntent.putExtras(intent.getExtras());

        TabHost.TabSpec tabList= tabHost.newTabSpec("List").setIndicator("Список").setContent(listIntent);
        TabHost.TabSpec tabSettings = tabHost.newTabSpec("Settings").setIndicator("Настройки").setContent(settingsIntent);

        tabHost.addTab(tabList);
        tabHost.addTab(tabSettings);
    }
}
