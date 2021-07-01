package com.example.demo.assemblers;

import com.example.demo.model.Country;
import com.example.demo.resource.GlobalVaccinatedResource;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CountryModelAssemblers implements RepresentationModelAssembler<Country, EntityModel<Country>> {


    @SneakyThrows
    @Override
    public EntityModel<Country> toModel(Country country) {
        return EntityModel.of(country,
                linkTo(methodOn(GlobalVaccinatedResource.class).getCountry(country.getCountryName())).withSelfRel(),
                linkTo(methodOn(GlobalVaccinatedResource.class).getAllCountries()).withRel("countries"));
    }
}
