package model;

/**
 * Created by ruslanabdulhalikov on 05.12.15.
 */
public class Event {
    Location location;
    private long id ;
    private String createdAt = "";
    private String startAt = "";
    private String endsAt = "";
    private String name = "";
    private String descriptionShort = "";
    private String defaultImageUrl = "";
    private String eventUrl = "";

    public Event() {}

    public Event(int id, String createdAt, String startAt, String endsAt, String name,
                 String descriptionShort, String default_url, String eventUrl,
                 Location location) {
        this.id = id;
        this.createdAt = createdAt;
        this.startAt = startAt;
        this.endsAt = endsAt;
        this.name = name;
        this.descriptionShort = descriptionShort;
        this.defaultImageUrl = default_url;
        this.eventUrl = eventUrl;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    public void setDefaultImageUrl(String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public long getId() {return id;}

    public void setId(long id) {
        this.id = id;
    }

}
