package model;

/**
 * Created by ruslanabdulhalikov on 12.12.15.
 */
public class EasyEvent {
    private long id ;
    private String name = "";
    private String defaultImageUrl = "";
    private String address = "";

    public long getId() {return id;}

    public void setId(long id) {
        this.id = id;
    }

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }

    public void setDefaultImageUrl(String defaultImageUrl) {
        this.defaultImageUrl = defaultImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
