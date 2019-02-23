package com.kaim808.countdown.model;

import com.orm.SugarRecord;

import java.util.List;
import java.util.Locale;

public class Item extends SugarRecord<Item> {

    private String title;
    private int hour;
    private int minute;
    private TimePeriod timePeriod;
    private boolean isActive;
    private int value;
    private int increment;

    public Item(){
    }

    public Item(String title, int hour, int minute, TimePeriod timePeriod, boolean isActive, int value, int increment) {
        this.title = title;
        this.hour = hour;
        this.minute = minute;
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

    public int getHour() {
        if (timePeriod == TimePeriod.PM && hour > 12) {
            return hour - 12;
        } else if (timePeriod == TimePeriod.AM && hour == 12) {
            return 0;
        }
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
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

    public String getFormattedTime() {
        return String.format(Locale.US, "%s:%02d", hour, minute);
    }

    public int get24HourTimeHour() {
        int hour = getHour();
        TimePeriod period = getTimePeriod();

        if (hour == 12 && period == TimePeriod.AM) {
            return 0;
        } else if (hour < 12 && period == TimePeriod.AM) {
            return hour;
        }
        // PMs
        else if (hour == 12) {
            return hour;
        } else {
            return hour + 12;
        }
    }

    public static boolean anyItemsActive() {
        return numItemsActive() >= 1;
    }

    public static int numItemsActive() {
        List<Item> items = Item.findWithQuery(Item.class, "SELECT * FROM Item WHERE is_active = ?", "1");
        return items.size();
    }

    public static int numItems() {
        List<Item> items = Item.findWithQuery(Item.class, "SELECT * FROM Item");
        return items.size();
    }

    public enum TimePeriod {
        AM(0), PM(1);

        private int type;

        private TimePeriod(int period) {
            this.type = period;
        }
    }
}
