/*
 * Filename:SeaPortProgram.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Contains our main method. Builds the GUI and contains the event handlers. Also reads the .txt input file
 *          and contains our search method. Contains search display helper methods.
 */
package com.antonioramirez;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


class SeaPortProgram extends JFrame {
    //Variable defined in project pdf.
    private World world;
    //Window variables
    private int height;
    private int width;
    private String title;
    //GUI variables
    private JFrame mainFrame;
    private JTextArea resultsTA;
    private JScrollPane resultsSP;
    private JPanel mainPanel, optionsPanel, displayPanel, secondaryPanel, treePanel;
    private JButton readButton, searchButton, sortButton;
    private JLabel searchLabel, sortLabel;
    private JTextField searchTextField;
    private String[] searchComboBoxValues, portSelectComboBoxValues, thingSelectComboBoxValues, sortByComboBoxValues;
    private JComboBox<String> searchComboBox, portSelectComboBox, thingSelectComboBox, sortByComboBox;
    private JFileChooser fileChooser;
    //Scanner for reading .txt file.
    private Scanner scanner;
    //JTree
    private JTree tree;
    private JScrollPane treeSP;
    //Job display
    private JPanel jobSPPanel, jobsPanel;
    private JScrollPane jobSP;
    //Displays pool information
    private JPanel jobsPoolPanel;
    private JScrollPane jobsPoolSP;
    //Displays job messages
    private JTextArea jobMessageTA;
    private JScrollPane jobMessageSP;
    //Holds the pool and message information
    private JPanel jobResourceMessagePanel;

    //Default constructor. Sets window variables.
    private SeaPortProgram(){
        super("Sea Port Program");
        title = "Sea Port Program";
        width = 1600;
        height = 900;
    }

    //Getters and setters. Setters are redundant since the values are initialized in the default constructor.
    //Included due to project pdf guidance. Could use setters in constructor like:
    //this.setWindowTitle("Sea Port Program");
    //this.setWindowHeight(900);
    //this.setWindowWidth(1600);
    private void setWindowTitle(String title) {
        this.title = title;
    }

    private void setWindowWidth(int width) {
        this.width = width;
    }

    private void setWindowHeight(int height) {
        this.height = height;
    }

    //Getters used in buildGUI() method
    private String getWindowTitle(){
        return this.title;
    }

    private int getWindowWidth(){
        return this.width;
    }

    private int getWindowHeight(){
        return this.height;
    }

    //Builds the GUI using our GUI variables.
    private void buildGUI(){
        // Main panel that will hold and organize the other panels.
        mainPanel = new JPanel(new BorderLayout());
        //Adjusted the number of columns to support GUI components for sorting
        optionsPanel = new JPanel(new GridLayout(1,10,5,5));
        //Added a new panel to hold the text areas since we are now using 2
        displayPanel = new JPanel(new GridLayout(1,2,5,5));
        //Tree Panel
        treePanel = new JPanel(new BorderLayout());
        //Jobs Panel
        jobsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        jobResourceMessagePanel = new JPanel(new GridLayout(1,2));
        //Holds the job and display panel
        secondaryPanel = new JPanel(new GridLayout(2,1,5,5));
        //Jobs Panel
        jobSPPanel = new JPanel(new GridLayout(0,1));
        jobsPoolPanel = new JPanel(new GridLayout(0,1));
        //Repeating the process for the results TextArea
        resultsTA = new JTextArea();
        resultsTA.setEditable(false);
        resultsTA.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
        resultsSP = new JScrollPane(resultsTA);
        resultsSP.setBorder(BorderFactory.createTitledBorder("Sort Results"));
        //GUI buttons
        readButton = new JButton("Read File");
        searchButton = new JButton("Search");
        sortButton = new JButton("Sort");
        //Search Components
        searchLabel = new JLabel("Search for:", JLabel.RIGHT);
        searchTextField = new JTextField("", 5);
        searchComboBoxValues = new String[]{"Name", "Index", "Skill"};
        searchComboBox = new JComboBox<>(this.searchComboBoxValues);
        //Sort Components
        sortLabel = new JLabel("Sort:", JLabel.RIGHT);
        portSelectComboBoxValues = new String[]{"All Ports"};
        portSelectComboBox = new JComboBox<>(portSelectComboBoxValues);
        thingSelectComboBoxValues = new String[]{"Que", "Ships", "Docks", "Persons", "Jobs"};
        thingSelectComboBox = new JComboBox<>(thingSelectComboBoxValues);
        sortByComboBoxValues = new String[]{"Name", "Weight", "Width", "Length", "Draft"};
        sortByComboBox = new JComboBox<>(sortByComboBoxValues);

        //Adding read, search, and sort components to panel
        optionsPanel.add(readButton);
        optionsPanel.add(searchLabel);
        optionsPanel.add(searchTextField);
        optionsPanel.add(searchComboBox);
        optionsPanel.add(searchButton);
        optionsPanel.add(sortLabel);
        optionsPanel.add(portSelectComboBox);
        optionsPanel.add(thingSelectComboBox);
        optionsPanel.add(sortByComboBox);
        optionsPanel.add(sortButton);

        //JTree setup
        tree = new JTree();
        tree.setModel(null);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeSP = new JScrollPane(tree);
        treeSP.setBorder(BorderFactory.createTitledBorder("Tree"));
        treePanel.add(treeSP, BorderLayout.CENTER);

        jobSP = new JScrollPane(jobSPPanel);
        jobsPanel.add(jobSP);

        jobsPoolSP = new JScrollPane(jobsPoolPanel);
        jobResourceMessagePanel.add(jobsPoolSP);

        //TextArea to display messages
        jobMessageTA = new JTextArea();
        jobMessageTA.setEditable(false);
        jobMessageTA.setFont(new java.awt.Font("Monospaced", Font.PLAIN, 12));
        jobMessageSP = new JScrollPane(jobMessageTA);
        jobResourceMessagePanel.add(jobMessageSP);

        jobsPanel.add(jobResourceMessagePanel);

        //Adding Text Areas to the display panel
        displayPanel.add(treePanel);
        displayPanel.add(resultsSP);

        secondaryPanel.add(jobsPanel);
        secondaryPanel.add(displayPanel);

        //Adding all panels to the Main panel
        mainPanel.add(secondaryPanel, BorderLayout.CENTER);
        mainPanel.add(optionsPanel, BorderLayout.SOUTH);

        //Titled border like the other components
        jobSP.setBorder(BorderFactory.createTitledBorder("Job Status"));
        jobSPPanel.setBackground(Color.WHITE);
        jobsPoolSP.setBorder(BorderFactory.createTitledBorder("Job Resources"));
        jobsPoolPanel.setBackground(Color.WHITE);
        jobMessageSP.setBorder(BorderFactory.createTitledBorder("Job Messages"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //Event handlers for the buttons within the GUI.
        readButton.addActionListener(e -> readFile());
        searchButton.addActionListener(e -> searchWorld());
        thingSelectComboBox.addActionListener(e -> adjustSortOptions());
        sortButton.addActionListener(e -> sort());


        this.mainFrame = new JFrame(this.getWindowTitle());
        this.mainFrame.setContentPane(this.mainPanel);
        this.mainFrame.setSize(this.getWindowWidth(), this.getWindowHeight());
        this.mainFrame.setVisible(true);
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //Method that is called when the "Read File" button is clicked.
    private void readFile(){
        int selection;
        FileReader fileReader = null;

        //Defined in project pdf.
        //Starts at current directory.
        fileChooser = new JFileChooser(".");

        //showOpenDialog() and APPROVE_OPTION found here:
        //https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
        selection = fileChooser.showOpenDialog(new JFrame());

        if (selection == JFileChooser.APPROVE_OPTION){
            //Try block to handle exception associated with FileReader
            try{
                //Reading selected file and scanning it.
                fileReader = new FileReader(fileChooser.getSelectedFile());
                scanner = new Scanner(fileReader);
            } catch(FileNotFoundException e){
                //Catching a FileNotFoundException
                String message = "File not found! Try again!";
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if(fileReader == null){
            return;
        }

        //Ensuring all currently running job threads are cancelled, and data is cleared
        if (world != null) {
            clearPreviousData();
            clearAllJobs();
        }

        //Creating a World instance with our scanned file data.
        world = new World(scanner);

        //Checking to see if data is empty
        if(world.getAllThings().isEmpty()){
            //Clearing our world instance for another file to be read
            clearPreviousData();
            clearAllJobs();
            world = null;
            //Informing user of empty file
            String message = "File is empty, please try again.";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            //Populate tree with all nodes
            tree.setModel(new DefaultTreeModel(world.generateTree()));
            //Adding ports to the ComboBox for sorting
            adjustPortOptions();
            //Populating Resource Pool Panel
            addAllResourcePools();
            //Start all available Jobs within the world upon generation
            startAllJobs();
        }
    }

    //Method to handle searching of world data
    private void searchWorld(){
        //Empty output
        StringBuilder results = new StringBuilder();
        //Storing user input search term
        String searchFor = searchTextField.getText();
        //Storing user selected search parameter
        //ComboBox returns an int for each selection, each items index.
        int searchType = searchComboBox.getSelectedIndex();

        //Checking to ensure a world has been built
        if (world == null || scanner == null){
            String message = "Please select a file and build a world first.";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Checking to ensure a search term has been entered
        if (searchFor.equals("")){
            String message = "Please enter a valid search term in the text field.";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Using the int from the ComboBox
        switch (searchType){
            //Case 0 or 1 means by name or by index, handled similarly.
            case 0:
            case 1:
                //Calls buildResults() helper method.
                results = new StringBuilder(buildResults(searchType, searchFor));
                //Calls displayResults() helper method
                displayResults(results.toString(), searchFor);
                break;
            case 2:
                //Case 2 is by skill. Must look in all ports since they store all persons in an ArrayList.
                for(SeaPort seaPort : world.getPorts()){
                    //Checking each port's Person ArrayList
                    for (Person person : seaPort.getPersons()){
                        //If the person has the searched for skill, added to output
                        //Each person will be on their own line
                        if(person.getSkill().equals(searchFor)){
                            results.append(person.getName()).append("\nIndex: ").append(person.getIndex()).append("\n");
                        }
                    }
                }
                //Calling displayResults helper method.
                displayResults(results.toString(), searchFor);
                break;
        }
    }

    //Helper method used by case 0 and 1 within the searchWorld() switch.
    private String buildResults(int index, String searchFor){
        //Used to get the correct getter from the correct class
        Method method;
        String methodResult;
        String methodName;
        //Empty output
        StringBuilder results = new StringBuilder();

        //Setting our searched for method name based on ComboBox selection
        if (index == 0){
            methodName = "getName";
        } else {
            methodName = "getIndex";
        }

        //Try block to catch various exceptions
        try{
            //Getting the correct method, getName() or getIndex(), stored in our type Method variable.
            //This works due to the fact that all searched items are children of the Thing class. Because of this,
            //all of the searched objects will contain the getName() and getIndex() methods. This is why we can also
            //use Thing.class.getDeclaredMethod and not have to worry about specific child classes.
            method = Thing.class.getDeclaredMethod(methodName);

            //Searching through all things in the world.
            //Could be more efficient if user specifies which sub-class they are looking for.
            for (Thing thing : world.getAllThings()){
                //Stores result in a String variable
                //Uses invoke() method. See: https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/Method.html
                methodResult = "" + method.invoke(thing);
                if(methodResult.equals(searchFor)){
                    results.append(thing.getClass().getSimpleName()).append(": \n-").append(thing.getName()).append("\n-Index: ").append(thing.getIndex()).append("\n\n");
                }
            }
        } catch (NoSuchMethodException|SecurityException|IllegalAccessException|IllegalArgumentException| InvocationTargetException e){
            //Catching various errors, outputting to console.
            System.out.println(e);
        }
        return results.toString();
    }

    //Helper method, displays search results.
    private void displayResults (String results, String searchFor){
        String message = searchFor + " not found.";
        if(results.equals("")){
            JOptionPane.showMessageDialog(null, message, searchFor + " not found", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,results,"Results for: " + searchFor,JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //Used to adjust sorting options depending if Que is selected per rubric
    private void adjustSortOptions(){
        //Clearing the Combo box from any previous sorting
        sortByComboBox.removeAllItems();
        //All things can be sorted by name
        sortByComboBox.addItem("Name");
        //If Que is selected as thing to be sorted, then add more options
        if(thingSelectComboBox.getSelectedIndex() == 0){
            sortByComboBox.addItem("Weight");
            sortByComboBox.addItem("Width");
            sortByComboBox.addItem("Length");
            sortByComboBox.addItem("Draft");
        }
    }

    //Adding all available world ports to the Combobox
    private void adjustPortOptions(){
        //Clearing the Combo box from any previous sorting
        portSelectComboBox.removeAllItems();
        //If world contains 1 port
        if(world.getPorts().size() == 1){
            for (SeaPort port : world.getPorts()){
                portSelectComboBox.addItem(port.getName());
            }
        } else if (world.getPorts().size() > 1){
            portSelectComboBox.addItem("All Ports");
            world.getPorts().sort(new CompareBy("Name"));
            for (SeaPort port : world.getPorts()){
                portSelectComboBox.addItem(port.getName());
            }
        }
    }

    //Unchecked warning, code is typesafe using generics, warning suppressed
    //See: http://www.informit.com/articles/article.aspx?p=2861454&seqNum=2
    @SuppressWarnings("unchecked")
    private void sort(){
        //Retrieving user selections from the ComboBoxes
        //Objects.requireNonNull() to prevent NullPointerException
        String sortPort = Objects.requireNonNull(portSelectComboBox.getSelectedItem()).toString();
        String sortTarget = Objects.requireNonNull(thingSelectComboBox.getSelectedItem()).toString();
        String sortType = Objects.requireNonNull(sortByComboBox.getSelectedItem()).toString();
        //Used for final output to TextArea
        StringBuilder result = new StringBuilder();
        String fieldMethodName = "";
        String listMethodName = "";
        Method getValue, getThings;
        //Lists used for iteration and storage of all results from different ports
        ArrayList<Thing> finalList = new ArrayList<>();
        ArrayList<Thing> currentList;
        //Checking to ensure a world has been built
        if (world == null || scanner == null){
            String message = "Please select a file and build a world first.";
            JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Switch to determine what thing list needs to be retrieved from the port based on user selection
        switch(sortTarget.trim()){
            case ("Que"):
                listMethodName = "getQue";
                break;
            case ("Ships"):
            case("Jobs"):
                listMethodName = "getShips";
                break;
            case("Docks"):
                listMethodName = "getDocks";
                break;
            case("Persons"):
                listMethodName = "getWorkers";
                break;
            default:
                break;
        }
        //Switch to determine how the list will be sorted based on user selection and the correct field
        switch(sortType.trim()){
            case("Name"):
                fieldMethodName = "getIndex";
                break;
            case("Weight"):
                fieldMethodName = "getWeight";
                break;
            case("Width"):
                fieldMethodName = "getWidth";
                break;
            case("Length"):
                fieldMethodName = "getLength";
                break;
            case("Draft"):
                fieldMethodName = "getDraft";
                break;
            default:
                break;

        }

        try{
            getThings = SeaPort.class.getDeclaredMethod(listMethodName);

            if(sortTarget.equals("Que") && !sortType.equals("Name")){
                getValue = Ship.class.getDeclaredMethod(fieldMethodName);
            } else {
                getValue = Thing.class.getDeclaredMethod(fieldMethodName);
            }

            //Iterating through all ports and aggregating separate port results of current list into the final list
            if(sortPort.equals("All Ports")){
                for(SeaPort port : world.getPorts()){
                    currentList =(ArrayList<Thing>) getThings.invoke(port);
                    finalList.addAll(currentList);
                }
            }else{
                //If only one port, no need to use current list, since no aggregation is required
                for(SeaPort port : world.getPorts()){
                    if (port.getName().equals(sortPort)){
                        finalList.addAll((ArrayList<Thing>)getThings.invoke(port));
                    }
                }
            }

            //Extracting and storing the jobs from the ships stored in the final list
            if(sortTarget.equals("Jobs")){
                currentList = new ArrayList<>();
                for (Thing thing : finalList) {
                    Ship ship = (Ship) thing;
                    //Adding each ships jobs to a list
                    currentList.addAll(ship.getJobs());
                }

                //Removing ships from the list and replacing them with the jobs
                finalList.clear();
                finalList.addAll(currentList);
            }

            if(finalList.isEmpty()){
                result.append("No results to sort");
            } else {
                finalList.sort(new CompareBy(sortType));
                for(Thing thing : finalList){
                    if (sortType.equals("Name")){
                        result.append("- ").append(thing.getName()).append(" (Index: ").append(getValue.invoke(thing)).append(")\n");
                    }else {
                        result.append("- ").append(thing.getName()).append(" (").append(sortType).append(": ").append(getValue.invoke(thing)).append(")\n");
                    }
                }
            }
        }catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException |
                InvocationTargetException | SecurityException e){
            System.out.println(e);
        }

        resultsTA.append("*****SORT RESULTS*****");
        resultsTA.append("\n\nSort: " + sortPort + " " + sortTarget + " by " + sortType + "\n\n");
        resultsTA.append(result + "\n");
    }

    //Called in readFile()
    private void startAllJobs(){
        //For each port
        for (SeaPort port : this.world.getPorts()) {
            //For each ports dock
            for (Dock dock : port.getDocks()) {
                //If the docked ship does not have a job
                if (dock.getShip().getJobs().isEmpty()) {
                    //Remove the ship
                    dock.setShip(null);
                    //While there are still ships in the ports que
                    while (!port.getQue().isEmpty()) {
                        //Get next ship from que
                        Ship newShip = port.getQue().remove(0);
                        //If the next ship has jobs
                        if (!newShip.getJobs().isEmpty()) {
                            //Dock the ship
                            dock.setShip(newShip);
                            break;
                        }
                    }
                }
                //Assign dock to ship
                dock.getShip().setDock(dock);
            }
            //Populate Job section of GUI for all jobs
            for (Ship ship : port.getShips()) {
                //If the ship has a job
                if (!ship.getJobs().isEmpty()) {
                    //Some ships have multiple jobs
                    for (Job job : ship.getJobs()) {
                        //Call Job.jobToGUI() helper method
                        jobSPPanel.add(job.jobToGUI());
                        jobSPPanel.revalidate();
                        jobSPPanel.repaint();
                        //So messages can be properly displayed
                        job.setJobMessages(jobMessageTA);
                        //Start the job
                        job.startJob();
                    }
                }
            }
        }
    }

    private void clearAllJobs(){
        //Clear the job panel
        jobSPPanel.removeAll();
        //Cycle through all possible jobs, and cancel them
        for(Thing thing : world.getAllThings()){
            if(thing instanceof Job){
                ((Job) thing).endJob();
            }
        }
    }

    //Clears all previous data
    //Used when loading a new world
    private void clearPreviousData(){
        jobMessageTA.setText("");
        resultsTA.setText("");
        tree.setModel(null);
        jobSPPanel.removeAll();
        jobsPoolPanel.removeAll();
    }

    //Creates the pools for all ports, and adds the information to the GUI
    private void addAllResourcePools(){
        //Iterate through all ports
        for(SeaPort port : world.getPorts()){
            //Create all pools for each port
            port.createPools();
            //Iterate through the newly created pools
            for(HashMap.Entry<String, ResourcePool> pair : port.getResourcePools().entrySet()){
                //For each pool, send the data to the GUI
                jobsPoolPanel.add(pair.getValue().poolToGUI());
            }
        }
    }

    //Main method
    public static void main(String[] args) {
	    SeaPortProgram seaPort = new SeaPortProgram();
	    seaPort.buildGUI();
    }
}
