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
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CourseController {
    @GetMapping("/api/dump-model")
    public String getCourseInformation(Model model) {
        CSVFileReader data = new CSVFileReader();
        data.extractDataFromCSVFile();
        model.addAttribute("courses", data.getCourseContainer());
        return "dump-model";
    }

    @PostMapping("/api/dump-model")
    public String getSearchResultsFromCSVFile(@RequestParam("SUBJECT") String subjectName,
                                              @RequestParam("CATALOGNUMBER") String catalogNumber,
                                              HttpServletResponse response, Model model) {
        CSVFileReader data = new CSVFileReader();
        data.extractDataFromCSVFileAndFilter(subjectName.toUpperCase(), catalogNumber);

        if (data.getCourseContainer().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "notFound";
        } else {
            model.addAttribute("subjectChosens", subjectName);
            model.addAttribute("catalogs", catalogNumber);
            model.addAttribute("courses", data.getCourseContainer());
            response.setStatus(HttpServletResponse.SC_OK);
            return "informationOnSelectedSubject";
        }
    }
}
