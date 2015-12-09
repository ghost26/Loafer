package com.android.ifmo_android_2015.loafer;

import android.app.IntentService;
import android.content.Intent;
import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Location;
import timepad.Timepad;

/**
 * Created by ruslanabdulhalikov on 06.12.15.
 */
public class DataBaseInitService extends IntentService {

    private Status status = Status.CREATED;
    private int countOfEvents = 1;
    private String debug = "DBServise";
    private boolean skipEvent;

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
        List<Event> list = new ArrayList<>();

        int countedEvents = 0, add = 0;
        try {
            while (countedEvents != countOfEvents) {
                URL url = Timepad.createCityUrl(city, 100, countedEvents);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();

                JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
                getCountOfEvents(reader);
                add = Math.min(100, countOfEvents - countedEvents);
                list.addAll(getAllEvents(reader, add));
                countedEvents += add;
                if (inputStream != null) {
                    inputStream.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            status = Status.ERROR;
            e.printStackTrace();
            stopSelf();
        }
        Log.d(debug, "Count of events: " + String.valueOf(list.size()));
        status = Status.DONE;
        stopSelf();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(debug, "onDestroy");
    }

    private void getCountOfEvents(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String term = reader.nextName();
            if (term.equals("total")) {
                countOfEvents = reader.nextInt();
            }
            if (term.equals("values")) {
                reader.beginArray();
                return;
            }
        }
        reader.endObject();
    }

    private List<Event> getAllEvents(JsonReader reader, int count) throws IOException, URISyntaxException {
        List<Event> list = new ArrayList<>();
        Event event;
        while (count != 0) {
            skipEvent = false;
            event = getNextEvent(reader);
            if (!skipEvent) {
                list.add(event);
            }
            count--;
        }
        reader.endArray();
        return list;
    }

    private Event getNextEvent(JsonReader reader) throws IOException, URISyntaxException {
        Event event = new Event();
        reader.beginObject();
        while (reader.hasNext()) {
            String term = reader.nextName();
            if (term.equals("id")) {
                event.setId(reader.nextInt());
            } else if (term.equals("created_at")) {
                event.setCreatedAt(reader.nextString());
            } else if (term.equals("starts_at")) {
                event.setStartAt(reader.nextString());
            } else if (term.equals(("ends_at"))) {
                event.setEndsAt(reader.nextString());
            } else if (term.equals("name")) {
                event.setName(reader.nextString());
            } else if (term.equals("url")) {
                event.setEventUrl(new URI(reader.nextString()));
            }else if (term.equals("poster_image")) {
                reader.beginObject();
                while (reader.hasNext()) {
                    if (reader.nextName().equals("default_url")) {
                        event.setDefaultImageUrl(new URI(reader.nextString()));
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();

            } else if (term.equals("description_short")) {
                event.setDescriptionShort(reader.nextString());
            } else if (term.equals("location")) {
                event.setLocation(getLocation(reader));
            } else {
                reader.skipValue();
            }

        }
        reader.endObject();
        return event;
    }

    private Location getLocation(JsonReader reader) throws IOException {
        Location location = new Location();
        reader.beginObject();
        while (reader.hasNext()) {
            String term = reader.nextName();
            if (term.equals("country")) {
                location.setCountry(reader.nextString());
            } else if (term.equals("city")) {
                location.setCity(reader.nextString());
            } else if (term.equals("address")) {
                location.setAddress(reader.nextString());
            } else if (term.equals("coordinates")) {
                reader.beginArray();
                ArrayList<String> cor = new ArrayList<>();
                while (reader.hasNext()) {
                    cor.add(reader.nextString());
                }
                if (cor.size() == 2) {
                    try {
                        location.setCoordinates(Double.parseDouble(cor.get(0)), Double.parseDouble(cor.get(1)));
                    } catch (Exception e) {
                        skipEvent = true;
                    }
                } else {
                    skipEvent = true;
                }
                reader.endArray();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return location;
    }

    private enum Status {CREATED, IN_PROGRESS, DONE, ERROR}
}
