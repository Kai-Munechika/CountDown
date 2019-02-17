package com.kaim808.countdown.Model;

enum TimePeriod {
    AM, PM
}

public class Item {

    private String title;
    private String formattedTime;
    private TimePeriod timePeriod;
    private boolean isActive;
    private int value;

    public Item(String title, String formattedTime, TimePeriod timePeriod, boolean isActive, int value) {
        this.title = title;
        this.formattedTime = formattedTime;
        this.timePeriod = timePeriod;
        this.isActive = isActive;
        this.value = value;
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
}
