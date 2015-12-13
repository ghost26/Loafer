package com.android.ifmo_android_2015.loafer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.EasyEvent;

public class EventsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<EasyEvent> events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //events = (List<EasyEvent>) bundle.getSerializable("List_of_events");
        events = new ArrayList<EasyEvent>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);//true if size of layout can't change

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
        detailedInfo.putExtra("NAME", name.getText().toString());
        detailedInfo.putExtra("ADDRESS", address.getText().toString());

        //mAdapter = new RecyclerViewAdapter(events);
        //mRecyclerView.swapAdapter(mAdapter, false);
        startActivity(detailedInfo);
    }
}