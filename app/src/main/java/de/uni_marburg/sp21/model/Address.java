package de.uni_marburg.sp21.model;

import java.io.Serializable;

/**
 * Model class for an adress (needed by company)
 *
 * @author chris
 * @see Company
 */
public class Address implements Serializable {
    private String street, city, zip;

    /**
     * no arg constructor
     */
    public Address() {
    }

    /**
     * constructor for an Address
     * @param street the street as String
     * @param city the city as String
     * @param zip the zip as String
     */
    public Address(String street, String city, String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    /**
     * getter for the street
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * getter for the city
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * getter for the zip
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * generates a string to represent an address
     * @return the whole address as a string.
     */
    @Override
    public String toString() {
        return "Address: " +
                 street + ", "
                 + city + ", " +
                zip
                ;
    }


}
