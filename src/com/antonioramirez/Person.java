/*
 * Filename: Person.java
 * Date: 9 Oct 19
 * Author: Antonio Ramirez
 * Purpose: Child class of Thing. Each person can have a skill, will most likely relate to a job.
 */
package com.antonioramirez;

import java.util.Scanner;

public class Person extends Thing {
    //Class variables, defined in project pdf.
    private String skill;
    //Added to track if person is busy, prevents working on multiple jobs
    private boolean workingFlag;

    //Constructor using a Scanner parameter.
    Person(Scanner scanner) {
        //Pulls parent class constructors to process previous variables within the .txt file.
        super(scanner);

        //Checks for a skill
        if (scanner.hasNext()) {
            setSkill(scanner.next());
        } else {
            setSkill("Person has no skill.");
        }

        //Person is not working by default
        workingFlag = false;
    }

    // Getters and setters, defined in project pdf.
    String getSkill() {
        return skill;
    }

    private void setSkill(String skill) {
        this.skill = skill;
    }

    public boolean isWorkingFlag() {
        return workingFlag;
    }

    public void setWorkingFlag(boolean workingFlag) {
        this.workingFlag = workingFlag;
    }

    // Overridden toString method
    @Override
    public String toString() {
        return "Person: " + super.toString() + " " + this.getSkill();
    }
}
