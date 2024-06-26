package com.example.demo.models.exceptionHandlers;

/* CourseNotFoundException class
 * CourseNotFoundException.java
 *
 * Class Description: It handles exceptions when a course is not found
 * Class Invariant: Must be an empty course list, or course is not presented in the list.
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April. 2024
 */
public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String message) {
        super(message);
    }
}
