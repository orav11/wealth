package com.wealth.rating.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PersonWealth {

    @Id
    private long id;
    private String firstName;
    private String lastName;
    private String fortune;

    public PersonWealth(long id, String firstName, String lastName, String fortune) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.fortune = fortune;
    }

    public PersonWealth() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFortune() {
        return fortune;
    }

    public void setFortune(String fortune) {
        this.fortune = fortune;
    }
}
