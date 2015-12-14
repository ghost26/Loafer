package com.android.ifmo_android_2015.loafer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import model.EventKeeper;
import model.MapEvent;

public class EventsMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapView mapView;
    private BroadcastReceiver receiver;
    private EventKeeper eventKeeper;
    private ArrayList<MapEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_map);

        IntentFilter filter = new IntentFilter(DataBaseInitService.UPDATE_IS_READY);
        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                //call function to change map view
            }
        };
        registerReceiver(receiver, filter);

        eventKeeper = EventKeeper.getInstance(getApplicationContext());
        events = eventKeeper.getMapEvents();

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
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        new LatLng(0, 0), 14f));
    }
}
