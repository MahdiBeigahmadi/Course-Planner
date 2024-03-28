package com.example.demo.models;

/* Course class
 * Course.java
 *
 * Class Description: This class contains course information such as instructor name, location, etc.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April. 2024
 */
public class Course {
    private int SEMESTER;
    private String SUBJECT;
    private String CATALOGNUMBER;
    private String LOCATION;
    private int ENROLEMENTCAPACITY;

    private int ENROLMENTTOTAL;

    private String INSTRUCTORS;

    private String COMPONENTCODE;

    public Course() {
    }

    public Course(int SEMESTER, String SUBJECT, String CATALOGNUMBER,
                  String LOCATION, int ENROLEMENTCAPACITY, int ENROLMENTTOTAL,
                  String INSTRUCTORS, String COMPONENTCODE) {
        this.SEMESTER = SEMESTER;
        this.SUBJECT = SUBJECT;
        this.CATALOGNUMBER = CATALOGNUMBER;
        this.LOCATION = LOCATION;
        this.ENROLEMENTCAPACITY = ENROLEMENTCAPACITY;
        this.ENROLMENTTOTAL = ENROLMENTTOTAL;
        this.INSTRUCTORS = INSTRUCTORS;
        this.COMPONENTCODE = COMPONENTCODE;
    }

    public int getSEMESTER() {
        return SEMESTER;
    }

    public void setSEMESTER(int SEMESTER) {
        this.SEMESTER = SEMESTER;
    }

    public String getSUBJECT() {
        return SUBJECT;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public String getCATALOGNUMBER() {
        return CATALOGNUMBER;
    }

    public void setCATALOGNUMBER(String CATALOGNUMBER) {
        this.CATALOGNUMBER = CATALOGNUMBER;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public int getENROLEMENTCAPACITY() {
        return ENROLEMENTCAPACITY;
    }

    public void setENROLEMENTCAPACITY(int ENROLEMENTCAPACITY) {
        this.ENROLEMENTCAPACITY = ENROLEMENTCAPACITY;
    }

    public int getENROLMENTTOTAL() {
        return ENROLMENTTOTAL;
    }

    public void setENROLMENTTOTAL(int ENROLMENTTOTAL) {
        this.ENROLMENTTOTAL = ENROLMENTTOTAL;
    }

    public String getINSTRUCTORS() {
        return INSTRUCTORS;
    }

    public void setINSTRUCTORS(String INSTRUCTORS) {
        this.INSTRUCTORS = INSTRUCTORS;
    }

    public String getCOMPONENTCODE() {
        return COMPONENTCODE;
    }

    public void setCOMPONENTCODE(String COMPONENTCODE) {
        this.COMPONENTCODE = COMPONENTCODE;
    }
}
