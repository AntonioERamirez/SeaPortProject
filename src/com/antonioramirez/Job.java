/*
 * Filename: Job.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Child class of Thing. Each job implements a thread. Also contains basic GUI components sent to SeaPortProgram.java.
 */
package com.antonioramirez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//Runnable interface used since we are already extending the Thing Class
public class Job extends Thing implements Runnable {
    //Class variables, defined in project pdf.
    private double duration;
    private ArrayList<String> requirements;

    //Status enums defined in pdf
    private enum Status {RUNNING, SUSPENDED, WAITING, DONE}
    //Tracks status of each job
    private Status status;
    //Parents of the Job/Docked ship
    private Ship parentShip;
    private SeaPort parentPort;
    //Persons to perform Job
    private ArrayList<Person> workers;
    //Thread for each job
    private Thread thread;
    //Status flags
    private boolean suspendedFlag, cancelledFlag, finishedFlag;

    //GUI components specific to each job
    private JButton suspendButton, cancelButton;
    private JProgressBar progressBar;
    private JPanel currentJobPanel;
    private JLabel currentJobLabel;
    private JLabel statusLabel;
    //Added to display any job related messages in the GUI per the rubric
    private JTextArea jobMessages;

    //Constructor using a Scanner parameter.
    //Included ships map in order to retrieve parent ships and their port
    Job(Scanner scanner, HashMap<Integer, Ship> shipHashMap) {
        //Pulls parent class constructors to process previous variables within the .txt file.
        super(scanner);
        //Variables unique to the class.
        if (scanner.hasNextDouble()) {
            setDuration(scanner.nextDouble());
        }

        //Initializes the ArrayList for the job requirements.
        setRequirements(new ArrayList<>());
        // While there are more requirements, add each to the ArrayList
        while (scanner.hasNext()) {
            getRequirements().add(scanner.next());
        }

        //Setting the Parent ship, port, and list of workers
        setParentShip(shipHashMap.get(getParent()));
        setParentPort(getParentShip().getPort());
        setWorkers(new ArrayList<>());
        //Resetting the status flags
        setSuspendedFlag(false);
        setCancelledFlag(false);
        setFinishedFlag(false);
        //Set all jobs initial status to suspended, changed depending on if the ship is currently docked
        setStatus(Status.SUSPENDED);
        //New thread for each job created
        setThread(new Thread(this));
    }

    // Getters and setters, defined in project pdf. Some unused for now.
    private double getDuration() {
        return duration;
    }

    private void setDuration(double duration) {
        this.duration = duration;
    }

    ArrayList<String> getRequirements() {
        return requirements;
    }

    private void setRequirements(ArrayList<String> requirements) {
        this.requirements = requirements;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Ship getParentShip() {
        return parentShip;
    }

    public void setParentShip(Ship parentShip) {
        this.parentShip = parentShip;
    }

    public SeaPort getParentPort() {
        return parentPort;
    }

    public void setParentPort(SeaPort parentPort) {
        this.parentPort = parentPort;
    }

    public ArrayList<Person> getWorkers() {
        return workers;
    }

    public void setWorkers(ArrayList<Person> workers) {
        this.workers = workers;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean getSuspendedFlag() {
        return suspendedFlag;
    }

    public void setSuspendedFlag(boolean suspendedFlag) {
        this.suspendedFlag = suspendedFlag;
    }

    public boolean getCancelledFlag() {
        return cancelledFlag;
    }

    public void setCancelledFlag(boolean cancelledFlag) {
        this.cancelledFlag = cancelledFlag;
    }

    public boolean getFinishedFlag() {
        return finishedFlag;
    }

    public void setFinishedFlag(boolean finishedFlag) {
        this.finishedFlag = finishedFlag;
    }

    public JTextArea getJobMessages() {
        return jobMessages;
    }

    public void setJobMessages(JTextArea jobMessages) {
        this.jobMessages = jobMessages;
    }

    //Creates a panel for each job that will be added to a master job panel
    JPanel jobToGUI(){
        currentJobPanel = new JPanel(new GridLayout(1, 5));

        currentJobLabel = new JLabel(getName(), JLabel.CENTER);
        currentJobLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        //Shows jobs status, changes colors to match status
        statusLabel = new JLabel();
        //So color can be seen
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        statusLabel.setVerticalAlignment(JLabel.CENTER);

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        //Buttons defined in the project pdf
        suspendButton = new JButton("Suspend");
        cancelButton = new JButton("Cancel");

        //Suspends current job, can be resumed
        suspendButton.addActionListener((ActionEvent e) -> toggleSuspendFlag());

        //Cancels current job, cannot be undone
        cancelButton.addActionListener((ActionEvent e ) -> toggleCancelFlag());

        //Adding all components to the panel
        currentJobPanel.add(currentJobLabel);
        currentJobPanel.add(progressBar);
        currentJobPanel.add(statusLabel);
        currentJobPanel.add(suspendButton);
        currentJobPanel.add(cancelButton);

        return currentJobPanel;
    }

    //Sets the flag, starts the job thread
    void startJob(){
        setFinishedFlag(false);
        thread.start();
    }

    //For when jobs are cancelled
    void endJob(){
        setCancelledFlag(true);
    }

    //Flags to track status
    private void toggleSuspendFlag(){
        suspendedFlag = !suspendedFlag;
    }

    private void toggleCancelFlag(){
        cancelledFlag = !cancelledFlag;
    }

    //From project pdf
    private void showStatus(Status st){
        switch (st){
            case RUNNING:
                statusLabel.setBackground(Color.GREEN);
                statusLabel.setText("RUNNING");
                break;
            case SUSPENDED:
                statusLabel.setBackground(Color.YELLOW);
                statusLabel.setText("SUSPENDED");
                break;
            case WAITING:
                statusLabel.setBackground(Color.ORANGE);
                statusLabel.setText("WAITING");
                break;
            case DONE:
                statusLabel.setBackground(Color.RED);
                statusLabel.setText("DONE");
                break;
        }
    }

    //Checks to see if ship is still in que
    private synchronized boolean checkQue() {
        //List to store workers
        ArrayList<Person> workers;

        //If the ship is in the que, return true and continue waiting
        if (getParentPort().getQue().contains(getParentShip())) {
            return true;
        } else {
            //If the job has requirements, check if workers are available
            if (!getRequirements().isEmpty()) {
                //Retrieve available qualified workers
                workers = getParentPort().acquireWorkers(this);
                //If no available qualified workers, continue waiting
                if (workers == null) {
                    return true;
                } else {
                    //Set the jobs workers to the retrieved list
                    setWorkers(workers);
                    return false;
                }
            } else {
                //If no requirements, job is ready
                return false;
            }
        }
    }

    @Override
    public void run() {
        //Many elements taken from project pdf
        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 1000 * (long) getDuration();
        double duration = stopTime - time;
        ArrayList<Boolean> allShipsJobs;
        Ship nextShip;
        Dock parentDock;

        //Taken from project pdf
        synchronized(getParentPort()) {
            while (checkQue()) {
                showStatus(Status.WAITING);
                try {
                    getParentPort().wait();
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e);
                }
            }
        }

        //Taken from project pdf
        while (time < stopTime && !getCancelledFlag()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Error: " + e);
            }

            if (!getSuspendedFlag()) {
                showStatus(Status.RUNNING);
                time += 100;
                progressBar.setValue((int)(((time - startTime) / duration) * 100));
            } else {
                showStatus(Status.SUSPENDED);
            }
        }

        //Taken from project pdf
        if (!suspendedFlag) {
            progressBar.setValue(100);
            showStatus(Status.DONE);
            setFinishedFlag(true);
        }

        synchronized(getParentPort()) {
            //Makes workers available for another job after completion
            if (!getRequirements().isEmpty() && !getWorkers().isEmpty()) {
                //Send message to GUI, current job is releasing workers
                jobMessages.append(getName() + " is returning the following workers:\n");
                for(Person worker : getWorkers()){
                    jobMessages.append(" - " + worker.getName() + "\n");
                }
                getParentPort().returnWorkers(getWorkers());
            }

            //Used to ensure all of the ships jobs are completed, before replacing it with a queued ship
            allShipsJobs = new ArrayList<>();

            for(Job job: getParentShip().getJobs()){
                allShipsJobs.add(job.getFinishedFlag());
            }

            //If all ships jobs are completed
            if (!allShipsJobs.contains(false)) {
                //Release docked ship, swap with next queued ship, inform user in GUI
                jobMessages.append(getParentPort().getName()+ "'s dock, " + getParentShip().getDock().getName() + ", is swapping ships for the next job.\n");
                jobMessages.append(" - " + getParentShip().getName() + " is leaving dock " + getParentShip().getDock().getName() + "\n");
                //Grabbing the next ship in the que
                while (!getParentPort().getQue().isEmpty()) {
                    nextShip = getParentPort().getQue().remove(0);

                    //If the next ship has jobs
                    if (!nextShip.getJobs().isEmpty()) {
                        //Using the previous dock
                        parentDock = getParentShip().getDock();
                        //Setting next ship to dock
                        parentDock.setShip(nextShip);
                        nextShip.setDock(parentDock);
                        //Inform user swap was a success
                        jobMessages.append(" - " + nextShip.getName() + " has docked at " + nextShip.getDock().getName() + "\n");
                        jobMessages.append(getParentPort().getName()+ "'s dock, " + getParentShip().getDock().getName() + ", has swapped ships for the next job.\n");
                        break;
                    }
                }
            }

            //Notify
            getParentPort().notifyAll();
        }
    }

    // Overridden toString method
    @Override
    public String toString() {
        StringBuilder output;

        output = new StringBuilder(super.toString() + "\n\tDuration: " + getDuration() + "\n\tRequirements:");

        if(getRequirements().isEmpty()){
            output.append("\n\t\t- No requirements");
        } else {
            for (String requirement : getRequirements()) {
                output.append("\n\t\t - ").append(requirement);
            }
        }

        output.append("\n");

        return output.toString();
    }
}
