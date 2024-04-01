package com.example.demo.models;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApiCourseOfferingDTO {
    private long departmentId;
    private long courseOfferingId;
    private String location;
    private String instructors;
    private String term;
    private long semesterCode;
    private int year;

    public ApiCourseOfferingDTO() {
    }

    public ApiCourseOfferingDTO(long departmentId, long courseOfferingId, String location, String instructors, String term, long semesterCode, int year) {
        this.departmentId = departmentId;
        this.courseOfferingId = courseOfferingId;
        this.location = location;
        this.instructors = instructors;
        this.term = term;
        this.semesterCode = semesterCode;
        this.year = year;
    }


    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
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

    public List<ApiCourseOfferingDTO> extractInformationBasedOnCourseIdAndDepartmentId() {
        CSVFileReader file = new CSVFileReader();
        file.extractDataFromCSVFile();
        List<Course> tempCourses = new ArrayList<>(file.getCourseContainer());
        List<ApiCourseOfferingDTO> filteredCourses = new ArrayList<>();

        System.out.println("Starting to filter courses for Department ID: " + getDepartmentId() + " and Course Offering ID: " + getCourseOfferingId());

        for (Course temp : tempCourses) {
            if (Objects.equals(temp.getSubject().trim(), checkDepartmentID(getDepartmentId())) &&
                    Objects.equals(temp.getCatalogNumber().trim(), String.valueOf(getCourseOfferingId()))) {
                System.out.println("Match found: " + temp);
                SemesterData semesterData = getDataForSemesterCode(temp.getSemester());
                filteredCourses.add(new ApiCourseOfferingDTO(getDepartmentId(),
                        getCourseOfferingId(),
                        temp.getLocation(),
                        temp.getInstructors(),
                        semesterData.term,
                        temp.getSemester(),
                        semesterData.year));
            } else {
                System.out.println("No match for Course: " + temp);
            }
        }

        if (filteredCourses.isEmpty()) {
            System.out.println("No courses matched the criteria.");
        }
        return filteredCourses;
    }

    private SemesterData getDataForSemesterCode(long semesterCode) {
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

    private String checkDepartmentID(long departmentId) {
        return switch ((int) departmentId) {
            case 1 -> "ENSC";
            case 2 -> "CMPT";
            case 3 -> "MSE";
            case 4 -> "MATH";
            case 5 -> "IAT";
            case 6 -> "DDP";
            case 7 -> "TECH";
            case 8 -> "MACM";
            case 9 -> "REM";
            case 10 -> "CMNS";
            case 11 -> "CHIN";
            case 12 -> "KIN";
            case 13 -> "IART";
            case 14 -> "WKTM";
            default -> "Failed";
        };
    }

    // Inner class to hold semester data
    private static class SemesterData {
        String term;
        int year;

        SemesterData(String term, int year) {
            this.term = term;
            this.year = year;
        }
    }
}