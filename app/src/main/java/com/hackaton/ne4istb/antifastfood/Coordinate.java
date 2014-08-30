package com.hackaton.ne4istb.antifastfood;

public class Coordinate {

    private double latitude;
    private double longtitude;

    public Coordinate(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }
}
