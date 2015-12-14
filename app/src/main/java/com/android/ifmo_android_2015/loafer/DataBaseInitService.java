package com.android.ifmo_android_2015.loafer;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.android.ifmo_android_2015.loafer.db.DataBaseAdapter;
import com.android.ifmo_android_2015.loafer.db.EventDBHelper;

import java.util.ArrayList;
import java.util.Calendar;

import model.EasyEvent;
import model.Event;
import model.EventKeeper;
import model.MapEvent;
import parser.Parser;

/**
 * Created by ruslanabdulhalikov on 06.12.15.
 */
public class DataBaseInitService extends IntentService {
    private String debug = "DBServise";
    private PendingIntent pi;


    public static String FROM = "FROM";
    public static String FROM_MAIN = "FROM_MAIN";
    public static String FROM_SETTINGS = "FROM_SETTINGS";
    public static String CITY_ID = "CITY_ID";
    public static String CITY = "CITY";
    public static String PENDING_INTENT = "PENDING_INTENT";
    public static String UPDATE_IS_READY = "UPDATE_IS_READY";
    public static String CURRENT_DATA = "UPDATE_IS_READY";

    public static int DONE = 0;
    public static int ERROR = 1;
    public static int TRY_TO_TAKE_TIMEPAD_DATA = 2;
    public static int UPDATE_DB = 3;
    public static int GET_FROM_DB = 4;

    public DataBaseInitService() {
        super("DataBaseInitService");
    }

    public void onCreate() {
        super.onCreate();
        Log.d(debug, "onCreate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String city = intent.getStringExtra(DataBaseInitService.CITY);
        String city_id = intent.getStringExtra(DataBaseInitService.CITY_ID);
        String from = intent.getStringExtra(DataBaseInitService.FROM);
        pi = intent.getParcelableExtra(DataBaseInitService.PENDING_INTENT);

        if (from.equals(DataBaseInitService.FROM_MAIN) || from.equals(DataBaseInitService.FROM_SETTINGS)) {
            EventDBHelper hp = EventDBHelper.getInstance(getApplicationContext());
            try {
                DataBaseAdapter wdb = new DataBaseAdapter(hp.getReadableDatabase());
                if (!wdb.isCityEventsExist(city_id) || from.equals(DataBaseInitService.FROM_SETTINGS)) {
                    Parser parser = new Parser();
                    pi.send(TRY_TO_TAKE_TIMEPAD_DATA);
                    ArrayList<Event> list = parser.parse(city);
                    pi.send(UPDATE_DB);
                    wdb.updateCityEvents(city_id, city, list);
                }

                pi.send(GET_FROM_DB);
                ArrayList<EasyEvent> easyEvents = wdb.getAllEventsByCity(city_id);
                ArrayList<MapEvent> mapEvents = wdb.getAllMapEventsByCity(city_id);

                EventKeeper a = EventKeeper.getInstance(getApplicationContext());
                a.setEasyEvents(easyEvents);
                a.setMapEvents(mapEvents);

                if (from.equals(DataBaseInitService.FROM_SETTINGS)) {
                    Intent in = new Intent(UPDATE_IS_READY);
                    sendBroadcast(in);
                }

                Intent data = new Intent();
                if (from.equals(FROM_SETTINGS)) {
                    Calendar calendar = Calendar.getInstance();
                    String time = calendar.getTime().toString();
                    data.putExtra(CURRENT_DATA, time);
                }

                pi.send(this, DONE, data);
            } catch (Exception e) {
                try {
                    pi.send(ERROR);
                } catch (PendingIntent.CanceledException e1) {
                    e1.printStackTrace();
                }
            }
        }
        stopSelf();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(debug, "onDestroy");
    }
}
