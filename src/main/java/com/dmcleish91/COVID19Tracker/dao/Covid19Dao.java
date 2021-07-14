package com.dmcleish91.COVID19Tracker.dao;

import com.dmcleish91.COVID19Tracker.models.LocationStats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface Covid19Dao {

    void getVirusData() throws IOException, InterruptedException;

    List<LocationStats> getAllStats();

    List<LocationStats> getSearchResults(String theSearchName);

    String getTotalReportedCases();

    String getTotalNewCases();
}
