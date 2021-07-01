package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Country {
    private String countryName;
    private long totalFullVaccinated;
    private String dataReported;

    @Override
    public int hashCode(){
        return countryName.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Country))
            return false;

        Country country = (Country) o;

        return this.countryName.equals(country.countryName);
    }
}
