package com.android.ifmo_android_2015.loafer.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.EasyEvent;
import model.Event;

/**
 * Created by ruslanabdulhalikov on 12.12.15.
 */
public class DataBaseAdapter {

    private SQLiteDatabase db;
    private int importedCount;

    private SQLiteStatement statement;



    public DataBaseAdapter(SQLiteDatabase db) {
        this.db = db;

    }


    public boolean insertEventList(List<Event> eventList) {
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
                if (!insertEvent(eventList.get(i))) {
                    return false;
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
        statement.bindString(2, event.getName());
        statement.bindString(3, event.getCreatedAt());
        statement.bindString(4, event.getStartAt());
        statement.bindString(5, event.getEndsAt());
        statement.bindString(6, event.getDescriptionShort());
        statement.bindString(7, event.getDefaultImageUrl());
        statement.bindString(8, event.getEventUrl());
        statement.bindString(9, event.getLocation().getAddress());
        statement.bindString(10, event.getLocation().getCountry());
        statement.bindDouble(11, event.getLocation().getLatitude());
        statement.bindDouble(12, event.getLocation().getLongitude());
        statement.bindLong(13, 1);
        long rowId = statement.executeInsert();


        if (rowId < 0) {
            Log.w(LOG_TAG, "Failed to insert city: id=" + event.getId() + " name=" + event.getName());
            return false;
        }
        return true;
    }

    public boolean insertCity(long id, @NonNull String name) {
        final ContentValues values = new ContentValues();
        values.put(EventContract.Cities.CITY_ID, id);
        values.put(EventContract.Cities.NAME, name);

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

    public List<EasyEvent> getAllEventsByCity(String cityId) {

        Cursor cursor = db.query(EventContract.Events.TABLE,
                new String[] { "_id", "name", "default_image_url", "address"},
                "city_id=?",
                new String[] {cityId}, null, null, null);
        List<EasyEvent> list = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            for(; !cursor.isAfterLast(); cursor.moveToNext()) {
                EasyEvent easyEvent = new EasyEvent();
                easyEvent.setId(cursor.getLong(0));
                easyEvent.setName(cursor.getString(1));
                easyEvent.setDefaultImageUrl(cursor.getString(2));
                easyEvent.setAddress(cursor.getString(3));
                Log.d(LOG_TAG, "" + easyEvent.getName());
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
        if (cursor != null && cursor.moveToFirst() && !cursor.isAfterLast()) {
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
            Log.d(LOG_TAG, "" + event.getName());
        }

        return event;
    }

    private static final String LOG_TAG = "EventReader";
}