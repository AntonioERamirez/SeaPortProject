/*
 * Filename: SeaPort.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Child class of Thing. Tracks docks, awaiting ships, all ships, and persons in individual
 *          ArrayLists. Extensive toString method since everything is stored in this class's ArrayLists.
 */
package com.antonioramirez;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SeaPort extends Thing {
    //Class variables, defined in project pdf.
    private ArrayList<Dock> docks;
    private ArrayList<Ship> que;
    private ArrayList<Ship> ships;
    private ArrayList<Person> persons;

    //Map to track all of the resource pools.
    //Each unique skill will have its own pool
    private HashMap<String, ResourcePool> resourcePools;

    //Constructor using a Scanner parameter.
    SeaPort(Scanner scanner) {
        //Pulls parent class constructors to process previous variables within the .txt file.
        super(scanner);
        //Initializes a new ArrayList set for each new SeaPort.
        setDocks(new ArrayList<>());
        setQue(new ArrayList<>());
        setShips(new ArrayList<>());
        setPersons(new ArrayList<>());
        //Initialize pool map for each port
        setResourcePools(new HashMap<>());
    }

    // Getters and setters, defined in project pdf.
    ArrayList<Dock> getDocks() {
        return docks;
    }

    private void setDocks(ArrayList<Dock> docks) {
        this.docks = docks;
    }

    ArrayList<Ship> getQue() {
        return que;
    }

    private void setQue(ArrayList<Ship> que) {
        this.que = que;
    }

    ArrayList<Ship> getShips() {
        return ships;
    }

    private void setShips(ArrayList<Ship> ships) {
        this.ships = ships;
    }

    ArrayList<Person> getPersons() {
        return persons;
    }

    private void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public HashMap<String, ResourcePool> getResourcePools() {
        return resourcePools;
    }

    public void setResourcePools(HashMap<String, ResourcePool> resourcePools) {
        this.resourcePools = resourcePools;
    }

    //Used to acquire available qualified workers
    synchronized ArrayList<Person> acquireWorkers(Job job) {
        //List to store and send qualified workers
        ArrayList<Person> workers = new ArrayList<>();
        //Ensures all job requirements are met
        boolean allReqsMet = true;
        //Used to store current required skill's pool
        //Used to ensure a sufficient number of qualified workers exist in the port
        ResourcePool currentSkillPool;
        //Map to track the requirements of each job
        //Tracks the number of each skill required (i.e. 2 Janitors)
        HashMap<String, Integer> jobReqMap = new HashMap<>();

        //Iterating through the job's requirements, adding each skill and number needed to the map
        //See: https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#merge-K-V-java.util.function.BiFunction-
        //See: https://stackoverflow.com/questions/81346/most-efficient-way-to-increment-a-map-value-in-java
        for(String skill : job.getRequirements()){
            jobReqMap.merge(skill,1, Integer::sum);
        }

        outerLoop:
        //Iterating through job requirements
        for(String skill : job.getRequirements()){
            //Set current pool to the current job requirement
            currentSkillPool = getResourcePools().get(skill);

            //If the pool is empty, no workers with that skill at this port
            if (currentSkillPool == null){
                //Inform user in the GUI
                job.getJobMessages().append("No qualified workers found to complete " + job.getName() + "\n");
                //Properly return any reserved workers for this job
                //Job cannot be completed
                returnWorkers(workers);
                //End the job
                job.endJob();
                //Return an empty list
                return new ArrayList<>();
            } else if (currentSkillPool.getWorkerPool().size() < jobReqMap.get(skill)){
                //If pool is not empty, but does not have the required amount of qualified workers for the requirement
                //Inform user in the GUI
                job.getJobMessages().append("Sufficient number of qualified workers not found for " + job.getName() + "\n");
                //Properly return any reserved workers for this job
                //Job cannot be completed
                returnWorkers(workers);
                //End the job
                job.endJob();
                //Return an empty list
                return new ArrayList<>();
            } else {
                //If there are sufficient qualified workers
                //Iterate through the workers
                for(Person worker : currentSkillPool.getWorkerPool()){
                    //If they aren't currently working
                    if(!worker.isWorkingFlag()){
                        //Reserve the worker
                        currentSkillPool.reservePerson(worker);
                        //Add the worker to the list to be returned
                        workers.add(worker);
                        //Continue iterating
                        continue outerLoop;
                    }
                }
                //If anything fails, set to false
                allReqsMet = false;
                //Stop iterating
                break;
            }
        }

        //As long as all job requirements have been met, inform user through the GUI and return the list
        if(allReqsMet){
            job.getJobMessages().append(job.getName() + " is reserving the following workers:\n");
            for(Person worker : workers){
                job.getJobMessages().append(" - " + worker.getName() + "\n");
            }
            return workers;
        } else{
            //Properly return any leftover workers
            returnWorkers(workers);
            //Return null as a fail
            return null;
        }
    }

    //Sets the worker flags back to false
    synchronized void returnWorkers(ArrayList<Person> resources) {
        resources.forEach((Person worker) -> getResourcePools().get(worker.getSkill()).returnPerson(worker));
    }

    void createPools() {
        ResourcePool currentPool;

        //Iterate through all workers
        for (Person worker : getPersons()) {
            //Check if a pool for current worker's skill has been created
            currentPool = getResourcePools().get(worker.getSkill());

            // Create the pool if pool for current skill has not been created
            if (currentPool == null) {
                currentPool = new ResourcePool(new ArrayList<>(), worker.getSkill(), getName());
                //Add the new pool to the map
                getResourcePools().put(worker.getSkill(), currentPool);
            }
            //Add the worker to the pool
            currentPool.addPerson(worker);
        }
    }

    // Overridden toString method
    //Designed output to resemble style in project pdf
    @Override
    public String toString() {
        StringBuilder result;

        //Adding SeaPort and associated Docks to the output
        //Relies on Dock.toString() method
        result = new StringBuilder("\n\nSeaPort: " + super.toString() + "\n");
        //For loop to cycle through all of the port's docks.
        for (Dock dock: getDocks()) {
            result.append("\n").append(dock.toString()).append("\n");
        }

        //Adding all ships in the que to the output
        result.append("\n--- List of all ships in que:");
        //For loop to cycle through all ships in the que list
        for (Ship queuedShip: getQue()) {
            result.append("\n>").append(queuedShip.getClass().getSimpleName()).append(": ").append(queuedShip.toString());
        }

        result.append("\n");

        //Adding all ships, docked and queued, to the output.
        //Slightly redundant since all ships have already been listed through
        //the dock.toString() and the que list. Included in project pdf.
        result.append("\n--- List of all ships:");
        //For loop to cycle through the ships list.
        for (Ship ship: getShips()) {
            result.append("\n>").append(ship.getClass().getSimpleName()).append(": ").append(ship.getName()).append(" ").append(ship.getIndex());
        }

        result.append("\n");

        //Adding all persons to the output
        result.append("\n--- List of all persons:");
        //For loop to cycle through all people
        for (Person person: getPersons()) {
            result.append("\n>").append(person.toString());
        }

        result.append("\n");

        return result.toString();
    }
}
