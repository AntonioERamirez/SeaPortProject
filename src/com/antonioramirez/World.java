/*
 * Filename: World.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Builds a world. Tracks all ports, time, and allThings. Generates the JTree
 */
package com.antonioramirez;

import javax.swing.tree.DefaultMutableTreeNode;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

@SuppressWarnings("unchecked")
public class World extends Thing {
    //Class variables, defined in project pdf.
    private ArrayList<SeaPort> ports;
    private PortTime time;
    //List added for easier tracking.
    private ArrayList<Thing> allThings;

    //Constructor using a Scanner parameter.
    World(Scanner scannerContents) {
        super(scannerContents);
        //Initializing ArrayLists
        setAllThings(new ArrayList<>());
        setPorts(new ArrayList<>());
        //Building world, line by line, from .txt file
        process(scannerContents);
    }

    // Getters and setters, defined in project pdf.
    public ArrayList<SeaPort> getPorts() {
        return ports;
    }

    private void setPorts(ArrayList<SeaPort> ports) {
        this.ports = ports;
    }

    public PortTime getTime() {
        return time;
    }

    public void setTime(PortTime time) {
        this.time = time;
    }

    public ArrayList<Thing> getAllThings() {
        return allThings;
    }

    private void setAllThings(ArrayList<Thing> allThings) {
        this.allThings = allThings;
    }

    //Process method goes line by line and creates various object instances.
    //Adjusted to include the necessary HashMaps
    private void process(Scanner scanner) {
        //HashMaps used to improve efficiency, especially index/parent lookup
        HashMap<Integer, SeaPort> portsHM = new HashMap<>();
        HashMap<Integer, Dock> docksHM = new HashMap<>();
        HashMap<Integer, Ship> shipsHM = new HashMap<>();
        //Used to store each line
        String currentLine;
        //Used to scan each line
        Scanner lineScanner;

        //Ensuring all lines are processed
        while (scanner.hasNextLine()) {
            //Storing current line in a String to be scanned
            currentLine = scanner.nextLine().trim();
            //Skipping blank lines
            if (currentLine.length() == 0) {
                continue;
            }

            //Scanning the current line
            lineScanner = new Scanner(currentLine);

            //Evaluating which class object to be instantiated
            //Included adding applicable objects to their respective HashMaps
            if (lineScanner.hasNext()) {
                switch(lineScanner.next().trim()) {
                    case "port":
                        SeaPort newSeaPort = new SeaPort(lineScanner);
                        getAllThings().add(newSeaPort);
                        getPorts().add(newSeaPort);
                        portsHM.put(newSeaPort.getIndex(),newSeaPort);
                        break;
                    case "dock":
                        Dock newDock = new Dock(lineScanner);
                        getAllThings().add(newDock);
                        addThingToList(newDock, "getDocks", portsHM);
                        docksHM.put(newDock.getIndex(),newDock);
                        break;
                    case "pship":
                        PassengerShip newPassengerShip = new PassengerShip(lineScanner, docksHM, portsHM);
                        getAllThings().add(newPassengerShip);
                        assignShip(newPassengerShip, portsHM, docksHM);
                        shipsHM.put(newPassengerShip.getIndex(),newPassengerShip);
                        break;
                    case "cship":
                        CargoShip newCargoShip = new CargoShip(lineScanner, docksHM, portsHM);
                        getAllThings().add(newCargoShip);
                        assignShip(newCargoShip, portsHM, docksHM);
                        shipsHM.put(newCargoShip.getIndex(),newCargoShip);
                        break;
                    case "person":
                        Person newPerson = new Person(lineScanner);
                        getAllThings().add(newPerson);
                        addThingToList(newPerson, "getPersons", portsHM);
                        break;
                    case "job":
                        Job newJob = new Job(lineScanner, shipsHM);
                        getAllThings().add(newJob);
                        //Linking jobs to their corresponding ship using HashMaps
                        linkJobToShip(newJob, shipsHM, docksHM);
                }
            }
        }
    }

    private void linkJobToShip(Job job, HashMap<Integer, Ship> shipsHM, HashMap<Integer, Dock> docksHM){
        Dock dock;
        //Assigns ship variable to the jobs parent ship index using the ship HashMap to recover the ship object.
        Ship ship = shipsHM.get(job.getParent());

        if(ship != null){
            ship.getJobs().add(job);
        } else{
            dock = docksHM.get(job.getParent());
            dock.getShip().getJobs().add(job);
        }
    }

    //Method adjusted to include ports HashMap to replace the getParentByIndex() call
    private <T extends Thing> void addThingToList(T newThing, String methodName, HashMap<Integer,SeaPort>portsHM) {
        //Similar methodology as SeaPortProgram.buildResults().
        SeaPort newPort;
        ArrayList<T> thingsList;
        Method getList;

        //Replaced getParentByIndex(), using HashMap.get()
        newPort = portsHM.get(newThing.getParent());

        try {
            //Pulling ships list or docks list
            getList = SeaPort.class.getDeclaredMethod(methodName);

            //Transferring list, whether it's persons or docks, to generic list
            thingsList = (ArrayList<T>) getList.invoke(newPort);

            if (newPort != null) {
                thingsList.add(newThing);
            }
        } catch (
                NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
            //Catching various exceptions
            System.out.println("Error: " + e);
        }
    }

    //Assigning ship to a dock
    private void assignShip(Ship ship, HashMap<Integer, SeaPort> portsHM, HashMap<Integer, Dock> docksHM) {
        //Used if dock is in que vs dock
        SeaPort port;

        //Replaced getThingByIndex(), using HashMap.get()
        Dock dock = docksHM.get(ship.getParent());

        //If ship does not have dock parent, add to que
        if (dock == null) {
            //Replaced getParentByIndex(), using HashMap.get()
            port = portsHM.get(ship.getParent());
            port.getShips().add(ship);
            port.getQue().add(ship);
            return;
        }

        //If ship does have dock index, assign to dock.
        //Replaced getParentByIndex(), using HashMap.get()
        port = portsHM.get(dock.getParent());
        dock.setShip(ship);
        port.getShips().add(ship);
    }

    //Called by readFile(), builds the JTree
    <T extends Thing>DefaultMutableTreeNode generateTree(){
        DefaultMutableTreeNode root, parent, child;
        Method getterMethod;
        //noinspection MismatchedQueryAndUpdateOfCollection
        HashMap<String, String> classMethodMap;
        ArrayList<T> thingsList;

        root = new DefaultMutableTreeNode("World");
        //Used to add the proper node name to the tree and retrieve the correct list
        classMethodMap = new HashMap<>() {{
            put("Docks", "getDocks");
            put("Ships", "getShips");
            put("Que", "getQue");
            put("Persons", "getPersons");
        }};

        for (SeaPort newPort : this.getPorts()) {
            //Parent will be the port
            parent = new DefaultMutableTreeNode(newPort.getName());
            root.add(parent);

            //Going through each HashMap pair
            //See: https://www.geeksforgeeks.org/hashmap-entryset-method-in-java/
            for (HashMap.Entry<String, String> pair : classMethodMap.entrySet()) {
                try {
                    //Retrieving the correct list getter
                    getterMethod = SeaPort.class.getDeclaredMethod(pair.getValue());
                    //Assigning the retrieved list
                    thingsList = (ArrayList<T>) getterMethod.invoke(newPort);

                    //Create a child node for each pair
                    //Key is the node name, passing the list to a helper method
                    child = this.addListToTree(pair.getKey(), thingsList);
                    //Add the child to the port parent node
                    parent.add(child);
                } catch (NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
                    System.out.println(e);
                }
            }
        }
        return root;
    }

    //Helper method, processes the individual lists, creates nodes
    private <T extends Thing> DefaultMutableTreeNode addListToTree(String name, ArrayList<T>list){
        DefaultMutableTreeNode parent, child;
        String childName;
        Dock dock;
        Ship ship;

        //Assigns the node name using the passed name
        parent = new DefaultMutableTreeNode(name);

        for(T thing : list){
            childName = thing.getName();
            child = new DefaultMutableTreeNode(childName);

            //Special consideration for docks since they can have children nodes of ships
            if(thing instanceof Dock){
                dock = (Dock) thing;
                ship = dock.getShip();

                //If the dock has a ship assigned, link the new node
                if (dock.getShip() != null){
                    childName = ship.getName();
                    child.add(new DefaultMutableTreeNode(childName));
                }
            } else if(thing instanceof Ship){
                //Also need to account for job children nodes
                ship = (Ship) thing;

                if(!ship.getJobs().isEmpty()){
                    for(Job job : ship.getJobs()){
                        childName = job.getName();
                        child.add(new DefaultMutableTreeNode(childName));
                    }
                }
            }
            parent.add(child);
        }
        return parent;
    }

    // Overridden toString method
    @Override
    public String toString() {
        StringBuilder stringOutput = new StringBuilder(">>>>> The world:");

        for (SeaPort seaPort : this.getPorts()) {
            stringOutput. append(seaPort.toString()).append("\n");
        }
        return stringOutput.toString();
    }
}
