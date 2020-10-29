/*
 * Filename:Dock.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Child class of Thing. Can hold one Ship object.
 */
package com.antonioramirez;

import java.util.Scanner;

public class Dock extends Thing {
    //Class variable, defined in project pdf.
    private Ship ship;

    //Constructor using a Scanner parameter.
    Dock(Scanner scanner) {
        //Pulls parent class constructors to process previous variables within the .txt file.
        //Does not instantiate a Ship object.
        //Ship object associated in World.assignShip()
        super(scanner);
    }

    // Getters and setters, defined in project pdf.
    Ship getShip() {
        return ship;
    }

    void setShip(Ship ship) {
        this.ship = ship;
    }

    // Overridden toString method
    @Override
    public String toString() {
        //Displaying dock name
        String result = "  Dock: " + super.toString() + "\n    ";

        //Checks if dock is empty.
        if (getShip() == null) {
            result += "No ships at dock.";
        } else {
            //Displaying ship currently in dock
            result += "Ship: " + getShip().toString();
        }
        return result;
    }
}
