package com.dmcleish91.COVID19Tracker.services;

import com.dmcleish91.COVID19Tracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

@Service

public class Covid19DataService {

    private static String DATABASE_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private List<LocationStats> allStats = new ArrayList<>();
    private LocalDate currentDate;
    private String formattedDate;

    @PostConstruct
    @Scheduled(cron = "0 0 1 * * ?")
    public void fetchVirusData() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DATABASE_URL)).build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);

        currentDate = LocalDate.now();
        formattedDate = currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));


        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDifferenceFromPreviousDay(latestCases - previousDayCases);
            allStats.add(locationStat);

        }

        for (int x = 0; x < allStats.size(); x++) {
             if (allStats.get(x).getCountry().equals("US")) {
                 allStats.get(x).setCountry("United States");
             }
        }
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public String getFormattedDate() {
        return formattedDate;
    }
}
