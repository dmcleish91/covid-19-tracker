package com.dmcleish91.COVID19Tracker.models;

public class LocationStats {
    private String state;
    private String country;
    private int latestTotalCases;
    private int average14days;
    private int differenceFromPreviousDay;

    public int getDifferenceFromPreviousDay() {
        return differenceFromPreviousDay;
    }

    public void setDifferenceFromPreviousDay(int differenceFromPreviousDay) {
        this.differenceFromPreviousDay = differenceFromPreviousDay;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public int getAverage14days() {
        return average14days;
    }

    public void setAverage14days(int average14days) {
        this.average14days = average14days;
    }

    @Override
    public String toString() {
        return "LocationStat{" +
                "State='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }
}
