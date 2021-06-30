package org.jcode.assembler;

import lombok.SneakyThrows;
import org.jcode.model.Country;
import org.jcode.resource.GlobalCovidResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CountryModelAssembler implements RepresentationModelAssembler<Country, EntityModel<Country>> {

    @Override
    public EntityModel<Country> toModel(Country country) {
        return EntityModel.of(country,
                linkTo(methodOn(GlobalCovidResource.class).getCountry(country.getName())).withSelfRel(),
                linkTo(methodOn(GlobalCovidResource.class).getAllCountries()).withRel("countries"));
    }
}
