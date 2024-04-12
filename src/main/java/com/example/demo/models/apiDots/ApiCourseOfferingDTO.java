package com.example.demo.models.apiDots;

/* ApiCourseOfferingDTO class
 * ApiCourseOfferingDTO.java
 *
 * Class Description: It handles when the user clicks on catalog number to see the offerings.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April 2024
 */

public class ApiCourseOfferingDTO {
    private long courseOfferingId;
    private String location;
    private String instructors;
    private String term;
    private long semesterCode;
    private int year;
    public ApiCourseOfferingDTO() {
    }

    public ApiCourseOfferingDTO(long semesterCode, String term, int year, String instructors, String location) {
        this.location = location;
        this.instructors = instructors;
        this.term = term;
        this.semesterCode = semesterCode;
        this.year = year;
    }

    public long getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(long courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInstructors() {
        return instructors;
    }

    public void setInstructors(String instructors) {
        this.instructors = instructors;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public long getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(long semesterCode) {
        this.semesterCode = semesterCode;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public SemesterData getDataForSemesterCode(long semesterCode) {
        String code = String.valueOf(semesterCode);
        int X = Character.getNumericValue(code.charAt(0));
        int Y = Character.getNumericValue(code.charAt(1));
        int Z = Character.getNumericValue(code.charAt(2));
        int A = Character.getNumericValue(code.charAt(3));

        int year = 1900 + 100 * X + 10 * Y + Z;
        String term;
        switch (A) {
            case 1:
                term = "Spring";
                break;
            case 4:
                term = "Summer";
                break;
            case 7:
                term = "Fall";
                break;
            default:
                term = "Invalid";
                System.out.println("Invalid semester code: " + semesterCode);
        }

        return new SemesterData(term, year);
    }

    // Inner class to hold semester data
    public static class SemesterData {
        public String term;
        public int year;

        SemesterData(String term, int year) {
            this.term = term;
            this.year = year;
        }
    }
}