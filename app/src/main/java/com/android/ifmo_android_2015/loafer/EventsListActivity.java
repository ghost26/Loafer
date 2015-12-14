package com.android.ifmo_android_2015.loafer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.EasyEvent;
import model.EventKeeper;

public class EventsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<EasyEvent> events;
    BroadcastReceiver receiver;
    EventKeeper eventKeeper;

    public static String RECYCLER_VIEW_NAME = "NAME";
    public static String RECYCLER_VIEW_ADDRESS = "ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        IntentFilter filter = new IntentFilter(DataBaseInitService.UPDATE_IS_READY);
        receiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                changeList();
            }
        };
        registerReceiver(receiver, filter);

        eventKeeper = EventKeeper.getInstance(getApplicationContext());
        events = eventKeeper.getEasyEvents();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(events);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        TextView name = (TextView) view.findViewById(R.id.recycler_item_name);
        TextView address = (TextView) view.findViewById(R.id.recycler_item_address);
        ImageView image = (ImageView) view.findViewById(R.id.recycler_item_image);

        Intent detailedInfo = new Intent(this, DetailedInfoActivity.class);
        detailedInfo.putExtra(RECYCLER_VIEW_NAME, name.getText().toString());
        detailedInfo.putExtra(RECYCLER_VIEW_ADDRESS, address.getText().toString());


        startActivity(detailedInfo);
    }

    private void changeList() {
        Log.d("LIST", "UPDATE");

        events = eventKeeper.getEasyEvents();
        mAdapter = new RecyclerViewAdapter(events);
        mRecyclerView.swapAdapter(mAdapter, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!= null){unregisterReceiver(receiver);}
    }
}