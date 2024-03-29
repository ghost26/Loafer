package com.android.ifmo_android_2015.loafer.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import model.EasyEvent;
import model.Event;

/**
 * Created by ruslanabdulhalikov on 12.12.15.
 */
public class DataBaseAdapter {

    private static final String LOG_TAG = "EventReader";
    private final String PROTOCOL = "https://";
    private SQLiteDatabase db;
    private int importedCount;
    private SQLiteStatement statement;
    private long cityId;


    public DataBaseAdapter(SQLiteDatabase db) {
        this.db = db;

    }

    public boolean insertEventList(ArrayList<Event> eventList, long cityId) {
        this.cityId = cityId;
        statement = db.compileStatement(String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                EventContract.Events.TABLE,
                EventContract.Events.EVENT_ID,
                EventContract.Events.NAME,
                EventContract.Events.CREATEDAT,
                EventContract.Events.STARTAT,
                EventContract.Events.ENDSAT,
                EventContract.Events.DESCRIPTIONSHORT,
                EventContract.Events.DEFAULTIMAGEURL,
                EventContract.Events.EVENTURL,
                EventContract.Events.ADDRESS,
                EventContract.Events.COUNTRY,
                EventContract.Events.LATITUDE,
                EventContract.Events.LONGITUDE,
                EventContract.Events.CITY_ID));
        db.beginTransaction();
        try {
            for(int i = 0; i < eventList.size(); ++i) {
                if (!isEventExists(eventList.get(i).getId())) {
                    if (!insertEvent(eventList.get(i))) {
                        return false;
                    }
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to parse cities: " + e, e);
        } finally {
            db.endTransaction();
            try {
                statement.close();
            } catch (Exception ignored) {
            }
        }
        return true;
    }

    private boolean insertEvent(Event event) {
        statement.bindLong(1, event.getId());
        statement.bindString(2, Html.fromHtml(event.getName()).toString());
        statement.bindString(3, event.getCreatedAt());
        statement.bindString(4, event.getStartAt());
        statement.bindString(5, event.getEndsAt());
        statement.bindString(6, Html.fromHtml(event.getDescriptionShort()).toString());
        statement.bindString(7, getNormalizedUrl(event.getDefaultImageUrl()));
        statement.bindString(8, event.getEventUrl());
        statement.bindString(9, Html.fromHtml(event.getLocation().getAddress()).toString());
        statement.bindString(10, event.getLocation().getCountry());
        statement.bindDouble(11, event.getLocation().getLatitude());
        statement.bindDouble(12, event.getLocation().getLongitude());
        statement.bindLong(13, this.cityId);
        long rowId = statement.executeInsert();


        if (rowId < 0) {
            Log.w(LOG_TAG, "Failed to insert city: id=" + event.getId() + " name=" + event.getName());
            return false;
        }
        return true;
    }

    public boolean insertCity(long id, @NonNull String name) {
        if (isCityEventsExist(Long.toString(id))) {
            Log.w(LOG_TAG, "The city have already exist city_id: " + id + " " + name);
            return false;
        }
        final ContentValues values = new ContentValues();
        values.put(EventContract.Cities.CITY_ID, id);
        values.put(EventContract.Cities.NAME, name);
        Calendar calendar = Calendar.getInstance();
        values.put(EventContract.Cities.LASTUPDATE, calendar.getTime().toString());

        long rowId = db.insert(EventContract.Cities.TABLE, null /*nullColumnHack not needed*/, values);
        if (rowId < 0) {
            Log.w(LOG_TAG, "Failed to insert city: id=" + id + " name=" + name);
            return false;
        }
        return true;
    }

    public boolean deleteEventsByCity(String cityId) {
        return db.delete(EventContract.Cities.TABLE, EventContract.Cities.CITY_ID + "=" + cityId, null) > 0;
    }

    private boolean isEventExists(long eventId) {
        String evid = Long.toString(eventId);
        Cursor cursor = db.query(EventContract.Events.TABLE, new String[]{"_id", "name"}, "_id=?", new String[]{evid}, null, null, null);
        return cursor != null && cursor.moveToFirst();
    }


    public boolean isCityEventsExist(String cityId) {
        Cursor cursor = db.query(EventContract.Cities.TABLE, new String[]{"_id", "name", "last_upd"}, "_id=?", new String[]{cityId}, null, null, null);
        return cursor != null && cursor.moveToFirst();
    }

    public String getLastUpdate(String cityId) {
        Cursor cursor = db.query(EventContract.Cities.TABLE, new String[]{"_id", "name", "last_upd"}, "_id=?", new String[]{cityId}, null, null, null);
        if (cursor != null && cursor.moveToFirst() && !cursor.isAfterLast()) {
            return cursor.getString(2);
        }
        return "";
    }

    public ArrayList<EasyEvent> getAllEventsByCity(String cityId) {

        Cursor cursor = db.query(EventContract.Events.TABLE,
                new String[] { "_id", "name", "default_image_url", "address"},
                "city_id=?",
                new String[] {cityId}, null, null, null);
        ArrayList<EasyEvent> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            for(; !cursor.isAfterLast(); cursor.moveToNext()) {
                EasyEvent easyEvent = new EasyEvent();
                easyEvent.setId(cursor.getLong(0));
                easyEvent.setName(cursor.getString(1));
                easyEvent.setDefaultImageUrl(cursor.getString(2));
                easyEvent.setAddress(cursor.getString(3));
                //Log.d(LOG_TAG, "" + easyEvent.getName());
                list.add(easyEvent);
            }

        }

        return list;
    }

    public Event getEventById(String eventId) {

        Cursor cursor = db.query(EventContract.Events.TABLE,
                new String[] { "_id", "name", "country", "latitude",
                        "longitude", "created_at", "start_at", "ends_at",
                        "description_short", "default_image_url", "event_url", "address", "city_id"},
                "_id=?",
                new String[] {eventId}, null, null, null);

        Event event = new Event();
        Log.d("DBSER", "CREATE NEW EVENT");
        if (cursor != null && cursor.moveToFirst() && !cursor.isAfterLast()) {
            Log.d("DBSER", "IN IF");
            event.setId(cursor.getLong(0));
            event.setName(cursor.getString(1));
            event.getLocation().setCountry(cursor.getString(2));
            event.getLocation().setCoordinates(cursor.getDouble(3), cursor.getDouble(4));
            event.setCreatedAt(cursor.getString(5));
            event.setStartAt(cursor.getString(6));
            event.setEndsAt(cursor.getString(7));
            event.setDescriptionShort(cursor.getString(8));
            event.setDefaultImageUrl(cursor.getString(9));
            event.setEventUrl(cursor.getString(10));
            event.getLocation().setAddress(cursor.getString(11));
            Log.d("DBSER", "FIND EVENT");
        }
        Log.d("DBSER", "SEND EVENT");
        return event;
    }

    public boolean updateCityEvents(String cityId, String cityName, ArrayList<Event> eventList) {
        deleteEventsByCity(cityId);
        return insertCity(Long.parseLong(cityId), cityName) &&
                insertEventList(eventList, Long.parseLong(cityId));
    }

    private String getNormalizedUrl(String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        int pos = -1;
        for (int i = 0; i < stringBuilder.length(); i++) {
            if (stringBuilder.charAt(i) == '/') {
                pos = i;
            } else if (pos != -1) {
                break;
            }
        }
        stringBuilder.delete(0, pos + 1);
        stringBuilder.insert(0, PROTOCOL);
        return stringBuilder.toString();
    }
}
