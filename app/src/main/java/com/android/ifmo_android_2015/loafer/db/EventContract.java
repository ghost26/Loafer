package com.android.ifmo_android_2015.loafer.db;

import java.net.URI;

/**
 * Created by ruslanabdulhalikov on 10.12.15.
 */
public final class EventContract {

    public interface EventColumns {
        String EVENT_ID = "_id";

        String NAME = "name";

        String COUNTRY = "country";

        String LATITUDE = "latitude";

        String LONGITUDE = "longitude";

        String CREATEDAT = "created_at";

        String STARTAT = "start_at";

        String ENDSAT = "ends_at";

        String DESCRIPTIONSHORT = "description_short";

        String DEFAULTIMAGEURL = "default_image_url";

        String EVENTURL = "event_url";

        String ADDRESS = "address";

        String CITY_ID = "city_id";
    }

    public interface  CityColumns {
        String CITY_ID = "_id";

        String NAME = "name";

    }

    public static final class Events implements EventColumns {

        public static final String TABLE = "events";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE
                + " ("
                + EVENT_ID + " INTEGER PRIMARY KEY, "
                + NAME + " TEXT, "
                + CREATEDAT + " TEXT, "
                + STARTAT + " TEXT, "
                + ENDSAT + " TEXT, "
                + DESCRIPTIONSHORT + " TEXT, "
                + DEFAULTIMAGEURL + " TEXT, "
                + EVENTURL + " TEXT, "
                + ADDRESS + " TEXT, "
                + COUNTRY + " TEXT, "
                + LATITUDE + " REAL, "
                + LONGITUDE + " REAL, "
                + "FOREIGN KEY(" + CITY_ID + ") REFERENCES " +
                "cities" + "(" + CITY_ID + ") ON DELETE CASCADE);"
                        + " )";

    }

    public static final class Cities implements CityColumns {

        public static final String TABLE = "cities";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE
                + " ("
                + CITY_ID + " INTEGER PRIMARY KEY, "
                + NAME + " TEXT, "
                + " )";

    }

    private EventContract() {}
}
