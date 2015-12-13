package com.android.ifmo_android_2015.loafer;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity{
    public String currentCity;
    public String currentCityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, DataBaseInitService.class);
        switch (v.getId()) {
            case R.id.stavropol :
                currentCity = "Ставрополь";
                currentCityId = "0";
                break;
            case R.id.moscow :
                currentCity = "Москва";
                currentCityId = "1";
                break;
            case R.id.stPetersburg :
                currentCity = "Санкт-Петербург";
                currentCityId = "2";
                break;
            case R.id.saratov:
                currentCity = "Саратов";
                currentCityId = "3";
                break;
            case R.id.rostovNaDony:
                currentCity = "Ростов-на-Дону";
                currentCityId = "4";
                break;
            case R.id.krasnodar:
                currentCity = "Краснодар";
                currentCityId = "5";
                break;
        }
        intent.putExtra("FROM", "MAIN");
        intent.putExtra("CITY", currentCity);
        intent.putExtra("CITY_ID", currentCityId);
        PendingIntent pi = createPendingResult(0, new Intent(), 0);
        intent.putExtra("PINTENT", pi);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == 0) {
                Log.d("LOL", "CATCH");
                Intent intent = new Intent(this, TabsActivity.class);
                intent.putExtra("CITY", currentCity);
                intent.putExtra("CITY_ID", currentCityId);
                Log.d("LOL", "SEND TO TABS");
                startActivity(intent);
            }
        }
    }
}
