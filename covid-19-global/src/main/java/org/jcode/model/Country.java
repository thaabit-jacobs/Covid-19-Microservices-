package org.jcode.model;

import lombok.Data;

import java.util.Map;

@Data
public class Country {
    private String name;
    private long currentConfirmed;
    private long currentDeaths;
    private long currentRecovered;
    private Map<String, Map<String, Long>> time_series_data;

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
