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

import com.example.demo.models.CSVFileReader;
import com.example.demo.models.Course;
import com.example.demo.models.DepartmentService;
import com.example.demo.models.GraphDataService;
import com.example.demo.models.apiDots.*;
import com.example.demo.models.exceptionHandlers.CourseNotFoundException;
import com.example.demo.models.exceptionHandlers.InvalidDepartmentException;
import com.example.demo.models.interfaces.IDepartmentIdConverter;
import com.example.demo.models.watchers.ApiWatcherCreateDTO;
import com.example.demo.models.watchers.ApiWatcherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CourseController implements IDepartmentIdConverter {
    private final long unixTimestamp = 1712862293499L;
    private final DepartmentService departmentService;
    private final GraphDataService graphDataService;
    private final List<ApiWatcherCreateDTO> watcherCreateDTOS = new ArrayList<>();
    LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(unixTimestamp), ZoneId.systemDefault());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = dateTime.format(formatter);
    private List<ApiDepartmentDTO> departments = new ArrayList<>();
    private List<Course> courseContainer = new ArrayList<>();
    private List<ApiWatcherDTO> watcherDTOS = new ArrayList<>();

    @Autowired
    public CourseController(DepartmentService departmentService, GraphDataService graphDataService) {
        this.departmentService = departmentService;
        this.graphDataService = graphDataService;
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
            if (Objects.equals(course.getSubject().trim(), IDepartmentIdConverter.checkDepartmentID(departmentId)) && course.getId() == courseId) {
                // Don't show duplicate offerings
                if (!offerings.stream().anyMatch(o -> o.getCourseOfferingId() == (Long.valueOf(String.valueOf(departmentId) + courseId + course.getSemester())))) {
                    ApiCourseOfferingDTO newOffering = new ApiCourseOfferingDTO();
                    ApiCourseOfferingDTO.SemesterData semesterData = newOffering.getDataForSemesterCode(course.getSemester());
                    newOffering.setCourseOfferingId(Long.valueOf(String.valueOf(departmentId) + courseId + course.getSemester()));
                    newOffering.setSemesterCode(course.getSemester());
                    newOffering.setTerm(semesterData.term);
                    newOffering.setYear(semesterData.year);
                    newOffering.setInstructors(course.getInstructors());
                    newOffering.setLocation(course.getLocation());
                    offerings.add(newOffering);
                }

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


    @GetMapping("/departments/{departmentID}/courses/{courseID}/offerings/{courseOfferingId}")
    public List<ApiOfferingSectionDTO> getDetailsOfOfferingSection(@PathVariable("departmentID") long departmentId,
                                                                   @PathVariable("courseID") long courseId,
                                                                   @PathVariable("courseOfferingId") long offeringId) {

        List<ApiOfferingSectionDTO> offerings = new ArrayList<>();
        List<Course> relevantCourses = courseContainer.stream().filter(c -> c.getSubject().equals(IDepartmentIdConverter.checkDepartmentID(departmentId)) && c.getId() == courseId)
                .map(c -> new Course(
                        c.getSemester(),
                        c.getSubject(),
                        c.getCatalogNumber(),
                        c.getLocation(),
                        c.getEnrolementCapacity(),
                        c.getEnrolmentTotal(),
                        c.getInstructors(),
                        c.getComponentCode()
                ))
                .collect(Collectors.toList());
        showOfferingDetails(departmentId, courseId, offeringId, relevantCourses, offerings);
        if (offerings.isEmpty()) {
            throw new CourseNotFoundException("No courses found for department ID: " + departmentId + " and course ID: " + courseId);
        }
        return offerings;
    }

    private void showOfferingDetails(long departmentId, long courseId, long offeringId,
                                     List<Course> relevantCourses, List<ApiOfferingSectionDTO> offerings) {
        for (Course course : relevantCourses) {
            if (Objects.equals(course.getSubject().trim(),
                    IDepartmentIdConverter.checkDepartmentID(departmentId)) &&
                    course.getId() == courseId &&
                    Long.valueOf(String.valueOf(departmentId) + courseId + course.getSemester()) == offeringId) {
                boolean added = false;
                for (ApiOfferingSectionDTO offer : offerings) {
                    if (offer.getType().equals(course.getComponentCode())) {
                        added = true;
                        int oldTotal = offer.getEnrollmentTotal();
                        offer.setEnrollmentTotal(oldTotal + course.getEnrolmentTotal());
                    }
                }
                if (!added) {
                    ApiOfferingSectionDTO newOffering = new ApiOfferingSectionDTO(course.getComponentCode(),
                            course.getEnrolementCapacity(), course.getEnrolmentTotal());
                    offerings.add(newOffering);
                }

            }
        }
    }

    @PostMapping("/addoffering")
    public ResponseEntity<?> addNewOffering(@RequestBody ApiOfferingDataDTO offeringDataDTO) {
        List<String> events = new ArrayList<>();
        //adds list of events to the ApiWatcher
        events.add(offeringDataDTO.toString() + " was added at " + formattedDateTime);
        //gets the data from front and stores in a new line of csv file
        final ApiOfferingDataDTO aNewOffering = getaNewOffering(offeringDataDTO);
        CSVFileReader file = new CSVFileReader();
        // extracts the data from csv file first
        watcherCreateDTOS.add(new ApiWatcherCreateDTO(offeringDataDTO.getCatalogNumber(), offeringDataDTO.getSubjectName()));
        addToWatcher(offeringDataDTO, file, aNewOffering, events); // it adds the event to the watcher
        System.out.println("A new offering added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(offeringDataDTO);
    }

    private void addToWatcher(ApiOfferingDataDTO offeringDataDTO, CSVFileReader file,
                              ApiOfferingDataDTO aNewOffering, List<String> events) {
        //adds an object to apiWatcherDTO class
        file.addToCsvFile(aNewOffering);
        ApiWatcherDTO apiWatcherDTO = new ApiWatcherDTO();
        apiWatcherDTO.incrementId();
        apiWatcherDTO.setEvents(events);
        apiWatcherDTO.setDepartment(new ApiDepartmentDTO(IDepartmentIdConverter.
                convertDepartmentStringToId(offeringDataDTO.getSubjectName()), offeringDataDTO.getSubjectName()));
        apiWatcherDTO.setCourse(new ApiCourseDTO(offeringDataDTO.getCatalogNumber(), offeringDataDTO.getSubjectName()));
        watcherDTOS.add(apiWatcherDTO);
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

    @GetMapping("/watchers")
    public List<ApiWatcherDTO> getAllWatchers() {
        return watcherDTOS;
    }

    @PostMapping("/watchers")
    public ResponseEntity<?> createNewWatcher(@RequestBody ApiWatcherCreateDTO newWatch) {
        List<String> events = new ArrayList<>();
        events.add(newWatch.toString() + " was added at " + formattedDateTime);
        watcherCreateDTOS.add(newWatch);
        System.out.println("watcher" + newWatch + " created successfully");

        ApiWatcherDTO newWatchDTO = new ApiWatcherDTO();
        newWatchDTO.incrementId();
        newWatchDTO.setCourse(new ApiCourseDTO(newWatch.getCourseId(),
                IDepartmentIdConverter.checkDepartmentID(Long.parseLong(newWatch.getDeptId()))));
        newWatchDTO.setDepartment(new ApiDepartmentDTO(Long.parseLong(newWatch.getDeptId()),
                IDepartmentIdConverter.checkDepartmentID(Long.parseLong(newWatch.getDeptId()))));
        newWatchDTO.setEvents(events);
        watcherDTOS.add(newWatchDTO);
        System.out.println("A new watch added to watch list successfully:\n" + newWatchDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/watchers/{id}")
    public ResponseEntity<List<ApiWatcherDTO>> returnListOfWatchesAssociatedWithId(@PathVariable long id) {
        List<ApiWatcherDTO> tempWatchers = watcherDTOS.stream()
                .filter(watcherDTO -> watcherDTO.getId() == id)
                .collect(Collectors.toList());

        if (tempWatchers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println(tempWatchers);
        return ResponseEntity.ok(tempWatchers);
    }

    @DeleteMapping("/watchers/{id}")
    public ResponseEntity<?> deleteWatcher(@PathVariable long id) {
        List<ApiWatcherDTO> filteredWatchers = watcherDTOS.stream()
                .filter(watcherDTO -> watcherDTO.getId() != id)
                .collect(Collectors.toList());

        if (filteredWatchers.size() < watcherDTOS.size()) {
            watcherDTOS = filteredWatchers;
            return ResponseEntity.ok().build();
        }
        System.out.println("watcher with id " + id + " is deleted");
        new CSVFileReader().deleteFromCsvFile();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats/students-per-semester")
    public ResponseEntity<?> getDataForGraph(@RequestParam(required = false, value = "depId") String deptId) {
        try {
            long departmentId = 0;
            if (deptId != null && !deptId.trim().isEmpty()) {
                departmentId = Long.parseLong(deptId);
            }
            List<ApiGraphDataPointDTO> graphData = graphDataService.generateGraphData(departmentId);
            System.out.println(graphData);
            return ResponseEntity.ok(graphData);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid department ID: " + deptId);
        }
    }
}


