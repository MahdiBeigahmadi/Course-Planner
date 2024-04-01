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
    private int semester;
    private String subject;
    private String catalogNumber;
    private String location;
    private int enrolementCapacity;

    private int enrolmentTotal;

    private String instructors;

    private String componentCode;

    public Course() {
    }

    public Course(int semester, String SUBJECT, String catalogNumber,
                  String location, int enrolementCapacity, int enrolmentTotal,
                  String instructors, String componentCode) {
        this.semester = semester;
        this.subject = SUBJECT;
        this.catalogNumber = catalogNumber;
        this.location = location;
        this.enrolementCapacity = enrolementCapacity;
        this.enrolmentTotal = enrolmentTotal;
        this.instructors = instructors;
        this.componentCode = componentCode;
    }

    @Override
    public String toString() {
        return "Course{" +
                "SEMESTER=" + semester +
                ", SUBJECT='" + subject + '\'' +
                ", CATALOGNUMBER='" + catalogNumber + '\'' +
                ", LOCATION='" + location + '\'' +
                ", ENROLEMENTCAPACITY=" + enrolementCapacity +
                ", ENROLMENTTOTAL=" + enrolmentTotal +
                ", INSTRUCTORS='" + instructors + '\'' +
                ", COMPONENTCODE='" + componentCode + '\'' +
                '}';
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCATALOGNUMBER() {
        return catalogNumber;
    }

    public void setCATALOGNUMBER(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getEnrolementCapacity() {
        return enrolementCapacity;
    }

    public void setEnrolementCapacity(int enrolementCapacity) {
        this.enrolementCapacity = enrolementCapacity;
    }

    public int getEnrolmentTotal() {
        return enrolmentTotal;
    }

    public void setEnrolmentTotal(int enrolmentTotal) {
        this.enrolmentTotal = enrolmentTotal;
    }

    public String getInstructors() {
        return instructors;
    }

    public void setInstructors(String instructors) {
        this.instructors = instructors;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }
}
