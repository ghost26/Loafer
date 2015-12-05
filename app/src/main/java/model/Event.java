package model;

import java.net.URI;

/**
 * Created by ruslanabdulhalikov on 05.12.15.
 */
public class Event {
    public final int id;
    public final String created_at;
    public final String starts_at;
    public final String ends_at;
    public final String name;
    public final String description_short;
    public final URI default_url;
    public final URI url;
    public final String country;
    public final String city;
    public final String address;
    public final double latitude;
    public final double longitude;

    public Event(int id, String created_at, String starts_at, String ends_at, String name,
                 String description_short, URI default_url, URI url,
                 String country, String city, String address,
                 double latitude, double longitude) {
        this.id = id;
        this.created_at = created_at;
        this.starts_at = starts_at;
        this.ends_at = ends_at;
        this.name = name;
        this.description_short = description_short;
        this.default_url = default_url;
        this.url = url;
        this.country = country;
        this.city = city;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
