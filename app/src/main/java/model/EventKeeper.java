package model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by daniil on 13.12.15.
 */
public class EventKeeper {
    private ArrayList<EasyEvent> easyEvents;
    private ArrayList<MapEvent> mapEvents;

    private static volatile EventKeeper instance;
    private final Context context;

    public EventKeeper(Context context) {
        this.context = context.getApplicationContext();
    }

    public static EventKeeper getInstance(Context context) {
        if (instance == null) {
            synchronized (EventKeeper.class) {
                if (instance == null) {
                    instance = new EventKeeper(context);
                }
            }
        }
        return instance;
    }

    public void setEasyEvents(ArrayList<EasyEvent> easyEvents) {
        this.easyEvents = easyEvents;
    }

    public void setMapEvents(ArrayList<MapEvent> mapEvents) {
        this.mapEvents = mapEvents;
    }

    public ArrayList<EasyEvent> getEasyEvents() {
        return easyEvents;
    }

    public ArrayList<MapEvent> getMapEvents() {
        return mapEvents;
    }
}
