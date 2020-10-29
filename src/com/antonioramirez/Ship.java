/*
 * Filename: Ship.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Child class of Thing. Parent class to CargoShip and PassengerShip. Each ship can also store multiple
 *          jobs in the jobs ArrayList.
 */
package com.antonioramirez;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Ship extends Thing {
    //Class variables, defined in project pdf.
    private PortTime arrivalTime, dockTime;
    private double draft, length, weight, width;
    private ArrayList<Job> jobs;

    private SeaPort port;
    private Dock dock;
    private HashMap<Integer, Dock> dockHashMap;

    //Constructor using a Scanner parameter.
    Ship(Scanner scannerContents, HashMap<Integer, Dock> dockHashMap, HashMap<Integer, SeaPort> portHashMap) {
        //Pulls parent class constructors to process previous variables within the .txt file.
        super(scannerContents);
        //Variables unique to the class.
        if (scannerContents.hasNextDouble()) {
            setWeight(scannerContents.nextDouble());
        }
        if (scannerContents.hasNextDouble()) {
            setLength(scannerContents.nextDouble());
        }
        if (scannerContents.hasNextDouble()) {
            setWidth(scannerContents.nextDouble());
        }
        if (scannerContents.hasNextDouble()) {
            setDraft(scannerContents.nextDouble());
        }
        //Initializing a new jobs ArrayList for each new ship.
        setJobs(new ArrayList<>());
        setPort(dockHashMap, portHashMap);
        setDockHashMap(dockHashMap);
        setDock();
    }

    // Getters and setters, defined in project pdf. Most are currently unused.
    public PortTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(PortTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public PortTime getDockTime() {
        return dockTime;
    }

    public void setDockTime(PortTime dockTime) {
        this.dockTime = dockTime;
    }

    double getDraft() {
        return draft;
    }

    private void setDraft(double draft) {
        this.draft = draft;
    }

    double getLength() {
        return length;
    }

    private void setLength(double length) {
        this.length = length;
    }

    double getWeight() {
        return weight;
    }

    private void setWeight(double weight) {
        this.weight = weight;
    }

    double getWidth() {
        return width;
    }

    private void setWidth(double width) {
        this.width = width;
    }

    ArrayList<Job> getJobs() {
        return jobs;
    }

    private void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    public SeaPort getPort() {
        return port;
    }

    public void setPort(SeaPort port) {
        this.port = port;
    }

    //Setter with 2 parameters, uses the dock and port HashMaps
    public void setPort(HashMap<Integer, Dock> dockHashMap, HashMap<Integer, SeaPort> portHashMap){
        port = portHashMap.get(getParent());

        //If the parent is null from the portHashMap, then the ship is docked
        if(port == null){
            //Retrieving the port from the docks parent instead
            Dock dock = dockHashMap.get(getParent());
            port = portHashMap.get(dock.getParent());
        }
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    //Setter with no parameter, checks if there is a dock, if there is, sets it
    public void setDock(){
        dock = dockHashMap.getOrDefault(getParent(), null);
    }

    public HashMap<Integer, Dock> getDockHashMap() {
        return dockHashMap;
    }

    public void setDockHashMap(HashMap<Integer, Dock> dockHashMap) {
        this.dockHashMap = dockHashMap;
    }

    // Overridden toString method
    @Override
    public String toString() {
        StringBuilder output;

        output = new StringBuilder(super.toString() + "\n\tJobs:\n");

        if(getJobs().isEmpty()){
            output.append("\n\t-No jobs\n");
        } else{
            for(Job job : getJobs()){
                output.append("\n\t- ").append(job.toString());
            }
        }

        return output.toString();
    }
}
