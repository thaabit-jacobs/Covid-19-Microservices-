package org.jcode.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jcode.model.Country;

import java.util.List;

@Data
@NoArgsConstructor
public class CountryListWrapper {
    private List<Country> countryList;
}
