package com.android.ifmo_android_2015.loafer;

import android.app.PendingIntent;
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
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, DataBaseInitService.class);
        switch (v.getId()) {
            case R.id.stavropol :
                intent.putExtra("CITY", "Ставрополь");
                intent.putExtra("CITY_ID", "0");
                break;
            case R.id.moscow :
                intent.putExtra("CITY", "Москва");
                intent.putExtra("CITY_ID", "1");
                break;
            case R.id.stPetersburg :
                intent.putExtra("CITY", "Санкт-Петербург");
                intent.putExtra("CITY_ID", "2");
                break;
            case R.id.saratov:
                intent.putExtra("CITY", "Саратов");
                intent.putExtra("CITY_ID", "3");
                break;
            case R.id.rostovNaDony:
                intent.putExtra("CITY", "Ростов-на-Дону");
                intent.putExtra("CITY_ID", "4");
                break;
            case R.id.krasnodar:
                intent.putExtra("CITY", "Краснодар");
                intent.putExtra("CITY_ID", "5");
                break;
        }
        intent.putExtra("FROM", "MAIN");
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
                Log.d("LOL", "SEND TO TABS");
                startActivity(intent);
            }
        }
    }
}
