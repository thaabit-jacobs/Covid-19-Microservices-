package com.example.demo.resource;

import com.example.demo.assemblers.CountryModelAssemblers;
import com.example.demo.exceptions.CountryNotFoundException;
import com.example.demo.model.Country;
import com.example.demo.services.GlobalVaccinatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("global-vaccinated")
public class GlobalVaccinatedResource {

    @Autowired
    GlobalVaccinatedService globalVaccinatedService;

    @Autowired
    CountryModelAssemblers countryModelAssemblers;

    @GetMapping
    public ResponseEntity<?> getAllCountries(){
        List<EntityModel<Country>> countries = globalVaccinatedService.getCountryList().stream()
        .map(countryModelAssemblers::toModel)
        .collect(Collectors.toList());

        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{countryName}")
    public ResponseEntity<?> getCountry(@PathVariable("countryName") String countryName) throws CountryNotFoundException {
        Country country = globalVaccinatedService.getCountry(countryName);

        EntityModel<Country> entityModel = countryModelAssemblers.toModel(country);

        return ResponseEntity.ok(entityModel);
    }


}
