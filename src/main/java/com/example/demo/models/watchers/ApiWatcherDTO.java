package com.example.demo.models.watchers;

import com.example.demo.models.apiDots.ApiCourseDTO;
import com.example.demo.models.apiDots.ApiDepartmentDTO;

import java.util.List;
/* ApiWatcherDTO class
 * ApiWatcherDTO.java
 *
 * Class Description: it puts a variety of events.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April 2024
 */
public class ApiWatcherDTO {
    private long id;
    private ApiDepartmentDTO department;
    private ApiCourseDTO course;
    private List<String> events;

    public ApiWatcherDTO(){}
    public ApiWatcherDTO(long id, ApiDepartmentDTO department, ApiCourseDTO course, List<String> events) {
        this.id = id;
        this.department = department;
        this.course = course;
        this.events = events;
    }

    public long getId() {
        return id;
    }
    public void incrementId() {
        this.id = this.id + 1;
    }
    public void setId(long id) {
        this.id = id;
    }

    public ApiDepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(ApiDepartmentDTO department) {
        this.department = department;
    }

    public ApiCourseDTO getCourse() {
        return course;
    }

    public void setCourse(ApiCourseDTO course) {
        this.course = course;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}