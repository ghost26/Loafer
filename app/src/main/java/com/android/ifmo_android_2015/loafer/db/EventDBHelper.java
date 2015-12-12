package com.android.ifmo_android_2015.loafer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.ifmo_android_2015.loafer.db.util.DatabaseCorruptionHandler;

import java.io.File;

/**
 * Created by ruslanabdulhalikov on 10.12.15.
 */
public class EventDBHelper extends SQLiteOpenHelper {

    private static final String DB_FILE_NAME = "events.db";

    private static final int DB_VERSION_1 = 1;



    private static volatile EventDBHelper instance;

    public static EventDBHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (EventDBHelper.class) {
                if (instance == null) {
                    instance = new EventDBHelper(context);
                }
            }
        }
        return instance;
    }

    private final Context context;

    public EventDBHelper(Context context) {
        super(context, DB_FILE_NAME, null, DB_VERSION_1,
                new DatabaseCorruptionHandler(context, DB_FILE_NAME));
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventContract.Cities.CREATE_TABLE);
        db.execSQL(EventContract.Events.CREATE_TABLE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //db.setForeignKeyConstraintsEnabled(true);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "onUpgrade: oldVersion=" + oldVersion + " newVersion=" + newVersion);
    }

    public void dropDb() {
        SQLiteDatabase db = getWritableDatabase();
        if (db.isOpen()) {
            try {
                db.close();
            } catch (Exception e) {
                Log.w(LOG_TAG, "Failed to close DB");
            }
        }
        final File dbFile = context.getDatabasePath(DB_FILE_NAME);
        try {
            Log.d(LOG_TAG, "deleting the database file: " + dbFile.getPath());
            if (!dbFile.delete()) {
                Log.w(LOG_TAG, "Failed to delete database file: " + dbFile);
            }
            Log.d(LOG_TAG, "Deleted DB file: " + dbFile);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Failed to delete database file: " + dbFile, e);
        }
    }

    private static final String LOG_TAG = "EvnetsDB";
}
