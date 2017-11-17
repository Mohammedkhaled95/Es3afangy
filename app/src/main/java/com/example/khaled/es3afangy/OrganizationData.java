package com.example.khaled.es3afangy;

/**
 * Created by mkhaled on 01/10/17.
 */

public class OrganizationData {

    private String number;
    private String name;
    private String availableBeds;
    private String LastUpdate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailableBeds() {
        return availableBeds;
    }

    public void setAvailableBeds(String availableBeds) {
        this.availableBeds = availableBeds;
    }

    public String getLastUpdate() {
        return LastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        LastUpdate = lastUpdate;
    }
}
