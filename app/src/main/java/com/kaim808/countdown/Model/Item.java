package com.kaim808.countdown.Model;

public class Item {

    private String title;
    private String formattedTime;
    private TimePeriod timePeriod;
    private boolean isActive;
    private int value;
    private int increment;

    public Item(String title, String formattedTime, TimePeriod timePeriod, boolean isActive, int value, int increment) {
        this.title = title;
        this.formattedTime = formattedTime;
        this.timePeriod = timePeriod;
        this.isActive = isActive;
        this.value = value;
        this.increment = increment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }
}
