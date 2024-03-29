package com.example.demo.models;

/* CSVFileReader class
 * CSVFileReader.java
 *
 * Class Description: It gets the data from CSV files.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April 2024
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CSVFileReader {
    private final List<Course> courseContainer = new ArrayList<>();

    public String subject;
    public String catalogNumber;
    public CSVFileReader() {}

    public List<Course> getCourseContainer() {
        return courseContainer;
    }

    public void extractDataFromCSVFile() {
        String filePath = "data/course_data_2018.csv";
        String line = "";
        String splitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] course = line.split(splitBy, -1);

                int semester, enrollmentCapacity, totalEnrollment;
                try {
                    semester = Integer.parseInt(course[0]);
                    enrollmentCapacity = Integer.parseInt(course[4]);
                    totalEnrollment = Integer.parseInt(course[5]);
                } catch (NumberFormatException e) {
                    System.err.println("Number format exception for line: " + line);
                    continue;
                }
                Course newCourse = new Course(semester, course[1], course[2],
                        course[3], enrollmentCapacity, totalEnrollment, course[6], course[7]);
                courseContainer.add(newCourse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void extractDataFromCSVFileAndFilter(String subject, String catalogNumber) {
        this.subject = subject;
        this.catalogNumber = catalogNumber;
        extractDataFromCSVFile();
        List<Course> filteredCourses = findBySubjectAndCatalogNumber();
        courseContainer.clear();
        courseContainer.addAll(filteredCourses);
    }

    public List<Course> findBySubjectAndCatalogNumber() {
        List<Course> filteredList = new ArrayList<>();
        for (Course course : courseContainer) {
            if (course.getSUBJECT().trim().equals(subject) && Objects.equals(course.getCATALOGNUMBER().trim(), catalogNumber)) {
                filteredList.add(course);
            }
        }
        return filteredList;
    }
}
