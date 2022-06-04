package de.uni_marburg.sp21.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model for opening hours (needed for Company)
 *
 * @author chris
 * @see Company
 */
public class OpeningHours implements Serializable {

    ArrayList<HashMap<String, String>> monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    /**
     * no arg constructor
     */
    public OpeningHours() {
    }

    /**
     * constructor for opening hours.
     * @param monday opening hours on monday
     * @param tuesday opening hours on tuesday
     * @param wednesday opening hours on wednesday
     * @param thursday opening hours on thursday
     * @param friday opening hours on friday
     * @param saturday opening hours on saturday
     * @param sunday opening hours on sunday
     */
    public OpeningHours(ArrayList<HashMap<String, String>> monday, ArrayList<HashMap<String, String>> tuesday, ArrayList<HashMap<String, String>> wednesday, ArrayList<HashMap<String, String>> thursday, ArrayList<HashMap<String, String>> friday, ArrayList<HashMap<String, String>> saturday, ArrayList<HashMap<String, String>> sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    /**
     * getter for monday
     * @return the opening hours on monday
     */
    public ArrayList<HashMap<String, String>> getMonday() {
        return monday;
    }

    /**
     * getter for tuesday
     * @return the opening hours on tuesday
     */
    public ArrayList<HashMap<String, String>> getTuesday() {
        return tuesday;
    }

    /**
     * getter for wednesday
     * @return the opening hours on wednesday
     */
    public ArrayList<HashMap<String, String>> getWednesday() {
        return wednesday;
    }

    /**
     * getter for thursday
     * @return the opening hours on thursday
     */
    public ArrayList<HashMap<String, String>> getThursday() {
        return thursday;
    }

    /**
     * getter for friday
     * @return the opening hours on friday
     */
    public ArrayList<HashMap<String, String>> getFriday() {
        return friday;
    }

    /**
     * getter for saturday
     * @return the opening hours on saturday
     */
    public ArrayList<HashMap<String, String>> getSaturday() {
        return saturday;
    }

    /**
     * getter for sunday
     * @return the opening hours on sunday
     */
    public ArrayList<HashMap<String, String>> getSunday() {
        return sunday;
    }

    /**
     * generates a string to represent the opening hours
     * @return the opening hours as human readable string.
     */
    @Override
    public String toString() {
        return "OpeningHours: " + '\n' +
                "Monday, " + monday + '\n' +
                "Tuesday, " + tuesday + '\n' +
                "Wednesday, " + wednesday + '\n' +
                "Thursday, " + thursday + '\n' +
                "Friday, " + friday + '\n' +
                "Saturday, " + saturday + '\n' +
                "Sunday, "  + sunday
                ;
    }

    public void setMonday(ArrayList<HashMap<String, String>> monday) {
        this.monday = monday;
    }

    public void setTuesday(ArrayList<HashMap<String, String>> tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(ArrayList<HashMap<String, String>> wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(ArrayList<HashMap<String, String>> thursday) {
        this.thursday = thursday;
    }

    public void setFriday(ArrayList<HashMap<String, String>> friday) {
        this.friday = friday;
    }

    public void setSaturday(ArrayList<HashMap<String, String>> saturday) {
        this.saturday = saturday;
    }

    public void setSunday(ArrayList<HashMap<String, String>> sunday) {
        this.sunday = sunday;
    }
}
