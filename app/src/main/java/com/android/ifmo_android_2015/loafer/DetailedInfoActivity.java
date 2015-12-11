package com.android.ifmo_android_2015.loafer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedInfoActivity extends AppCompatActivity {
    private TextView name;
    private TextView address;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        Intent intent = getIntent();

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);

        address.setText(intent.getStringExtra("ADDRESS"));
        name.setText(intent.getStringExtra("NAME"));
    }

    public void onClick(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }
}
