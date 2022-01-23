package com.dmcleish91.COVID19Tracker.controllers;


import com.dmcleish91.COVID19Tracker.models.LocationStats;
import com.dmcleish91.COVID19Tracker.services.Covid19DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {


    Covid19DataService covid19DataService;

    @Autowired
    public void setCovid19DataService(Covid19DataService covid19DataService) {
        this.covid19DataService = covid19DataService;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("LocationStats",covid19DataService.getAllStats());
        model.addAttribute("totalReportedCases",covid19DataService.getTotalReportedCases());
        model.addAttribute("totalNewCases",covid19DataService.getTotalNewCases());
        model.addAttribute("formattedDate",covid19DataService.getFormattedDate());

        return "home";
    }

    @GetMapping("/search")
    public String searchCustomers(@RequestParam("theSearchName") String theSearchName, Model model) {

        // search allStats for requested data
        List<LocationStats> searchResults = covid19DataService.getSearchResults(theSearchName);

        model.addAttribute("LocationStats",searchResults);
        model.addAttribute("totalReportedCases",covid19DataService.getTotalReportedCases());
        model.addAttribute("totalNewCases",covid19DataService.getTotalNewCases());
        model.addAttribute("formattedDate",covid19DataService.getFormattedDate());

        return "home";

    }

}
