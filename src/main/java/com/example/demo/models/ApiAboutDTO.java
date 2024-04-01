package com.example.demo.models;

/* ApiAboutDTO class
 * ApiAboutDTO.java
 *
 * Class Description: It transfers data for about.html page.
 * Class Invariant:
 *
 * Author(s): Mahdi Beigahmadi, Danieva Paraiso
 * Student ID(s): 301570853,
 * Last modified: April. 2024
 */

public class ApiAboutDTO {
    public String appName;
    public String authorName;

    public ApiAboutDTO(String appName, String authorName) {
        this.appName = appName;
        this.authorName = authorName;
    }
}
