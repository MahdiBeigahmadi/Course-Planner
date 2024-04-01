package com.example.demo.models;

import java.util.ArrayList;
import java.util.Collections;
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
    private long courseId;
    private final List<String> catalogNumber = new ArrayList<>();

    private String courseName;

    public ApiCourseDTO(){
    }

    public List<String> findCourseBasedOnDepartment() {
        List<ApiDepartmentDTO> departments = new DepartmentService().extractDepartmentsFromCSVFile();

        for (ApiDepartmentDTO department : departments) {
            if (getCourseId() == department.getDeptId()) {
                setCourseName(department.getName());
            }
        }
        CSVFileReader file = new CSVFileReader();
        file.extractDataFromCSVFile();
        for (int i = 0; i< file.getCourseContainer().size(); i++) {
            if(file.getCourseContainer().get(i).getSubject().equals(getCourseName())) {
                catalogNumber.add(file.getCourseContainer().get(i).getCatalogNumber().trim());
            }
        }
        Collections.sort(catalogNumber);
        return catalogNumber;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }
}
