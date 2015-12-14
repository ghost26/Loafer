package parser;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Location;
import timepad.Timepad;

/**
 * Created by daniil on 13.12.15.
 */
public class Parser {
    private int countOfEvents = 1;
    private boolean skipEvent;

    public Parser () {

    }

    public ArrayList<Event> parse(String  city) throws IOException, URISyntaxException {
        ArrayList<Event> list = new ArrayList<>();
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
            Log.d("LOL", "ERROR IN PARSER");
            throw e;
        }
        return list;
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
                event.setId(reader.nextLong());
            } else if (term.equals("created_at")) {
                event.setCreatedAt(reader.nextString());
            } else if (term.equals("starts_at")) {
                event.setStartAt(reader.nextString());
            } else if (term.equals(("ends_at"))) {
                event.setEndsAt(reader.nextString());
            } else if (term.equals("name")) {
                event.setName(reader.nextString());
            } else if (term.equals("url")) {
                event.setEventUrl(reader.nextString());
            }else if (term.equals("poster_image")) {
                reader.beginObject();
                while (reader.hasNext()) {
                    if (reader.nextName().equals("default_url")) {
                        event.setDefaultImageUrl(reader.nextString());
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
}
