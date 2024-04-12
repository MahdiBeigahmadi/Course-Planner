package com.example.demo.models.apiDots;

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

    public ApiCourseDTO(String catalogNumber, String courseName, long courseId){
        this.catalogNumber = catalogNumber;
        this.courseName = courseName;
        this.courseId = courseId;
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

    @Override
    public String toString() {
        return "ApiCourseDTO{" +
                "courseId=" + courseId +
                ", catalogNumber='" + catalogNumber + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
