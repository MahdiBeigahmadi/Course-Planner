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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseController {
    @GetMapping("/index")
    public String getCourseInformation(Model model) {
        CSVFileReader data = new CSVFileReader();
        data.extractDataFromCSVFile();
        model.addAttribute("courses", data.getCourseContainer());
        return "index";
    }
}
