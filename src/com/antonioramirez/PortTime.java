/*
 * Filename: PortTime.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Stores the time at each port.
 */
package com.antonioramirez;

public class PortTime {
    //Class variables, defined in project pdf.
    private int time;

    //Constructor using a Scanner parameter.
    PortTime(int time) {
        setTime(time);
    }

    // Getters and setters, defined in project pdf.
    private int getTime() {
        return time;
    }

    private void setTime(int time) {
        this.time = time;
    }

    // Overridden toString method
    @Override
    public String toString() {
        return "Time: " + this.getTime();
    }
}
