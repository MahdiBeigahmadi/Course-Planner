package com.example.demo.models;

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
}
