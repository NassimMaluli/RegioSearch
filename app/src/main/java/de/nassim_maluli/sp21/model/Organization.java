package de.uni_marburg.sp21.model;

import java.io.Serializable;

/**
 * Model class for an Organization (needed by Company)
 *
 * @author chris
 * @see Company
 */
public class Organization implements Serializable {
    private Integer id;
    private String name, url;

    /**
     * no arg constructor
     */
    public Organization() {
    }

    /**
     * constructor for an organization
     * @param id the id of the organization
     * @param name the name of the organization
     * @param url the url of the organization
     */
    public Organization(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    /**
     * getter for the id
     * @return the id of the organization
     */
    public Integer getId() {
        return id;
    }

    /**
     * getter for the name
     * @return the name of the organization
     */
    public String getName() {
        return name;
    }

    /**
     * getter for the url
     * @return the url of the organization
     */
    public String getUrl() {
        return url;
    }

    /**
     * generates a string to represent the organization
     * @return the string containing all information about the organization
     */
    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
