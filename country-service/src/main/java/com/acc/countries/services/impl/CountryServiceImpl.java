package com.acc.countries.services.impl;

import com.acc.countries.constants.AppConstants;
import com.acc.countries.exceptions.CountryNotFoundException;
import com.acc.countries.payload.CountryResponseDto;
import com.acc.countries.services.CountryService;
import com.acc.countries.utils.RestApiRequestHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * The Class CountryServiceImpl
 */
@Slf4j
@Service
public class CountryServiceImpl implements CountryService {
    @Value("${external.api.countriesApiUrl}")
    private String countriesApiUrl;

    @Value("${external.api.countriesByRegionApiUrl}")
    private String countriesByRegionApiUrl;

    @Value("${external.api.countriesByCodeApiUrl}")
    private String countriesByCodeApiUrl;

    @Value("${country.region.asia}")
    private String countryRegionAsia;

    @Autowired
    private RestApiRequestHelper restApiRequestHelper;


    /**
     * This method gets the sorted countries list by population density
     *
     * @return the list of type CountryResponseDto
     */
    @Override
    public List<CountryResponseDto> getSortedCountriesByPopulationDensity() {
        log.info("Entering getSortedCountriesByPopulationDensity method");

        // Retrieved all countries from an external API
        CountryResponseDto[] allCountriesInfo = getAllCountriesDetails();

        List<CountryResponseDto> sortedCountriesList = Arrays.stream(allCountriesInfo).filter(Objects::nonNull).sorted(Comparator.comparing(CountryResponseDto::getPopulation).reversed()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(sortedCountriesList)) {
            log.error("Sorted countries list is empty or null: {}", sortedCountriesList);
            throw new CountryNotFoundException("Country not found");
        }

        log.debug("Sorted countries list: {}", sortedCountriesList);
        return sortedCountriesList;
    }


    /**
     * This method retrieves the Asian country with most non-asian region borders
     *
     * @return the object of type CountryResponseDto
     */
    @Override
    public CountryResponseDto getAsianCountryWithMostNonAsianBorders() {
        log.info("Entering getAsianCountryWithMostNonAsianBorders method");

        // Map containing the parameters for filtering countries by 'Asia' region
        Map<String, String> regionFilterParamsMap = new HashMap<>();
        regionFilterParamsMap.put(AppConstants.REGION, countryRegionAsia);

        // Retrieved all Asian countries details
        CountryResponseDto[] asianCountriesInfo = restApiRequestHelper.fetchCountryDetails(countriesByRegionApiUrl, HttpMethod.GET, regionFilterParamsMap);

        if (ArrayUtils.isEmpty(asianCountriesInfo)) {
            log.debug("asianCountriesInfo array is null or empty");
            return new CountryResponseDto();
        }

        log.debug("Retrieved list of asianCountries {}", asianCountriesInfo.length);

        // Creating a Map of Asian countries and their bordering countries
        Map<String, List<String>> asianCountryBordersMap = Arrays.stream(asianCountriesInfo).filter(Objects::nonNull).filter(country -> !CollectionUtils.isEmpty(country.getBorders())).collect(Collectors.toMap(CountryResponseDto::getCca3, CountryResponseDto::getBorders));

        if (asianCountryBordersMap.isEmpty()) {
            log.debug("asianCountryBordersMap is empty");
            return new CountryResponseDto();
        }

        log.debug("Map created with key as Asian country code and value as its borders: {}", asianCountryBordersMap);

        // Creating a Map of Asian countries and the count of their non-Asian borders
        Map<String, Integer> asianCountryNonAsianBordersCountMap = getAsianCountryNonAsianBordersCountMap(asianCountryBordersMap);

        // Retrieved specific country details on the basis of 'country code'
        CountryResponseDto[] specificCountryInfo = getSpecificCountryDetails(asianCountryNonAsianBordersCountMap);
        if (ArrayUtils.isNotEmpty(specificCountryInfo)) return specificCountryInfo[0];

        throw new CountryNotFoundException("Country not found");

    }


    /**
     * This method fetches the specific country details on the basis of country code
     *
     * @return An array of CountryResponseDto objects
     *
     * @see RestApiRequestHelper
     * @see CountryResponseDto
     */
    private CountryResponseDto[] getSpecificCountryDetails(Map<String, Integer> asianCountryNonAsianBordersCountMap) {
        String countryCode = asianCountryNonAsianBordersCountMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(Map.Entry::getKey).findFirst().orElse(null);
        log.debug("Asian Country code with the most non-asian borders: {}", countryCode);

        // Map containing the parameters for retrieving country details by country code
        Map<String, String> countryCodeParamsMap = new HashMap<>();
        countryCodeParamsMap.put(AppConstants.CODE, countryCode);

        // Retrieved specific country details on the basis of 'country code'
        CountryResponseDto[] specificCountryInfo = restApiRequestHelper.fetchCountryDetails(countriesByCodeApiUrl, HttpMethod.GET, countryCodeParamsMap);
        return specificCountryInfo;
    }


    /**
     * This method retrieves details of all countries by making a request to an external API
     *
     * @return An array of CountryResponseDto objects representing the response retrieved from the external API
     * @see HttpHeaders
     * @see RestApiRequestHelper
     * @see CountryResponseDto
     */
    private CountryResponseDto[] getAllCountriesDetails() {
        HttpHeaders httpHeaders = new HttpHeaders();

        // Retrieved all countries from an external API
        CountryResponseDto[] allCountriesInfo = restApiRequestHelper.getCustomResponseEntityArray(countriesApiUrl, httpHeaders, CountryResponseDto[].class);
        log.debug("No. of countries received in response from the {} {}", countriesApiUrl, allCountriesInfo.length);
        return allCountriesInfo;
    }


    /**
     * This method retrieves the Asian country and its NonAsianBordersCount Map
     *
     * @return the Map<String,Integer>
     */
    private Map<String, Integer> getAsianCountryNonAsianBordersCountMap(Map<String, List<String>> asianCountryBordersMap) {
        // Creating a Map of Asian countries and the count of their non-Asian borders
        Map<String, Integer> asianCountryNonAsianBordersCountMap = new HashMap<>();

        asianCountryBordersMap.entrySet().forEach(entry -> {
            Optional.ofNullable(entry.getValue()).ifPresent(borderList -> {
                borderList.forEach(border -> {
                    if (!asianCountryBordersMap.containsKey(border)) {
                        asianCountryNonAsianBordersCountMap.put(entry.getKey(), asianCountryNonAsianBordersCountMap.getOrDefault(entry.getKey(), 0) + 1);
                    }
                });
            });

        });

        log.debug("New Map created with key as Asian country code and value as the count of its non-asian borders: {}", asianCountryNonAsianBordersCountMap);
        return asianCountryNonAsianBordersCountMap;
    }

}
