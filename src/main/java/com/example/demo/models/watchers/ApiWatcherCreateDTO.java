package com.example.demo.models.watchers;

/* ApiWatcherCreateDTO class
 * ApiWatcherCreateDTO.java
 *
 * Class Description: It creates a watcher for adding, new course, department etc.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April 2024
 */
public final class ApiWatcherCreateDTO {
    private String deptId;
    private String courseId;

    public ApiWatcherCreateDTO(){}
    public ApiWatcherCreateDTO(String deptId, String courseId) {
        this.deptId = deptId;
        this.courseId = courseId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "ApiWatcherCreateDTO{" +
                "deptId='" + deptId + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }
}