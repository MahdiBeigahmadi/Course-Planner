package com.example.demo.models;
/* DepartmentService class
 * DepartmentService.java
 *
 * Class Description: The DepartmentService is intended to serve as a service layer in the application,
 * which is a common pattern in Spring Boot and other Java Enterprise applications.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April. 2024
 */

import com.example.demo.models.apiDots.ApiDepartmentDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class DepartmentService {
    private final AtomicLong nextDeptId = new AtomicLong(1);

    public List<ApiDepartmentDTO> getDepartments() {
        return departments;
    }

    private final List<ApiDepartmentDTO> departments = new ArrayList<>();
    public DepartmentService(){}
    public List<ApiDepartmentDTO> extractDepartmentsFromCSVFile() {
        CSVFileReader file = new CSVFileReader();
        List<Course> courses = file.getCourseContainer();

        Set<String> existingDepartmentNames = departments.stream()
                .map(ApiDepartmentDTO::getName)
                .collect(Collectors.toSet());

        for (Course course : courses) {
            String subjectName = course.getSubject();
            if (!existingDepartmentNames.contains(subjectName)) {
                departments.add(new ApiDepartmentDTO(nextDeptId.getAndIncrement(), subjectName));
                existingDepartmentNames.add(subjectName);
            }
        }
        return new ArrayList<>(departments);
    }
    //chatGPT suggested that I design this class as a helper class
}
