package model;

import java.io.Serializable;

/**
 * Created by daniil on 13.12.15.
 */
public class MapEvent implements Serializable {
    private long id;
    private String name = "";
    private double latitude ;
    private double longitude;

    public long getId() {return id;}

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getlongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
