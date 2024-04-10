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
import com.example.demo.models.interfaces.IDepartmentId;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api")
public class CourseController implements IDepartmentId {
    private final DepartmentService departmentService;
    private final HttpServletResponse httpServletResponse;
    private List<ApiDepartmentDTO> departments;
    private List<Course> courseContainer;

    @Autowired
    public CourseController(DepartmentService departmentService, HttpServletResponse httpServletResponse) {
        this.departmentService = departmentService;
        this.httpServletResponse = httpServletResponse;
    }

    @GetMapping("/about")
    public ApiAboutDTO getInformationAboutAuthors() {
        return new ApiAboutDTO("Course Planner", "Mahdi & Danieva");
    }

    @GetMapping("/dump-model")
    public List<Course> loadCSVFileOnServer() {
        CSVFileReader data = new CSVFileReader();
        return data.getCourseContainer();
    }

    @GetMapping("/departments")
    public ResponseEntity<List<ApiDepartmentDTO>> getDepartments() {
        departments = departmentService.extractDepartmentsFromCSVFile();
        CSVFileReader file = new CSVFileReader();
        courseContainer = file.getCourseContainer();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/departments/{id}/courses")
    public List<ApiCourseDTO> getAllCoursesBasedOnSelectedDepartment(@PathVariable("id") long dept) {
        return departments.get((int) dept - 1).findCourseBasedOnDepartment();
    }

    @GetMapping("/departments/{departmentID}/courses/{courseID}/offerings")
    public List<ApiCourseOfferingDTO> showAllOfferingsBasedOnSelectedDepartments(@PathVariable("departmentID") long departmentId,
                                                                                 @PathVariable("courseID") long courseId) {
        if (departmentId > departments.size()) {
            throw new InvalidDepartmentException("Invalid department ID: " + departmentId);
        }
        // create a list of offerings
        // search for course by courseID
        List<ApiCourseOfferingDTO> offerings = new ArrayList<>();
        for (Course course : courseContainer) {
            if (Objects.equals(course.getSubject().trim(), IDepartmentId.checkDepartmentID(departmentId)) &&
                    Objects.equals(course.getCatalogNumber().trim(), String.valueOf(courseId))) {
                ApiCourseOfferingDTO newOffering = new ApiCourseOfferingDTO();
                ApiCourseOfferingDTO.SemesterData semesterData = newOffering.getDataForSemesterCode(course.getSemester());
                newOffering.setCourseOfferingId(Long.valueOf(String.valueOf(departmentId) + courseId));
                newOffering.setSemesterCode(course.getSemester());
                newOffering.setTerm(semesterData.term);
                newOffering.setYear(semesterData.year);
                newOffering.setInstructors(course.getInstructors());
                newOffering.setLocation(course.getLocation());
                offerings.add(newOffering);
            }
        }

        if (offerings.isEmpty()) {
            throw new CourseNotFoundException("No courses found for department ID: " + departmentId + " and course ID: " + courseId);
        }
        return offerings;
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

    @GetMapping("/departments/{departmentId}/courses/{courseId}/offerings/{courseOfferingId}")
    public List<ApiOfferingSectionDTO> getDetailsOfOfferingSection(@PathVariable("departmentId") long departmentId,
                                                                   @PathVariable("courseId") String courseId,
                                                                   @PathVariable("courseOfferingId") long offeringId) {

        ApiOfferingSectionDTO offeringSection = new ApiOfferingSectionDTO();
        offeringSection.setCourseId(courseId);
        offeringSection.setDepartmentId(departmentId);
        offeringSection.setCourseOfferingId(offeringId);
        System.out.println(ApiDepartmentDTO.offering);
        return ApiDepartmentDTO.offering;
    }

    @PostMapping("/addoffering")
    public ResponseEntity<?> addNewOffering(@RequestBody ApiOfferingDataDTO offeringDataDTO) {
        List<String> events = new ArrayList<>();
        //adds list of events to the ApiWatcher
        events.add(offeringDataDTO.toString() + " was added at " + System.currentTimeMillis());
        //gets the data from front and stores in a new line of csv file
        final ApiOfferingDataDTO aNewOffering = getaNewOffering(offeringDataDTO);
        CSVFileReader file = new CSVFileReader();
        // extracts the data from csv file first
        addToWatcher(offeringDataDTO, file, aNewOffering, events); // it adds the event to the watcher
        return ResponseEntity.status(HttpStatus.CREATED).body(offeringDataDTO);
    }

    private void addToWatcher(ApiOfferingDataDTO offeringDataDTO, CSVFileReader file,
                              ApiOfferingDataDTO aNewOffering, List<String> events) {
        //adds an object to apiWatcherDTO class
        file.addToCsvFile(aNewOffering);
        ApiWatcherDTO apiWatcherDTO = new ApiWatcherDTO();
        apiWatcherDTO.incrementId();
        apiWatcherDTO.setEvents(events);
        apiWatcherDTO.setDepartment(new ApiDepartmentDTO(IDepartmentId.
                convertDepartmentStringToId(offeringDataDTO.getSubjectName()), offeringDataDTO.getSubjectName()));
        apiWatcherDTO.setCourse(new ApiCourseDTO(offeringDataDTO.getCatalogNumber(), offeringDataDTO.getSubjectName()));
    }

    private ApiOfferingDataDTO getaNewOffering(ApiOfferingDataDTO offeringDataDTO) {
        //data from front-end
        ApiOfferingDataDTO aNewOffering = new ApiOfferingDataDTO();
        aNewOffering.setSemester(offeringDataDTO.getSemester());
        aNewOffering.setSubjectName(offeringDataDTO.getSubjectName());
        aNewOffering.setCatalogNumber(offeringDataDTO.getCatalogNumber());
        aNewOffering.setLocation(offeringDataDTO.getLocation());
        aNewOffering.setEnrollmentCap(offeringDataDTO.getEnrollmentCap());
        aNewOffering.setComponent(offeringDataDTO.getComponent());
        aNewOffering.setInstructor(offeringDataDTO.getInstructor());
        aNewOffering.setEnrollmentTotal(offeringDataDTO.getEnrollmentTotal());
        return aNewOffering;
    }
}

