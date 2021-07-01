package com.example.demo.services;


import com.example.demo.exceptions.CountryNotFoundException;
import com.example.demo.model.Country;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class GlobalVaccinatedService {

    private static final Logger LOGGER= LoggerFactory.getLogger(GlobalVaccinatedService.class);

    private static final String vaccinatedURI = "https://raw.githubusercontent.com/govex/COVID-19/master/data_tables/vaccine_data/global_data/vaccine_data_global.csv";
    @Autowired
    private RestTemplate restTemplate;

    private List<Country> countryList = new ArrayList<>();

    public List<Country> getCountryList(){
        return new ArrayList<>(countryList);
    }

    public Country getCountry(String name) throws CountryNotFoundException {
        int index = getCountryIndex(name);

        if (index == -1){
            throw new CountryNotFoundException(name);
        }

        return countryList.get(index);
    }

    @PostConstruct
    private  void init() throws IOException {
        populateCountryListData();

        System.out.println(countryList);
    }

    private void populateCountryListData() throws IOException {
        Reader reader = new StringReader(fetchVaccinatedData());
        Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);

        for (CSVRecord record : records) {
            String countryName = record.get("Country_Region");
            String reportedDate = record.get("Report_Date_String");
            String state = record.get("Province_State");


            if (state.equals("")){
                String totalNumberOfVaccinated = record.get("People_fully_vaccinated");

                long fullVaccinated = totalNumberOfVaccinated.equals("") ? 0 : Long.valueOf(record.get("People_fully_vaccinated"));

                Country country = new Country();
                country.setCountryName(countryName);
                country.setTotalFullVaccinated(fullVaccinated);
                country.setDataReported(reportedDate);

                countryList.add(country);
            }
        }

        reader.close();
    }

    private String fetchVaccinatedData(){
        return restTemplate.getForObject(vaccinatedURI, String.class);
    }

    private boolean doesCountryExist(String countryName){
        for (Country country:countryList) {
            if (country.getCountryName().equals(countryName)){
                return true;
            }
        }
        return false;
    }

    private int getCountryIndex(String countryName){
        for (int i=0;i<countryList.size();i++) {
            if (countryList.get(i).getCountryName().equals(countryName)){
                return i;
            }
        }
        return -1;
    }
}
