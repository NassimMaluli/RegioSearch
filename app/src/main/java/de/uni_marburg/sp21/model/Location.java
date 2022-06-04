package de.uni_marburg.sp21.model;

import java.io.Serializable;

/**
 * Model class for a location (of a company)
 *
 * @author chris
 * @see Company
 */
public class Location implements Serializable {
    private Double lat, lon;



    /**
     * no arg constructor
     */
    public Location() {
    }

    /**
     * constructor for a location
     * @param lat the latitude
     * @param lon the longitude
     */
    public Location(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * getter for the latitude
     * @return the latitude of the location
     */
    public Double getLat() {
        return lat;
    }

    /**
     * getter for the longitude
     * @return the longitude of the location
     */
    public Double getLon() {
        return lon;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     * generates a string to represent the location
     * @return the string holding the data of the location.
     */
    @Override
    public String toString() {
        return "Location: " +
                "lat= " + lat +
                ", lon= " + lon ;
    }
}
