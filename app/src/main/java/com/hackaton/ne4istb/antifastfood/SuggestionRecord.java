package com.hackaton.ne4istb.antifastfood;

/**
 * Created by ne4istb on 31.08.2014.
 */
public class SuggestionRecord {
    String name;
    String address;
    String site;

    Integer distance;
    Coordinate coordinate;

    public SuggestionRecord(String name, String address, String site, Coordinate coordinate, Integer distance) {
        this.name = name;
        this.address = address;
        this.site = site;
        this.coordinate = coordinate;
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public String getSite() {
        return site;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getName() {
        return name;
    }
}
