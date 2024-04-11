package com.example.demo.models.apiDots;

import com.example.demo.models.Course;

/* ApiGraphDataPointDTO class
 * ApiGraphDataPointDTO.java
 *
 * Class Description: It transfers data for drawing the graph.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April 2024
 */
public class ApiGraphDataPointDTO {
    private long semesterCode;
    private long totalCoursesTaken;

    public ApiGraphDataPointDTO(int semesterCode, long totalCoursesTaken) {
        this.semesterCode = semesterCode;
        this.totalCoursesTaken = totalCoursesTaken;
    }

    public long getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(long semesterCode) {
        this.semesterCode = semesterCode;
    }

    public long getTotalCoursesTaken() {
        return totalCoursesTaken;
    }

    public void setTotalCoursesTaken(long totalCoursesTaken) {
        this.totalCoursesTaken = totalCoursesTaken;
    }

    @Override
    public String toString() {
        return "ApiGraphDataPointDTO{" +
                "semesterCode=" + semesterCode +
                ", totalCoursesTaken=" + totalCoursesTaken +
                '}';
    }
}
