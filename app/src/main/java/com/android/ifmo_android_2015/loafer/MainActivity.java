package com.android.ifmo_android_2015.loafer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("HYINA", getApplicationContext().getDatabasePath("events.db").getAbsolutePath().toString());

    }

    public void onClick(View v) {
        Intent tabsView = new Intent(this, TabsActivity.class);
        switch (v.getId()) {
            case R.id.stavropol :
                tabsView.putExtra("CITY", "Ставрополь");
                tabsView.putExtra("CITY_ID", "0");
                break;
            case R.id.moscow :
                tabsView.putExtra("CITY", "Москва");
                tabsView.putExtra("CITY_ID", "1");
                break;
            case R.id.stPetersburg :
                tabsView.putExtra("CITY", "Санкт-Петербург");
                tabsView.putExtra("CITY_ID", "2");
                break;
            case R.id.rostov:
                tabsView.putExtra("CITY", "Ростов");
                tabsView.putExtra("CITY_ID", "3");
                break;
            case R.id.rostovNaDony:
                tabsView.putExtra("CITY", "Ростов-на-Дону");
                tabsView.putExtra("CITY_ID", "4");
                break;
            case R.id.krasnodar:
                tabsView.putExtra("CITY", "Краснодар");
                tabsView.putExtra("CITY_ID", "5");
                break;
        }
        startActivity(tabsView);
    }
}
