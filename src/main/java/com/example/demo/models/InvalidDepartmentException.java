package com.example.demo.models;
/* InvalidDepartmentException class
 * InvalidDepartmentException.java
 *
 * Class Description: It handles when the user enters an invalid department ID.
 * Class Invariant: The department ID must be greater than 14.
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April. 2024
 */
public class InvalidDepartmentException extends RuntimeException {
    public InvalidDepartmentException(String message) {
        super(message);
    }
}