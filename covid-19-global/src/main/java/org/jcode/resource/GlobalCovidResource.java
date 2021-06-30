package org.jcode.resource;

import lombok.SneakyThrows;
import org.jcode.assembler.CountryModelAssembler;
import org.jcode.exceptions.CountryNotFoundException;
import org.jcode.model.Country;
import org.jcode.services.GlobalCovidServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("global-covid")
public class GlobalCovidResource {

    @Autowired
    private CountryModelAssembler countryModelAssembler;

    @Autowired
    private GlobalCovidServiceImpl globalCovidService;

    @GetMapping
    public ResponseEntity<?> getAllCountries() {
        List<EntityModel<Country>> countries = globalCovidService.getAllCountries().getCountryList().stream() //
                .map(countryModelAssembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Country>> entityModel =  CollectionModel.of(countries,
                linkTo(methodOn(GlobalCovidResource.class).getAllCountries()).withSelfRel());

        return ResponseEntity.ok(entityModel);
    }

    @SneakyThrows
    @GetMapping("/{countryName}")
    public ResponseEntity<?> getCountry(@PathVariable("countryName") String name) {
        Optional<Country> country = globalCovidService.getCountry(name);

        EntityModel <Country> entityModel = countryModelAssembler.toModel(country.get());

        return ResponseEntity.ok(entityModel);
    }

}
