package com.android.ifmo_android_2015.loafer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventsMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapView mapView;
    private double latitude;
    private double longitude;
    private float zoom;

    private MarkerOptions marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_map);

//        marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Hello Maps ");

        if (savedInstanceState == null) {

            Intent intent = getIntent();
            latitude = intent.getDoubleExtra(DetailedInfoActivity.LAT, 0);
            longitude = intent.getDoubleExtra(DetailedInfoActivity.LNG, 0);
            zoom = 14f;
        } else {
            latitude = savedInstanceState.getDouble("lat");
            longitude = savedInstanceState.getDouble("lng");
            zoom = savedInstanceState.getFloat("zoom");
        }
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMap().getUiSettings().setZoomControlsEnabled(true);
        mapView.getMap().setMyLocationEnabled(true);
        mapView.getMapAsync(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        outState.putDouble("lat", latitude);
        outState.putDouble("lng", longitude);
        outState.putFloat("zoom", zoom);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latitude, longitude), zoom));
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Hello world"));

    }
}
