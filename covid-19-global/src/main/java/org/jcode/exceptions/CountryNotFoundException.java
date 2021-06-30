package org.jcode.exceptions;

public class CountryNotFoundException extends Exception{
    public CountryNotFoundException(String country) {
        super("Cant find country with name: " + country);
    }
}
