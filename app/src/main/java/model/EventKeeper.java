package model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by daniil on 13.12.15.
 */
public class EventKeeper {
    private ArrayList<EasyEvent> easyEvents;
    private Event event;

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

    public ArrayList<EasyEvent> getEasyEvents() {
        return easyEvents;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return this.event;
    }
}
