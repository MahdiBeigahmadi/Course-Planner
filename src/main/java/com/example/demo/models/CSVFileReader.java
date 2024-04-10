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

import com.example.demo.models.apiDots.ApiOfferingDataDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVFileReader {
    private static final String FILE_PATH = "docs/course_data_2018.csv";
    private final List<Course> courseContainer = new ArrayList<>();

    public List<Course> getCourseContainer() {
        return courseContainer;
    }

    public CSVFileReader(){
        Course.resetNextId();
        String line = "";

        Pattern pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); //chatGPT suggested using Pattern Class

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                Matcher matcher = pattern.matcher(line);
                List<String> courseDetails = new ArrayList<>();

                int lastStart = 0;
                while (matcher.find()) {
                    courseDetails.add(line.substring(lastStart, matcher.start()).trim().replace("\"", ""));
                    lastStart = matcher.end();
                }
                courseDetails.add(line.substring(lastStart).trim().replace("\"", ""));

                if (courseDetails.size() < 8) {
                    System.err.println("Incorrect data format for line: " + line);
                    continue;
                }

                try {
                    final Course newCourse = getCourse(courseDetails);
                    courseContainer.add(newCourse);
                } catch (NumberFormatException e) {
                    System.err.println("Number format exception for line: " + line);
                } catch (Exception e) {
                    System.err.println("Unexpected exception for line: " + line);
                    e.printStackTrace();
                }
            }
            courseContainer.sort(new Comparator<Course>() {
                @Override
                public int compare(Course c1, Course c2) {
                    return Integer.compare(c1.getSemester(), c2.getSemester());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Course getCourse(List<String> courseDetails) {
        int semester = Integer.parseInt(courseDetails.get(0));
        String subject = courseDetails.get(1);
        String catalogNumber = courseDetails.get(2);
        String location = courseDetails.get(3);
        int enrollmentCapacity = Integer.parseInt(courseDetails.get(4));
        int enrollmentTotal = Integer.parseInt(courseDetails.get(5));
        String instructor = courseDetails.get(6);
        String componentCode = courseDetails.get(7);

        return new Course(semester, subject, catalogNumber,
                location, enrollmentCapacity, enrollmentTotal, instructor, componentCode);
    }


    public void addToCsvFile(ApiOfferingDataDTO offering) {
        String csvLine = String.format("%s,%s,%s,%s,%s,%s,%s,%s\n",
                offering.getSemester(),
                offering.getSubjectName(),
                offering.getCatalogNumber(),
                offering.getLocation(),
                offering.getEnrollmentCap(),
                offering.getComponent(),
                offering.getInstructor(),
                offering.getEnrollmentTotal());
        appendToCsv(csvLine);
    }

    private void appendToCsv(String csvLine) {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH, true)) {
            fileWriter.write(csvLine);
            System.out.println(csvLine);
            System.out.println("Data added to CSV file successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while adding data to the CSV file.");
            e.printStackTrace();
        }
    }
}