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
    private double value;
    private double increment;

    public Item(){
    }

    public Item(String title, int hour, int minute, TimePeriod timePeriod, boolean isActive, double value, double increment) {
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

    private int getHour() {
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

    public double getValue() {
        return value;
    }

    public String getValueString() {
        return getDecimalString(getValue());
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getIncrement() {
        return increment;
    }

    public String getIncrementString() {
        return getDecimalString(getIncrement());
    }

    private String getDecimalString(double variable) {
        if ((variable == Math.floor(variable)) && !Double.isInfinite(variable)) {
            // integer type
            return String.valueOf(Double.valueOf(variable).intValue());
        }

        String variableAsString = String.valueOf(variable);
        int numDecimals = getNumDecimalPlaces(variableAsString);
        if (numDecimals == 1) {
            variableAsString = variableAsString + "0";
        } else if (numDecimals > 5) {
            int decimalsToPop = numDecimals - 5;
            variableAsString = variableAsString.substring(0, variableAsString.length() - decimalsToPop);

            // get rid of trailing 0s
            variableAsString = !variableAsString.contains(".") ? variableAsString : variableAsString.replaceAll("0*$", "").replaceAll("\\.$", "");

            if (getNumDecimalPlaces(variableAsString) == 1) { variableAsString = variableAsString + "0"; }
        }

        return variableAsString;
    }

    private int getNumDecimalPlaces(String variableAsString) {
        if (variableAsString.contains(".")) {
            String[] s = variableAsString.split("\\.");
            return s[s.length - 1].length();
        }
        return 0;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public String getFormattedTime() {
        return String.format(Locale.US, "%s:%02d", hour, minute);
    }

    public int get24HourTimeHour() {
        // case when default values are unchanged
        if (hour == 0) { return 0; }

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

        TimePeriod(int period) {
            this.type = period;
        }
    }
}
