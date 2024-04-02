package com.example.demo.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private final List<ApiCourseDTO> listOfCourses = new ArrayList<>();
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

        for (ApiDepartmentDTO department : departments) {
            if (getCourseId() == department.getDeptId()) {
                setCourseName(department.getName());
            }
        }
        CSVFileReader file = new CSVFileReader();
        file.extractDataFromCSVFile();
        for (int i = 0; i < file.getCourseContainer().size(); i++) {
            if (file.getCourseContainer().get(i).getSubject().equals(getCourseName())) {
                listOfCourses.add(new ApiCourseDTO(file.getCourseContainer().get(i).getCatalogNumber().trim(), getCourseName()));
            }
        }
        listOfCourses.sort(new CatalogNumberComparator());
        return listOfCourses;
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
