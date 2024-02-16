package com.acc.countries.exceptions;

/**
 * The Exception class CountryNotFoundException
 */
public class CountryNotFoundException extends RuntimeException {
    /**
     * Instantiates a new Country not found exception.
     *
     * @param message the message
     */
    public CountryNotFoundException(String message) {
        super(message);
    }

    /**
     * Instantiates a new CountryNotFoundException
     */
    public CountryNotFoundException() {
    }
}
