/*
* Filename: CargoShip.java
* Date: 9 Oct 19
* Author: Antonio Ramirez
* Purpose: Child class of Ship. Contains 3 unique class variables.
*/
package com.antonioramirez;

import java.util.HashMap;
import java.util.Scanner;

public class CargoShip extends Ship {
    //Class variables, defined in project pdf.
    private double cargoValue;
    private double cargoVolume;
    private double cargoWeight;

    //Constructor using a Scanner parameter.
     CargoShip(Scanner scanner, HashMap<Integer, Dock> dockHashMap, HashMap<Integer, SeaPort> portHashMap) {
         //Pulls parent class constructors to process previous variables within the .txt file.
        super(scanner, dockHashMap, portHashMap);

        //Variables unique to the class.
        if (scanner.hasNextDouble()) {
            setCargoWeight(scanner.nextDouble());
        }
        if (scanner.hasNextDouble()) {
            setCargoVolume(scanner.nextDouble());
        }
        if (scanner.hasNextDouble()) {
            setCargoValue(scanner.nextDouble());
        }
    }

    // Getters and setters, defined in project pdf. Getters are unused.
    private double getCargoValue() {
        return cargoValue;
    }

    private void setCargoValue(double cargoValue) {
        this.cargoValue = cargoValue;
    }

    private double getCargoVolume() {
        return cargoVolume;
    }

    private void setCargoVolume(double cargoVolume) {
        this.cargoVolume = cargoVolume;
    }

    private double getCargoWeight() {
        return cargoWeight;
    }

    private void setCargoWeight(double cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    // Overridden toString method
    @Override
    public String toString() {
        return "Cargo Ship: " + super.toString();
    }
}
