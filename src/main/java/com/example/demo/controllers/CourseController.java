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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {
    private final DepartmentService departmentService;
    private List<ApiDepartmentDTO> departments;

    @Autowired
    public CourseController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/about")
    public ApiAboutDTO getInformationAboutAuthors() {
        return new ApiAboutDTO("Course Planner", "Mahdi & Danieva");
    }

    @GetMapping("/dump-model")
    public List<Course> loadCSVFileOnServer() {
        CSVFileReader data = new CSVFileReader();
        data.extractDataFromCSVFile();
        return data.getCourseContainer();
    }

    @GetMapping("/departments")
    public ResponseEntity<List<ApiDepartmentDTO>> getDepartments() {
        departments = departmentService.extractDepartmentsFromCSVFile();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/departments/{id}/courses")
    public List<ApiCourseDTO> getAllCoursesBasedOnSelectedDepartment(@PathVariable("id") long dept) {
        return departments.get((int) dept - 1).findCourseBasedOnDepartment();
    }

    @GetMapping("/departments/{departmentID}/courses/{courseID}/offerings")
    public List<ApiCourseOfferingDTO> showAllOfferingsBasedOnSelectedDepartments(@PathVariable("departmentID") long departmentId,
                                                                                 @PathVariable("courseID") long courseId) {
        ApiCourseOfferingDTO offerings = new ApiCourseOfferingDTO();
        offerings.setDepartmentId(departmentId);
        offerings.setCourseOfferingId(courseId);
        DepartmentService serviceSize = new DepartmentService();
        long size = serviceSize.extractDepartmentsFromCSVFile().size();
        offerings.extractInformationBasedOnCourseIdAndDepartmentId();
        if (departmentId > size) {
            throw new InvalidDepartmentException("Invalid department ID: " + departmentId);
        }
        if (offerings.getFilteredCourses().isEmpty()) {
            throw new CourseNotFoundException("No courses found for department ID: " + departmentId + " and course ID: " + courseId);
        }
        return offerings.getFilteredCourses();
    }

    @ExceptionHandler(CourseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCourseNotFound(CourseNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidDepartmentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidDepartment(InvalidDepartmentException ex) {
        return ex.getMessage();
    }


    //example: Brian Fraser, CMPT 213, courseOfferingId: 345 -> /api/departments/5/courses/213/offerings/345
    // enroll cap: 45, enroll total: 42
    /*{
        "id": 345,
            "semester": 1131,
            "subject": "CMPT",
            "catalogNumber": "213",
            "location": "SURREY",
            "enrolementCapacity": 45,
            "enrolmentTotal": 42,
            "instructors": "Brian Fraser",
            "componentCode": "LEC"
    }*/

    @GetMapping("/departments/{departmentID}/courses/{courseID}/offerings/{courseOfferingId}")
    public List<ApiOfferingSectionDTO> getDetailsOfOfferingSection(@PathVariable("departmentID") long departmentId,
                                                                   @PathVariable("courseID") long courseId,
                                                                   @PathVariable("courseOfferingId") long offeringId) {

        ApiOfferingSectionDTO offeringSection = new ApiOfferingSectionDTO();
        offeringSection.setCourseId(courseId);
        offeringSection.setDepartmentId(departmentId);
        offeringSection.setCourseOfferingId(offeringId);
        offeringSection.getAdditionalDetailsOnOfferings();
        return offeringSection.getApiOfferingSectionDTO();
    }
}

