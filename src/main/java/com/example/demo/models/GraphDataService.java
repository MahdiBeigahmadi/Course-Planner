package com.example.demo.models;
/* GraphDataService class
 * GraphDataService.java
 *
 * Class Description: The GraphDataService is intended to serve as a service layer in the application,
 * which is a common pattern in Spring Boot and other Java Enterprise applications.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April 2024
 */
import com.example.demo.models.apiDots.ApiGraphDataPointDTO;
import com.example.demo.models.interfaces.IDepartmentIdConverter;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphDataService {


    public List<ApiGraphDataPointDTO> generateGraphData(String departmentId) {
        List<Course> courses = new CSVFileReader().getCourseContainer();

        List<Course> filteredCourses = courses.stream()
                .filter(course -> course.getSubject().equals(IDepartmentIdConverter.checkDepartmentID(Long.parseLong(departmentId))))
                .filter(course -> "LEC".equals(course.getComponentCode()))
                .filter(this::checkSemester)
                .toList();

        return filteredCourses.stream()
                .collect(Collectors.groupingBy(Course::getSemester, Collectors.summingInt(Course::getEnrolmentTotal)))
                .entrySet().stream()
                .map(entry -> new ApiGraphDataPointDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingLong(ApiGraphDataPointDTO::getSemesterCode))
                .collect(Collectors.toList());
    }

    private boolean checkSemester(Course course) {
        int semNumber = course.getSemester();
        int lastDigit = semNumber % 10;
        return lastDigit == 1 || lastDigit == 4 || lastDigit == 7;
    }
}