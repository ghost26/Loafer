package timepad;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ruslanabdulhalikov on 05.12.15.
 */
public class Timepad {
    private static final String BASE_URL = "https://api.timepad.ru/v1";

    private static final String LIMIT = "limit";
    private static final String SKIP = "skip";
    private static final String CITIES = "cities";
    private static final String FIELDS = "fields";
    private static final String LOCATION = "location";
    private static final String CREATED_AT = "created_at";
    private static final String ENDS_AT = "ends_at";
    private static final String ACCESS_SATUSES = "access_statuses";
    private static final String PUBLIC = "public";

    private static final String EVENT_RESOURSE = "events";

    private static final String FORMAT_JSON = ".json";


    public static URL createCityUrl(String city, int limit, int skip)
            throws MalformedURLException {
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendEncodedPath(EVENT_RESOURSE.concat(FORMAT_JSON))
                .appendQueryParameter(LIMIT, Integer.toString(limit))
                .appendQueryParameter(SKIP, Integer.toString(skip))
                .appendQueryParameter(CITIES, city)
                .appendQueryParameter(FIELDS, CREATED_AT + "," + ENDS_AT + "," + LOCATION)
                .appendQueryParameter(ACCESS_SATUSES, PUBLIC)
                .build();
        return new URL(uri.toString());
    }
}
