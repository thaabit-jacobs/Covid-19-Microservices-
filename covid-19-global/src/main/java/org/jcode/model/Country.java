package org.jcode.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Country {
    private String name;
    private double latitude;
    private double longitude;
    private long currentConfirmed;
    private long currentDeaths;
    private long currentRecovered;
    private Map<String, Map<String, Long>> time_series_data;

    public Country(){
        this.time_series_data = new HashMap<>();
        this.time_series_data.put("confirmed", new HashMap<>());
        this.time_series_data.put("deaths", new HashMap<>());
        this.time_series_data.put("recovered", new HashMap<>());
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Country))
            return false;

        Country country = (Country) o;

        return this.name.equals(country.name);
    }
}
