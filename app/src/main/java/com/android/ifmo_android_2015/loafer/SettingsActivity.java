package com.android.ifmo_android_2015.loafer;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {
    private String city;
    private String city_id;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        city = intent.getStringExtra("CITY");
        city_id = intent.getStringExtra("CITY_ID");
    }

    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("LOL");
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();

        Intent intent = new Intent(this, DataBaseInitService.class);
        intent.putExtra("FROM", "SETTINGS");
        intent.putExtra("CITY", city);
        intent.putExtra("CITY_ID", city_id);
        PendingIntent pi = createPendingResult(0, new Intent(), 0);
        intent.putExtra("PINTENT", pi);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            alertDialog.cancel();
        }
    }
}
