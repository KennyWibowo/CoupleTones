package com.helloworld.kenny.coupletones.favorites;

/**
 * Created by Kenny on 5/27/2016.
 */
public class JSONEntry {
    private String name;
    private long timestamp;
    private double latitude;
    private double longitude;
    private boolean departed;

    public JSONEntry() {}

    public JSONEntry(Entry entry) {
        this.name = entry.getName();
        this.timestamp = entry.getTimestamp().getTime();
        this.latitude = entry.getLocation().latitude;
        this.longitude = entry.getLocation().longitude;
        this.departed = false;
    }


    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean getDeparted() {
        return departed;
    }

    public void setDeparted(boolean departed) {
        this.departed = departed;
    }
}
