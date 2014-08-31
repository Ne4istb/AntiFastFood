package com.hackaton.ne4istb.antifastfood;

/**
 * Created by ne4istb on 31.08.2014.
 */
public class SuggestionRecord {


    String id;
    String name;
    String address;
    String site;
    Integer distance;

    Coordinate coordinate;

    public String thumbnail;

    public SuggestionRecord(String id, String name, String address, String site, Coordinate coordinate, Integer distance, String thumbnail) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.site = site;
        this.coordinate = coordinate;
        this.distance = distance;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
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


    public String getThumbnail() {
        return thumbnail;
    }
}
