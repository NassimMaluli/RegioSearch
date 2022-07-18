package de.uni_marburg.sp21.model;

import java.io.Serializable;

/**
 * Model for messages (needed for Company)
 *
 * @author chris
 * @see Company
 */
public class Message implements Serializable {
    private String content, date;

    /**
     * no arg constructor
     */
    public Message() {
    }

    /**
     * constructor for a message.
     *
     * @param content the content of the message
     * @param date    the date of the message
     */
    public Message(String content, String date) {
        this.content = content;
        this.date = date;
    }

    /**
     * getter for the content of the message
     *
     * @return the content of the message
     */
    public String getContent() {
        return content;
    }

    /**
     * getter for the date of the message
     *
     * @return the date of the message
     */
    public String getDate() {
        return date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * generates a string to represent a message of a company
     *
     * @return the data of the message as string.
     */
    @Override
    public String toString() {
        return "Message of the Day: " +
                content + '\n' +
                "Date of posting: " + date + '\n' + '\n';
    }
}