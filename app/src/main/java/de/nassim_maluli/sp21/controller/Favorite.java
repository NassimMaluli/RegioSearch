package de.uni_marburg.sp21.controller;

import java.util.Objects;

import de.uni_marburg.sp21.model.Company;

public class Favorite {
    Company company;
    boolean newMessages = false;

    Favorite(Company company) {
        this.company = company;
    }

    Favorite(Company company, boolean newMessages) {
        this.company = company;
        this.newMessages = newMessages;
    }

    public boolean checkForNewMessages(Company c) {
        return company.messages.size() != c.messages.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(company, ((Favorite)o).company);
    }
}
