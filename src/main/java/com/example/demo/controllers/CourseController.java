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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class CourseController implements IDepartmentIdConverter {
    private final DepartmentService departmentService;
    private final GraphDataService graphDataService;
    private final HashMap<Long, ArrayList<String>> eventsMap = new HashMap<>();
    private final LocalDateTime dateTime = LocalDateTime.now();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String formattedDateTime = dateTime.format(formatter);
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
        courseContainer = data.getCourseContainer();
        return courseContainer;
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
            if (Objects.equals(course.getSubject().trim(),
                    IDepartmentIdConverter.checkDepartmentID(departmentId)) && course.getId() == courseId) {
                // Don't show duplicate offerings
                if (!offerings.stream().anyMatch(o -> o.getCourseOfferingId() ==
                        (Long.valueOf(String.valueOf(departmentId) + courseId + course.getSemester())))) {
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
            throw new CourseNotFoundException("No courses found for department ID: "
                    + departmentId + " and course ID: " + courseId);
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
        List<Course> relevantCourses = courseContainer.stream().filter(c ->
                        c.getSubject().equals(IDepartmentIdConverter.checkDepartmentID(departmentId)) && c.getId() == courseId)
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addNewOffering(@RequestBody ApiOfferingDataDTO offeringDataDTO) {
        final ResponseEntity<String> BAD_REQUEST = handleInvalidSemesterCode(offeringDataDTO);
        if (BAD_REQUEST != null) return BAD_REQUEST;
        new CSVFileReader().addToCsvFile(offeringDataDTO);
        System.out.printf("A new offer has been added to the database.%n");
        ApiCourseOfferingDTO.SemesterData term =
                new ApiCourseOfferingDTO().getDataForSemesterCode(Long.parseLong(offeringDataDTO.getSemester()));
        // add event to every key
        for (long i = 1; i <= watcherDTOS.size(); i++) {
            if (eventsMap.get(i) == null) {
                ArrayList<String> list = new ArrayList<>();
                list.add(formattedDateTime + " added section " + offeringDataDTO.getComponent() +
                        " with enrollment (" + offeringDataDTO.getEnrollmentTotal() + "/" +
                        offeringDataDTO.getEnrollmentCap() + ") to offering " + term.term + " " + term.year);
                eventsMap.put(i, list);
            } else {
                eventsMap.get(i).add(formattedDateTime + " added section " + offeringDataDTO.getComponent() +
                        " with enrollment (" + offeringDataDTO.getEnrollmentTotal() + "/" +
                        offeringDataDTO.getEnrollmentCap() + ") to offering " + term.term + " " + term.year);
            }
        }
        System.out.println("A new offering added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(offeringDataDTO);
    }

    private ResponseEntity<String> handleInvalidSemesterCode(ApiOfferingDataDTO offeringDataDTO) {
        String semester = offeringDataDTO.getSemester();
        char semesterCode = semester.charAt(3);
        boolean isItOk = semesterCode == '1' || semesterCode == '4' || semesterCode == '7';
        if (!isItOk) {
            System.out.println("Invalid semester code: " + semesterCode);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid semester code");
        }
        return null;
    }

    @GetMapping("/watchers")
    public List<ApiWatcherDTO> getAllWatchers() {
        return watcherDTOS;
    }

    @PostMapping("/watchers")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createNewWatcher(@RequestBody ApiWatcherCreateDTO newWatch) {
        final ResponseEntity<String> BAD_REQUEST = getStringResponseEntity(newWatch);
        if (BAD_REQUEST != null) return BAD_REQUEST;
        final ApiWatcherDTO newWatchDTO = getApiWatcherDTO(newWatch);
        watcherDTOS.add(newWatchDTO);
        System.out.println("A new watch added to watch list successfully:\n" + newWatchDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private ApiWatcherDTO getApiWatcherDTO(ApiWatcherCreateDTO newWatch) {
        ApiWatcherDTO newWatchDTO = new ApiWatcherDTO();
        newWatchDTO.setId(watcherDTOS.size() + 1);
        newWatchDTO.setCourse(new ApiCourseDTO(newWatch.getCourseId().trim(),
                IDepartmentIdConverter.checkDepartmentID(Long.parseLong(newWatch.getDeptId()))));
        newWatchDTO.setDepartment(new ApiDepartmentDTO(Long.parseLong(newWatch.getDeptId()),
                IDepartmentIdConverter.checkDepartmentID(Long.parseLong(newWatch.getDeptId()))));
        // creates a new list of events
        ArrayList<String> list = new ArrayList<>();
        eventsMap.put(newWatchDTO.getId(), list);
        newWatchDTO.setEvents(eventsMap.get(newWatchDTO.getId()));
        return newWatchDTO;
    }

    private ResponseEntity<String> getStringResponseEntity(ApiWatcherCreateDTO newWatch) {
        DepartmentService service = new DepartmentService();
        service.extractDepartmentsFromCSVFile();
        long departmentSize = service.getDepartments().size();
        boolean failed = departmentSize < Long.parseLong(newWatch.getDeptId()) || newWatch.getCourseId().length() > 3;
        if (failed) {
            System.out.println("invalid department id or course id");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid department id or course id");
        }
        return null;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteWatcher(@PathVariable long id) {
        List<ApiWatcherDTO> filteredWatchers = watcherDTOS.stream()
                .filter(watcherDTO -> watcherDTO.getId() != id)
                .collect(Collectors.toList());
        System.out.println("watcher with id = " + id + " is deleted");
        if (filteredWatchers.size() < watcherDTOS.size()) {
            watcherDTOS = filteredWatchers;
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats/students-per-semester")
    public ResponseEntity<?> getDataForGraph(@RequestParam(required = false, value = "deptId") String deptId) {
        try {
            List<ApiGraphDataPointDTO> graphData = graphDataService.generateGraphData(deptId);
            System.out.println(graphData);
            return ResponseEntity.ok(graphData);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid department ID: " + deptId);
        }
    }
}


