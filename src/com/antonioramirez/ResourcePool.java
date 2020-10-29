/*
 * Filename: ResourcePool.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Used to track worker pool effectively. Contains a poolToGUI() method that resembles the approach taken in
 *          the Job class to send all pool information to the GUI.
 */
package com.antonioramirez;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ResourcePool {
    //Used to track all workers
    private ArrayList<Person> workerPool;
    //Number displayed of all non-working workers
    private int availableWorkers;
    //Number displayed of all workers in the port
    private int totalWorkers;
    //Used to display the skill/requirement name in the GUI
    private String skill;
    //Used to display the port name in the GUI
    private String parentPort;

    private JPanel currentPoolPanel;
    private JLabel portLabel, skillLabel, availableLabel, totalLabel;

    //Parameterized constructor
    public ResourcePool(ArrayList<Person> workerPool, String skill, String parentPort) {
        this.workerPool = workerPool;
        this.availableWorkers = getWorkerPool().size();
        this.totalWorkers = getWorkerPool().size();
        this.skill = skill;
        this.parentPort = parentPort;
    }

    //Getters and Setters
    public ArrayList<Person> getWorkerPool() {
        return workerPool;
    }

    public void setWorkerPool(ArrayList<Person> workerPool) {
        this.workerPool = workerPool;
    }

    public int getAvailableWorkers() {
        return availableWorkers;
    }

    public void setAvailableWorkers(int availableWorkers) {
        this.availableWorkers = availableWorkers;
    }

    public int getTotalWorkers() {
        return totalWorkers;
    }

    public void setTotalWorkers(int totalWorkers) {
        this.totalWorkers = totalWorkers;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getParentPort() {
        return parentPort;
    }

    public void setParentPort(String parentPort) {
        this.parentPort = parentPort;
    }

    //Similar to the method used to display individual jobs to the GUI
    //Sends each pool as a row to the GUI
    JPanel poolToGUI(){
        //Capitalizing the first letter of the skill for looks
        //See: https://stackoverflow.com/questions/3904579/how-to-capitalize-the-first-letter-of-a-string-in-java
        String skill = getSkill().substring(0, 1).toUpperCase() + getSkill().substring(1);

        //Panel to hold pool components
        currentPoolPanel = new JPanel(new GridLayout(1, 3));
        //Label displaying the skill/requirement name
        skillLabel = new JLabel(skill, JLabel.CENTER);
        skillLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        //Label displaying the port name
        portLabel = new JLabel(parentPort, JLabel.CENTER);
        portLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        //Displays the current number of available(non-working) workers
        availableLabel = new JLabel("Available: " + getAvailableWorkers(), JLabel.CENTER);
        availableLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        //Displays the total number of workers at the port
        totalLabel = new JLabel("Total: " + getTotalWorkers(), JLabel.CENTER);
        totalLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        //Adding Labels to the panel
        currentPoolPanel.add(skillLabel);
        currentPoolPanel.add(portLabel);
        currentPoolPanel.add(availableLabel);
        currentPoolPanel.add(totalLabel);

        return currentPoolPanel;
    }

    //Adds worker to the ArrayList, updates the available/total counts
    void addPerson(Person person){
        workerPool.add(person);
        setAvailableWorkers(workerPool.size());
        setTotalWorkers(workerPool.size());
    }

    //Sets working flag to true, subtracts 1 from the available count, updates label
    void reservePerson(Person person){
        person.setWorkingFlag(true);
        setAvailableWorkers(availableWorkers - 1);
        availableLabel.setText("Available: " + availableWorkers);
    }

    //Sets working flag to false, adds 1 to the available count, updates label
    void returnPerson(Person person){
        person.setWorkingFlag(false);
        setAvailableWorkers(availableWorkers + 1);
        availableLabel.setText("Available: " + availableWorkers);
    }
}
