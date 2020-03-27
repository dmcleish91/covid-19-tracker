package com.dmcleish91.COVID19Tracker.controllers;


import com.dmcleish91.COVID19Tracker.models.LocationStats;
import com.dmcleish91.COVID19Tracker.services.Covid19DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    Covid19DataService covid19DataService;

    @GetMapping("/")
    public String home(Model model) {
        List<LocationStats> allStats = covid19DataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDifferenceFromPreviousDay()).sum();
        model.addAttribute("LocationStats",covid19DataService.getAllStats());
        model.addAttribute("totalReportedCases",totalReportedCases);
        model.addAttribute("totalNewCases",totalNewCases);

        return "home";
    }

}
