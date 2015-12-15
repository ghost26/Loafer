package com.android.ifmo_android_2015.loafer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import model.Event;
import model.EventKeeper;

public class DetailedInfoActivity extends AppCompatActivity {
    private TextView name;
    private TextView address;
    private TextView description;
    private TextView startsAt;
    private TextView endsAt;
    private ImageView image;

    public static String LAT = "LAT";
    public static String LNG = "LNG";
    private String url;

    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        description = (TextView) findViewById(R.id.description);
        startsAt = (TextView) findViewById(R.id.starts_at);
        endsAt = (TextView) findViewById(R.id.ends_at);
        image = (ImageView) findViewById(R.id.image);

        EventKeeper eventKeeper = EventKeeper.getInstance(getApplicationContext());
        Event event = eventKeeper.getEvent();

        url = event.getEventUrl();
        longitude = event.getLocation().getLongitude();
        latitude = event.getLocation().getLatitude();
        name.setText(event.getName());
        address.setText(event.getLocation().getAddress());
        description.setText(event.getDescriptionShort());
        startsAt.setText(event.getStartAt());
        endsAt.setText(event.getEndsAt());

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(event.getDefaultImageUrl(), image);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.seeMore) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
        if (view.getId() == R.id.viewInMap) {
            Intent intent = new Intent(this, EventsMapActivity.class);
            intent.putExtra(LAT, latitude);
            intent.putExtra(LNG, longitude);
            startActivity(intent);
        }
    }
}
