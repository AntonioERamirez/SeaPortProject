/*
 * Filename: Thing.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Parent class to most other classes.
 */
package com.antonioramirez;

import java.util.Comparator;
import java.util.Scanner;

class Thing implements Comparable<Thing> {
    //Class variables, defined in project pdf.
    private int index;
    private String name;
    private int parent;

    //Constructor using a Scanner parameter.
    Thing(Scanner scanner) {
        //3 variables used by all sub-classes. Used for searching.
        if (scanner.hasNext()) {
            setName(scanner.next());
        }
        if (scanner.hasNextInt()) {
            setIndex(scanner.nextInt());
        }
        if (scanner.hasNextInt()) {
            setParent(scanner.nextInt());
        }
    }

    // Getters and setters, defined in project pdf.
    int getIndex() {
        return index;
    }

    private void setIndex(int index) {
        this.index = index;
    }

    String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    int getParent() {
        return parent;
    }

    private void setParent(int parent) {
        this.parent = parent;
    }

    // Overridden toString method
    @Override
    public String toString(){
        return this.getName() + " " + this.getIndex();
    }

    //Needed due to interface and project pdf. See: https://docs.oracle.com/javase/7/docs/api/java/lang/Comparable.html
    @Override
    public int compareTo(Thing thingInstance) {
        return Integer.compare(this.getIndex(), thingInstance.getIndex());
    }
}
//Used in conjunction with Collections.sort()/List.sort() to sort by defined parameters
//See: https://www.geeksforgeeks.org/collections-sort-java-examples/
class CompareBy implements Comparator<Thing> {
    private String sortBy;

    public CompareBy(String sortBy) {
        this.sortBy = sortBy;
    }

    //Setters and getters
    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    //Takes sortBy string, which is the user selected sorting parameter, and sorts accordingly
    public int compare(Thing thing1, Thing thing2){
        switch (this.sortBy){
            case "Weight":
                return (int) (((Ship)thing1).getWeight() - ((Ship)thing2).getWeight());
            case "Width":
                return (int) (((Ship)thing1).getWidth() - ((Ship)thing2).getWidth());
            case "Length":
                return (int) (((Ship)thing1).getLength() - ((Ship)thing2).getLength());
            case "Draft":
                return (int) (((Ship)thing1).getDraft() - ((Ship)thing2).getDraft());
            case "Name":
                return thing1.getName().compareTo(thing2.getName());
            default:
                return -1;
        }
    }
}
