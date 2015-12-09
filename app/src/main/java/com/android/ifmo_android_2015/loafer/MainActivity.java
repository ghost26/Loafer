package com.android.ifmo_android_2015.loafer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View v) {
        Intent tabsView = new Intent(this, TabsActivity.class);
        switch (v.getId()) {
            case R.id.stavropol :
                tabsView.putExtra("CITY", "Ставрополь");
                break;
            case R.id.moscow :
                tabsView.putExtra("CITY", "Москва");
                break;
            case R.id.stPetersburg :
                tabsView.putExtra("CITY", "Санкт-Петербург");
                break;
            case R.id.rostov:
                tabsView.putExtra("CITY", "Ростов");
                break;
            case R.id.rostovNaDony:
                tabsView.putExtra("CITY", "Ростов-на-Дону");
                break;
            case R.id.krasnodar:
                tabsView.putExtra("CITY", "Краснодар");
                break;
        }

        startActivity(tabsView);
    }
}
