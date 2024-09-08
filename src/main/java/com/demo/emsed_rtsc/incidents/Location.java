package com.demo.emsed_rtsc.incidents;

public class Location {
    private double lat;
    private double lon;

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLatitude() {
        return this.lat;
    }

    public double getLongitude() {
        return this.lon;
    }

    public void setLatitude(double latitude) {
        this.lat = latitude;
    }

    public void setLongitude(double longitude) {
        this.lon = longitude;
    }
}
