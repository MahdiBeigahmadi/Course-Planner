package com.example.demo.models;

import java.util.*;

/* ApiCourseDTO class
 * ApiCourseDTO.java
 *
 * Class Description: It transfers data for about.html page.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April. 2024
 */
public class ApiCourseDTO {
    private long courseId;
    private String catalogNumber;

    private String courseName;

    public ApiCourseDTO() {
    }

    public ApiCourseDTO(String catalogNumber, String courseName) {
        this.catalogNumber = catalogNumber;
        this.courseName = courseName;
    }

    public List<ApiCourseDTO> findCourseBasedOnDepartment() {
        List<ApiDepartmentDTO> departments = new DepartmentService().extractDepartmentsFromCSVFile();
        Set<String> addedCourses = new HashSet<>();

        for (ApiDepartmentDTO department : departments) {
            if (getCourseId() == department.getDeptId()) {
                setCourseName(department.getName());
            }
        }

        CSVFileReader file = new CSVFileReader();
        file.extractDataFromCSVFile();
        List<ApiCourseDTO> listOfCoursesWithoutDuplicates = new ArrayList<>();
        for (int i = 0; i < file.getCourseContainer().size(); i++) {
            ApiCourseDTO course = new ApiCourseDTO(file.getCourseContainer().get(i).getCatalogNumber().trim(), getCourseName());
            String uniqueKey = course.getCatalogNumber() + "-" + course.getCourseName();

            if (!addedCourses.contains(uniqueKey)) {
                listOfCoursesWithoutDuplicates.add(course);
                addedCourses.add(uniqueKey);
            }
        }
        listOfCoursesWithoutDuplicates.sort(new CatalogNumberComparator());
        return listOfCoursesWithoutDuplicates;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public static class CatalogNumberComparator implements Comparator<ApiCourseDTO> {
        @Override
        public int compare(ApiCourseDTO c1, ApiCourseDTO c2) {
            return c1.getCatalogNumber().compareTo(c2.getCatalogNumber());
        }
    }
}
