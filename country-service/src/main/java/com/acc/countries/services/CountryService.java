package com.acc.countries.services;

import com.acc.countries.payload.CountryResponseDto;
import java.util.List;

/**
 * The interface CountryService
 */
public interface CountryService {
    /**
     * Gets sorted list of countries by population density.
     *
     * @return the sorted countries list by population density
     */
    List<CountryResponseDto> getSortedCountriesByPopulationDensity();


    /**
     * Gets most bordering asian country with most non-asian region borders.
     *
     * @return the most bordering asian country with most non-asian region borders
     */
    CountryResponseDto getAsianCountryWithMostNonAsianBorders();

}
