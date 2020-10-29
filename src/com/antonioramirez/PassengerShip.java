/*
 * Filename: PassengerShip.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Child of Ship. Contains 3 unique variables.
 */
package com.antonioramirez;

import java.util.HashMap;
import java.util.Scanner;

public class PassengerShip extends Ship {
    //Class variables, defined in project pdf.
    private int numberOfOccupiedRooms;
    private int numberOfPassengers;
    private int numberOfRooms;

    //Constructor using a Scanner parameter.
    PassengerShip(Scanner scanner, HashMap<Integer, Dock> dockHashMap, HashMap<Integer, SeaPort> portHashMap) {
        //Pulls parent class constructors to process previous variables within the .txt file.
        super(scanner, dockHashMap, portHashMap);

        //Variables unique to the class
        if (scanner.hasNextInt()) {
            setNumberOfPassengers(scanner.nextInt());
        }
        if (scanner.hasNextInt()) {
            setNumberOfRooms(scanner.nextInt());
        }
        if (scanner.hasNextInt()) {
            setNumberOfOccupiedRooms(scanner.nextInt());
        }
    }

    // Getters and setters, defined in project pdf. Not all currently used.
    private int getNumberOfOccupiedRooms() {
        return numberOfOccupiedRooms;
    }

    private void setNumberOfOccupiedRooms(int numberOfOccupiedRooms) {
        this.numberOfOccupiedRooms = numberOfOccupiedRooms;
    }

    private int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    private void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    private int getNumberOfRooms() {
        return numberOfRooms;
    }

    private void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    // Overridden toString method
    @Override
    public String toString() {
        return "Passenger Ship: " + super.toString();
    }
}
