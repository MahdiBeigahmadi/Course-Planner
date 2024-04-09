package com.example.demo.models;
/* ApiOfferingSectionDTO class
 * ApiOfferingSectionDTO.java
 *
 * Class Description:It returns information about course with specific id number.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April 2024
 */

import java.util.ArrayList;
import java.util.List;

public class ApiOfferingSectionDTO {
    public String type;
    public int enrollmentCap;
    public int enrollmentTotal;
    private long courseOfferingId;
    private long courseId;
    private long departmentId;

    public List<ApiOfferingSectionDTO> getApiOfferingSectionDTO() {
        return apiOfferingSectionDTO;
    }

    private final List<ApiOfferingSectionDTO> apiOfferingSectionDTO = new ArrayList<>();
    public ApiOfferingSectionDTO() {
    }

    public ApiOfferingSectionDTO(String type, int enrollmentCap, int enrollmentTotal) {
        this.type = type;
        this.enrollmentCap = enrollmentCap;
        this.enrollmentTotal = enrollmentTotal;
    }

    public long getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(long courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public void setEnrollmentCap(int enrollmentCap) {
        this.enrollmentCap = enrollmentCap;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void setEnrollmentTotal(int enrollmentTotal) {
        this.enrollmentTotal = enrollmentTotal;
    }

    @Override
    public String toString() {
        return "ApiOfferingSectionDTO{" +
                "enrollmentCap=" + enrollmentCap +
                ", type='" + type + '\'' +
                ", enrollmentTotal=" + enrollmentTotal +
                '}';
    }

    public void getAdditionalDetailsOnOfferings() {
        CSVFileReader csvFileReader = new CSVFileReader();
        csvFileReader.extractDataFromCSVFile();
        List<Course> temp = new ArrayList<>(csvFileReader.getCourseContainer());

        for (Course course : temp) {
            boolean isValidSectionId = String.valueOf(getCourseOfferingId()).equals(course.getCatalogNumber()) &&
                    ICourseController.checkDepartmentID(getDepartmentId()).equals(course.getSubject())
                    && String.valueOf(getCourseId()).equals(course.getCatalogNumber());
            if (isValidSectionId) {
                apiOfferingSectionDTO.add(new ApiOfferingSectionDTO(course.getComponentCode(),
                        course.getEnrolementCapacity(), course.getEnrolmentTotal()));
                break;
            }
        }
        System.out.println(apiOfferingSectionDTO);
    }
}
