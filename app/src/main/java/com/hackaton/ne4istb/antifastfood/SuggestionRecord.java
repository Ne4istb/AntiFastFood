package com.hackaton.ne4istb.antifastfood;

/**
 * Created by ne4istb on 31.08.2014.
 */
public class SuggestionRecord {
    String name;
    String address;
    String site;
    Coordinate coordinate;

    public SuggestionRecord(String name, String address, String site, Coordinate coordinate) {
        this.name = name;
        this.address = address;
        this.site = site;
        this.coordinate = coordinate;
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

    public String getName() {
        return name;
    }
}
