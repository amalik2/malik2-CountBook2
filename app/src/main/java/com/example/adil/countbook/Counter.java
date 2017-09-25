package com.example.adil.countbook;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Adil on Sep 13 2017.
 * This class represents a counter that the user has created.
 * All members of the class can be modified through setters, except for the date.
 */

public class Counter implements Serializable {

    // Max and min values that currentValue can be
    private static final long MIN_VALUE = 0, MAX_VALUE = 99999999999999999L;

    private String name;
    private Date date; // When the counter was made or the current value was last changed
    private long currentValue;
    private long initialValue;
    private String comment;

    /**
     * Construct a new Counter object
     * @param cname is the name of the counter
     * @param startValue is what the counter's value will be initially
     * @param ccomment is a description of the counter
     */
    public Counter(String cname, long startValue, String ccomment){
        name = cname;
        initialValue = startValue;
        comment = ccomment;
        currentValue = initialValue;
        resetDate();
    }

    /**
     * @return the name of this counter
     */
    public String getName(){
        return name;
    }

    public void setName(String newName){
        name = newName;
    }

    /**
     * Get when the counter was made or when the current value was last changed
     * @return the date in yyyy-mm-dd format
     */
    public String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * @return the current value of this counter
     */
    public long getCurrentValue(){
        return currentValue;
    }

    /**
     * Set the current value of this counter
     * @param newValue is what the current value will be changed to
     */
    public void setCurrentValue(long newValue){
        currentValue = newValue;
        resetDate();
    }

    /**
     *
     * @return the initial value of this counter
     */
    public long getInitialValue(){
        return initialValue;
    }

    /**
     * Set the initial value of this counter
     * @param newValue is what the initial value will be changed to
     */
    public void setInitialValue(long newValue){
        initialValue = newValue;
    }

    /**
     * Increase the current value by 1
     */
    public void increment(){

        if (currentValue == MAX_VALUE)
            return;

        currentValue++;
        resetDate();
    }

    /**
     * Decrease the current value by 1
     */
    public void decrement(){

        if (currentValue == MIN_VALUE)
            return;

        currentValue--;
        resetDate();
    }

    /**
     * Reset the current value to the counter's initial value
     */
    public void resetValue(){
        currentValue = initialValue;
        resetDate();
    }

    /**
     * Regenerate the date this counter was last edited on
     */
    private void resetDate(){
        date = new Date();
    }


    @Override
    public String toString() {
        return name + ": " + currentValue + " (" + getDate() + ")";
    }

    public String getComment(){
        return comment;
    }

    public void setComment(String description){
        comment = description;
    }
}
