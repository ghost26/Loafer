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
        TabHost.TabSpec tabList= tabHost.newTabSpec(getString(R.string.list_name))
                .setIndicator(getString(R.string.res_list)).setContent(listIntent);
        TabHost.TabSpec tabSettings = tabHost.newTabSpec(getString(R.string.settings))
                .setIndicator(getString(R.string.settings_name)).setContent(settingsIntent);

        tabHost.addTab(tabList);
        tabHost.addTab(tabSettings);
    }
}
