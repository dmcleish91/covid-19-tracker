package com.dmcleish91.COVID19Tracker.dao;

import com.dmcleish91.COVID19Tracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@Repository
public class Covid19DaoImpl implements Covid19Dao{

    private static final String DATABASE_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private final List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "0 0 1 * * ?")
    @Override
    public void getVirusData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DATABASE_URL)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);


        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDayCases = Integer.parseInt(record.get(record.size() - 2));

            // news reporting now uses 14 day average
//            int sum = 0;
//            for (int last14days = 14; last14days >= 0 ; last14days--) {
//                sum = Integer.parseInt(record.get((record.size() - 1) - last14days));
//            }
//            int average = sum/14;
//            locationStat.setAverage14days(average);

            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDifferenceFromPreviousDay(latestCases - previousDayCases);
            allStats.add(locationStat);

        }

        for (LocationStats allStat : allStats) {
            if (allStat.getCountry().equals("US")) {
                allStat.setCountry("United States");
            }
        }
    }

    @Override
    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @Override
    public List<LocationStats> getSearchResults(String theSearchName) {
        List<LocationStats> searchResults = new ArrayList<>();

        if (theSearchName != null) {
            for (LocationStats stats : allStats) {
                if (stats.getCountry().toLowerCase().contains(theSearchName.toLowerCase()) ||
                        stats.getState().toLowerCase().contains(theSearchName.toLowerCase())) {
                    searchResults.add(stats);
                }
            }
            return searchResults;
        } else {
            return getAllStats();
        }
    }

    @Override
    public String getTotalReportedCases() {
        int totalReportedCasesRaw = allStats.stream().mapToInt(LocationStats::getLatestTotalCases).sum();
        String totalReportedCases = String.format("%,d", totalReportedCasesRaw);

        return totalReportedCases;
    }

    @Override
    public String getTotalNewCases() {
        int totalNewCasesRaw = allStats.stream().mapToInt(LocationStats::getDifferenceFromPreviousDay).sum();
        String totalNewCases = String.format("%,d", totalNewCasesRaw);

        return totalNewCases;
    }
}
