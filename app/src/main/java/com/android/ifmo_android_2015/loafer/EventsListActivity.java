package com.android.ifmo_android_2015.loafer;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    private AlertDialog dialogForUpdate;

    private static boolean UPDATE_IN_PROGRESS;

    public static int LAST_ANSWER_FROM_SERVICE;

    public static String RECYCLER_VIEW_NAME = "NAME";
    public static String RECYCLER_VIEW_ADDRESS = "ADDRESS";

    public static int LIST_REQUEST = 0;
    public static String EVENT_ID = "EVENT_ID";

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

        if (UPDATE_IN_PROGRESS) {
            createDialogForUpdate();
            updateDialogForUpdate(LAST_ANSWER_FROM_SERVICE);;
        }

        eventKeeper = EventKeeper.getInstance(getApplicationContext());
        events = eventKeeper.getEasyEvents();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(events);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void onClick(View view) {
        String eventId = ((TextView)view.findViewById(R.id.event_id)).getText().toString();
        createDialogForUpdate();
        Intent getDetInfo = new Intent(this, DataBaseInitService.class);
        PendingIntent pi = createPendingResult(LIST_REQUEST, new Intent(), 0);
        getDetInfo.putExtra(DataBaseInitService.EVENT_ID, eventId);
        getDetInfo.putExtra(DataBaseInitService.FROM, DataBaseInitService.FROM_LIST);
        getDetInfo.putExtra(DataBaseInitService.PENDING_INTENT, pi);
        startService(getDetInfo);
    }

    private void changeList() {
        events = eventKeeper.getEasyEvents();
        mAdapter = new RecyclerViewAdapter(events);
        mRecyclerView.swapAdapter(mAdapter, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIST_REQUEST) {
            if (resultCode == DataBaseInitService.DONE) {
                hideDialogForUpdate();
                Toast.makeText(getApplicationContext(), "Готово", Toast.LENGTH_SHORT).show();

                Intent intentDI = new Intent(this, DetailedInfoActivity.class);
                startActivity(intentDI);
            } else if (resultCode == DataBaseInitService.ERROR) {
                hideDialogForUpdate();
                Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
            } else {
                LAST_ANSWER_FROM_SERVICE = resultCode;
                updateDialogForUpdate(resultCode);
            }
        }
    }

    private void createDialogForUpdate() {
        UPDATE_IN_PROGRESS = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_for_update, null));

        builder.setCancelable(false);

        dialogForUpdate = builder.create();
        dialogForUpdate.show();
    }

    private void updateDialogForUpdate(int resultCode) {
        TextView status = (TextView) dialogForUpdate.findViewById(R.id.status);
        if (resultCode == DataBaseInitService.GET_FROM_DB) {
            status.setText("Получаем данные из базы");
        }
    }

    private void hideDialogForUpdate() {
        UPDATE_IN_PROGRESS = false;
        dialogForUpdate.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!= null){unregisterReceiver(receiver);}
    }
}