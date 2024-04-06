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
    private static int nextId = 1;
    private final int id;
    private int semester;
    private String subject;
    private String catalogNumber;
    private String location;
    private int enrolementCapacity;

    private int enrolmentTotal;

    private String instructors;

    private String componentCode;

    public Course(int semester, String subject, String catalogNumber,
                  String location, int enrolementCapacity, int enrolmentTotal,
                  String instructors, String componentCode) {
        this.id = nextId++;
        this.semester = semester;
        this.subject = subject;
        this.catalogNumber = catalogNumber;
        this.location = location;
        this.enrolementCapacity = enrolementCapacity;
        this.enrolmentTotal = enrolmentTotal;
        this.instructors = instructors;
        this.componentCode = componentCode;
    }

    public int getId() {
        return id;
    }

    public static void resetNextId() {
        nextId = 1;
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

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", semester=" + semester +
                ", subject='" + subject + '\'' +
                ", catalogNumber='" + catalogNumber + '\'' +
                ", location='" + location + '\'' +
                ", enrolementCapacity=" + enrolementCapacity +
                ", enrolmentTotal=" + enrolmentTotal +
                ", instructors='" + instructors + '\'' +
                ", componentCode='" + componentCode + '\'' +
                '}';
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
