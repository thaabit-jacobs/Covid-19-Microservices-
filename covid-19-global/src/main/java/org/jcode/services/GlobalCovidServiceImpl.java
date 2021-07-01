package org.jcode.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jcode.CountryListWrapper;
import org.jcode.exceptions.CountryNotFoundException;
import org.jcode.model.Country;
import org.jcode.model.Global;
import org.jcode.types.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GlobalCovidServiceImpl{
    private static final Logger LOGGER= LoggerFactory.getLogger(GlobalCovidServiceImpl.class);

    private static final LocalDate startingDate = LocalDate.of(2020, 1,22);

    private final String baseURI = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series";
    private final String CONFIRMED = "/time_series_covid19_confirmed_global.csv";
    private final String DEATHS = "/time_series_covid19_deaths_global.csv";
    private final String RECOVERED = "/time_series_covid19_recovered_global.csv";

    @Autowired
    private RestTemplate restTemplate;

    private List<Country> countryList = new ArrayList<>();

    @PostConstruct
    private void fetchVirusAllData() throws IOException {
        populateCountryListNames();
        populateCountryList(Types.CONFIRMED);
        populateCountryList(Types.RECOVERED);
        populateCountryList(Types.DEATHS);

        populateCurrent(Types.CONFIRMED);
        populateCurrent(Types.RECOVERED);
        populateCurrent(Types.DEATHS);
    }

    public Optional<Country> getCountry(String countryName) throws CountryNotFoundException {
        int index = getCountryIndex(countryName);

        if (index == -1){
            throw new CountryNotFoundException(countryName);
        }

        return Optional.of(countryList.get(index));
    }

    public CountryListWrapper getAllCountries(){
        CountryListWrapper countryListWrapper = new CountryListWrapper();
        countryListWrapper.setCountryList(countryList);
        return countryListWrapper;
    }

    private String fetchVirusData(String type){
        return restTemplate.getForObject(baseURI+type, String.class);
    }


    private void populateCountryListNames() throws IOException {
        for (CSVRecord record:getCSVRecords(Types.CONFIRMED)) {
            String name = record.get("Country/Region");

            if (doesCountryExist(name))
                continue;

            double latitude = Double.valueOf(record.get("Lat"));
            double longitude = Double.valueOf(record.get("Long"));

            Country newCountry = new Country();
            newCountry.setName(name);
            newCountry.setLatitude(latitude);
            newCountry.setLongitude(longitude);

            System.out.println(name);
            countryList.add(newCountry);
        }

        for (CSVRecord record:getCSVRecords(Types.RECOVERED)) {
            String name = record.get("Country/Region");

            if (doesCountryExist(name))
                continue;

            double latitude = Double.valueOf(record.get("Lat"));
            double longitude = Double.valueOf(record.get("Long"));

            Country newCountry = new Country();
            newCountry.setName(name);
            newCountry.setLatitude(latitude);
            newCountry.setLongitude(longitude);

            countryList.add(newCountry);
        }

        for (CSVRecord record:getCSVRecords(Types.DEATHS)) {
            String name = record.get("Country/Region");

            if (doesCountryExist(name))
                continue;

            double latitude = Double.valueOf(record.get("Lat"));
            double longitude = Double.valueOf(record.get("Long"));

            Country newCountry = new Country();
            newCountry.setName(name);
            newCountry.setLatitude(latitude);
            newCountry.setLongitude(longitude);

            countryList.add(newCountry);
        }
    }


    private Iterable<CSVRecord> getCSVRecords(Types type) throws IOException {
        Iterable<CSVRecord> records = null;
        Reader reader = null;

        switch (type){
            case CONFIRMED: String csvConfirmed = fetchVirusData(CONFIRMED);
                reader = new StringReader(csvConfirmed);
                records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
                break;
            case DEATHS: String csvDeaths = fetchVirusData(DEATHS);
                reader = new StringReader(csvDeaths);
                records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
                break;
            case RECOVERED: String csvRecovered = fetchVirusData(RECOVERED);
                reader = new StringReader(csvRecovered);
                records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
                break;
        }

      /*  reader.close();*/

        return records;
    }

    private void populateCountryList(Types type) throws IOException {
        for (CSVRecord record:getCSVRecords(type)) {
            String name = record.get("Country/Region");

            int index = getCountryIndex(name);

            String typeKey = type.toString().toLowerCase();

            Country countryAtIndex = countryList.get(index);

            Map<String, Long> map = countryAtIndex.getTime_series_data().get(typeKey);

            for (String date:getDateKeys()) {
                Long value = Long.valueOf(record.get(date));
                Long valueToBeUpdated = map.get(date);

                if (valueToBeUpdated != null){
                    map.put(date, valueToBeUpdated+value);
                }else{
                    map.put(date, value);
                }
            }
            countryAtIndex.getTime_series_data().put(typeKey, map);
            countryList.set(index, countryAtIndex);
        }
    }

    private void populateCurrent(Types type){
        for (Country country:countryList) {
            int  index = getCountryIndex(country.getName());
            String currentDate = formatLocalDate(LocalDate.now().minusDays(1));
            String typeKey = type.toString().toLowerCase();
            long currentValueForType = country.getTime_series_data().get(typeKey).get(currentDate);

            switch (type){
                case CONFIRMED:country.setCurrentConfirmed(currentValueForType);
                countryList.set(index, country);
                break;
                case RECOVERED:country.setCurrentRecovered(currentValueForType);
                    countryList.set(index, country);
                break;
                case DEATHS:country.setCurrentDeaths(currentValueForType);
                    countryList.set(index, country);
                break;
            }
        }
    }

    private boolean doesCountryExist(String countryName){
        for (Country country:countryList) {
                if (country.getName().equals(countryName)){
                    return true;
                }
        }
        return false;
    }

    private int getCountryIndex(String countryName){
        for (int i=0;i<countryList.size();i++) {
            if (countryList.get(i).getName().equals(countryName)){
                return i;
            }
        }
        return -1;
    }

    public static List<String> getDateKeys(){
        LocalDate currentDate = LocalDate.now();

        List<String> dateKeys = new LinkedList<>();

        LocalDate key = startingDate;

        while(!currentDate.equals(key)){
            dateKeys.add(formatLocalDate(key));
            key = key.plusDays(1);
        }

        return dateKeys;
    }

    public static String formatLocalDate(LocalDate day){
        String formattedDate = day.format(DateTimeFormatter.ofPattern("M/d/yy"));

        return formattedDate;
    }
}
