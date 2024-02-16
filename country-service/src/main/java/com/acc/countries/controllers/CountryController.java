package com.acc.countries.controllers;

import com.acc.countries.payload.CountryResponseDto;
import com.acc.countries.services.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * The Controller class CountryController
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {
    @Autowired
    private CountryService countryService;

    /**
     * This method fetches the sorted countries by population density.
     *
     * @return the ResponseEntity
     */
    @GetMapping("/getByPopulationDensity")
    public ResponseEntity<List<CountryResponseDto>> getSortedCountriesByPopulationDensity() {
        log.info("Inside the getSortedCountriesByPopulationDensity method of CountryController");
        List<CountryResponseDto> countryResponseDtoList = countryService.getSortedCountriesByPopulationDensity();
        log.debug("Retrieved sorted list of countries by population density: {}", countryResponseDtoList);
        return ResponseEntity.ok(countryResponseDtoList);
    }

    /**
     * This method fetches the most bordering asian country with different region borders.
     *
     * @return the ResponseEntity
     */
    @GetMapping("/getAsianCountryWithMostNonAsianBorders")
    public ResponseEntity<CountryResponseDto> getAsianCountryWithMostNonAsianBorders() {
        log.info("Inside the getAsianCountryWithMostNonAsianBorders method of CountryController");
        CountryResponseDto countryResponseDto = countryService.getAsianCountryWithMostNonAsianBorders();
        log.debug("Retrieved the Asian Country with most non-asian border countries: {}", countryResponseDto);
        return ResponseEntity.ok(countryResponseDto);
    }

}
