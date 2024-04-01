package com.example.demo.controllers;

/* CourseController class
 * CourseController.java
 *
 * Class Description: It is a controller class that has access to the models.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April. 2024
 */

import com.example.demo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CourseController {
    private final DepartmentService departmentService;

    @Autowired
    public CourseController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/api/about")
    public ApiAboutDTO getInformationAboutAuthors() {
        return new ApiAboutDTO("Course Planner", "Mahdi & Danieva");
    }

    @GetMapping("/api/dump-model")
    public List<Course> loadCSVFileOnServer() {
        CSVFileReader data = new CSVFileReader();
        data.extractDataFromCSVFile();
        return data.getCourseContainer();
    }

    @GetMapping("/api/departments")
    public ResponseEntity<List<ApiDepartmentDTO>> getDepartments() {
        List<ApiDepartmentDTO> departments = departmentService.extractDepartmentsFromCSVFile();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/api/departments/{id}/courses")
    public List<String> getAllCoursesBasedOnSelectedDepartment(@PathVariable("id") long dept) {
        ApiCourseDTO course = new ApiCourseDTO();
        course.setCourseId(dept);
        return course.findCourseBasedOnDepartment();
    }

    @GetMapping("/api/departments/{departmentID}/courses/{courseID}/offerings")
    public List<ApiCourseOfferingDTO> showAllOfferingsBasedOnSelectedDepartments(@PathVariable("departmentID") long departmentId,
                                                                                 @PathVariable("courseID") long courseId) {

        ApiCourseOfferingDTO offer = new ApiCourseOfferingDTO();
        offer.setCourseOfferingId(courseId);
        offer.setDepartmentId(departmentId);
        return offer.extractInformationBasedOnCourseIdAndDepartmentId();
    }
}
