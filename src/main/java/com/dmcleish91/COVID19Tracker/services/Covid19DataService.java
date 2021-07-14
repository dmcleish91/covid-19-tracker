package com.dmcleish91.COVID19Tracker.services;

import com.dmcleish91.COVID19Tracker.dao.Covid19Dao;
import com.dmcleish91.COVID19Tracker.models.LocationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Service
public class Covid19DataService {

    private String formattedDate;

    @Autowired
    Covid19Dao covid19Dao;

    public void getVirusData() throws IOException, InterruptedException {
        covid19Dao.getVirusData();
    }

    public List<LocationStats> getAllStats() {
        return covid19Dao.getAllStats();
    }

    public List<LocationStats> getSearchResults(String theSearchName) {
        return covid19Dao.getSearchResults(theSearchName);
    }

    public String getFormattedDate() {
        LocalDate currentDate = LocalDate.now();
        formattedDate = currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));

        return formattedDate;
    }

    public String getTotalReportedCases() {
        return covid19Dao.getTotalReportedCases();
    }

    public String getTotalNewCases() {
        return covid19Dao.getTotalNewCases();
    }
}
