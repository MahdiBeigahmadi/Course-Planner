package com.example.demo.models;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/* ApiDepartmentDTO class
 * ApiDepartmentDTO.java
 *
 * Class Description: It transfers data for department to the server.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April. 2024
 */
public class ApiDepartmentDTO {
    private final long deptId;
    private final String name;

    public ApiDepartmentDTO(long deptId, String name) {
        this.deptId = deptId;
        this.name = name;
    }

    public long getDeptId() {
        return deptId;
    }

    public String getName() {
        return name;
    }

    public List<ApiCourseDTO> findCourseBasedOnDepartment() {
        Set<String> addedCourses = new HashSet<>();
        CSVFileReader file = new CSVFileReader();
        file.extractDataFromCSVFile();
        List<ApiCourseDTO> listOfCoursesWithoutDuplicates = new ArrayList<>();
        AtomicLong nextCourseId = new AtomicLong(1000);

        for (Course currentCourse: file.getCourseContainer()) {
            String uniqueKey = currentCourse.getCatalogNumber() + "-" + name;

            if(currentCourse.getSubject().equals(name) && !addedCourses.contains(uniqueKey)){
                ApiCourseDTO course = new ApiCourseDTO(currentCourse.getCatalogNumber().trim(), name, nextCourseId.getAndIncrement());
                listOfCoursesWithoutDuplicates.add(course);
                addedCourses.add(uniqueKey);
            }

        }
        listOfCoursesWithoutDuplicates.sort(new ApiDepartmentDTO.CatalogNumberComparator());
        return listOfCoursesWithoutDuplicates;
    }

    public static class CatalogNumberComparator implements Comparator<ApiCourseDTO> {
        @Override
        public int compare(ApiCourseDTO c1, ApiCourseDTO c2) {
            return c1.getCatalogNumber().compareTo(c2.getCatalogNumber());
        }
    }
}
