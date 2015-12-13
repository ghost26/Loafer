package com.android.ifmo_android_2015.loafer;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.android.ifmo_android_2015.loafer.db.DataBaseAdapter;
import com.android.ifmo_android_2015.loafer.db.EventDBHelper;

import java.util.ArrayList;

import model.EasyEvent;
import model.Event;
import model.EventKeeper;
import model.MapEvent;
import parser.Parser;

/**
 * Created by ruslanabdulhalikov on 06.12.15.
 */
public class DataBaseInitService extends IntentService {

    private Status status = Status.CREATED;

    private String debug = "DBServise";
    private PendingIntent pi;

    public DataBaseInitService() {
        super("DataBaseInitService");
    }

    public void onCreate() {
        super.onCreate();
        Log.d(debug, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        status = Status.IN_PROGRESS;
        String city = intent.getStringExtra("CITY");
        String city_id = intent.getStringExtra("CITY_ID");
        String from = intent.getStringExtra("FROM");
        pi = intent.getParcelableExtra("PINTENT");

        if (from.equals("MAIN") || from.equals("SETTINGS")) {
            EventDBHelper hp = EventDBHelper.getInstance(getApplicationContext());
            try {
                DataBaseAdapter wdb = new DataBaseAdapter(hp.getReadableDatabase());
                if (!wdb.isCityEventsExist(city_id) || from.equals("SETTINGS")) {
                    Parser parser = new Parser();
                    ArrayList<Event> list = parser.parse(city);
                    wdb.updateCityEvents(city_id, city, list);
                }

                Log.d("LOL", "REQUEST TO BASE");
                ArrayList<EasyEvent> easyEvents = wdb.getAllEventsByCity(city_id);
                ArrayList<MapEvent> mapEvents = wdb.getAllMapEventsByCity(city_id);
                Log.d("LOL", "ANSWER FROM BASE");

                EventKeeper a = EventKeeper.getInstance(getApplicationContext());
                a.setEasyEvents(easyEvents);
                a.setMapEvents(mapEvents);
                if (from.equals("SETTINGS")) {
                    Intent in = new Intent("UPDATEISREADY");
                    sendBroadcast(in);
                }
                pi.send(0);
                status = Status.DONE;
            } catch (Exception e) {
                e.printStackTrace();
                status = Status.ERROR;
            }
        }
        stopSelf();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(debug, "onDestroy");
    }

    private enum Status {CREATED, IN_PROGRESS, DONE, ERROR}
}
