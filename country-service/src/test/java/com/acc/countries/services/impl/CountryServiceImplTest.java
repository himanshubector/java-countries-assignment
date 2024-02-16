package com.acc.countries.services.impl;

import com.acc.countries.exceptions.CountryNotFoundException;
import com.acc.countries.payload.CountryResponseDto;
import com.acc.countries.utils.RestApiRequestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CountryServiceImplTest {

    @MockBean
    private RestApiRequestHelper restApiRequestHelper;

    @InjectMocks
    private CountryServiceImpl countryService;

    @Value("${external.api.countriesApiUrl}")
    private String countriesApiUrl;

    @Value("${external.api.countriesByRegionApiUrl}")
    private String countriesByRegionApiUrl;

    @Value("${external.api.countriesByCodeApiUrl}")
    private String countriesByCodeApiUrl;

    @Value("${country.region.asia}")
    private String countryRegionAsia;


    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field field = ReflectionUtils.findFields(CountryServiceImpl.class, f -> f.getName().equals("countriesApiUrl"), ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).get(0);
        field.setAccessible(true);
        field.set(countryService, countriesApiUrl);

        Field field2 = CountryServiceImpl.class.getDeclaredField("countriesByRegionApiUrl");
        field2.setAccessible(true);
        field2.set(countryService, countriesByRegionApiUrl);

        Field field3 = CountryServiceImpl.class.getDeclaredField("countryRegionAsia");
        field3.setAccessible(true);
        field3.set(countryService, countryRegionAsia);

        Field field4 = CountryServiceImpl.class.getDeclaredField("countriesByCodeApiUrl");
        field4.setAccessible(true);
        field4.set(countryService, countriesByCodeApiUrl);
    }


    @Test
    public void testGetSortedCountriesByPopulationDensityIsNotEmpty() {
        CountryResponseDto[] mockResponseArray = new CountryResponseDto[4];

        CountryResponseDto countryResponseDto1 = new CountryResponseDto();
        CountryResponseDto countryResponseDto2 = new CountryResponseDto();
        CountryResponseDto countryResponseDto3 = new CountryResponseDto();
        CountryResponseDto countryResponseDto4 = new CountryResponseDto();
        countryResponseDto1.setCca3("TUR");
        countryResponseDto1.setPopulation(45000);
        countryResponseDto2.setCca3("CHN");
        countryResponseDto2.setPopulation(78000);
        countryResponseDto3.setCca3("IND");
        countryResponseDto3.setPopulation(95000);
        countryResponseDto4.setCca3("KGZ");
        countryResponseDto4.setPopulation(20000);

        // Mock response from RestApiRequestHelper
        mockResponseArray[0] = countryResponseDto1;
        mockResponseArray[1] = countryResponseDto2;
        mockResponseArray[2] = countryResponseDto3;
        mockResponseArray[3] = countryResponseDto4;

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<CountryResponseDto[]> mockResponseEntity = ResponseEntity.ok(mockResponseArray);

        when(restApiRequestHelper.getCustomResponseEntityArray(anyString(), eq(headers), eq(CountryResponseDto[].class))).thenReturn(mockResponseArray);

        // Call the method under test
        List<CountryResponseDto> sortedCountries = countryService.getSortedCountriesByPopulationDensity();

        // Verify that the method works correctly
        assertFalse(sortedCountries.isEmpty());
        assertEquals(4, sortedCountries.size());
        assertEquals("IND", sortedCountries.get(0).getCca3());
        assertEquals("CHN", sortedCountries.get(1).getCca3());
        Assertions.assertNotNull(sortedCountries);
        Assertions.assertTrue(sortedCountries.size() > 0);
        Assertions.assertEquals(sortedCountries.get(0).getPopulation(), 95000);

        // Verify that the RestApiRequestHelper was called with the correct parameters
        verify(restApiRequestHelper, times(1)).getCustomResponseEntityArray(anyString(), eq(headers), eq(CountryResponseDto[].class));
    }


    @Test
    public void testGetSortedCountriesByPopulationDensityIsEmpty() {
        CountryResponseDto[] mockResponseArray = new CountryResponseDto[0];

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<CountryResponseDto[]> mockResponseEntity = ResponseEntity.ok(mockResponseArray);

        when(restApiRequestHelper.getCustomResponseEntityArray(anyString(), eq(headers), eq(CountryResponseDto[].class))).thenReturn(mockResponseArray);

        assertThrows(CountryNotFoundException.class, () -> countryService.getSortedCountriesByPopulationDensity());

        // Verify that the RestApiRequestHelper was called with the correct parameters
        verify(restApiRequestHelper, times(1)).getCustomResponseEntityArray(anyString(), eq(headers), eq(CountryResponseDto[].class));

    }


    @Test
    void testGetAllAsianCountriesNotEmpty() {

        CountryResponseDto[] responseArray = new CountryResponseDto[4];

        List<String> borders1 = new ArrayList<>();
        List<String> borders2 = new ArrayList<>();
        List<String> borders3 = new ArrayList<>();
        List<String> borders4 = new ArrayList<>();

        borders1 = Arrays.asList("ARM", "AZE", "BGR", "GEO", "GRC", "IRN", "IRQ", "SYR");
        borders2 = Arrays.asList("CHN", "KGZ", "RUS", "TKM", "UZB");
        borders3 = Arrays.asList("CHN", "RUS");
        borders4 = Arrays.asList("CHN", "KOR", "RUS");

        CountryResponseDto countryResponseDto1 = new CountryResponseDto();
        CountryResponseDto countryResponseDto2 = new CountryResponseDto();
        CountryResponseDto countryResponseDto3 = new CountryResponseDto();
        CountryResponseDto countryResponseDto4 = new CountryResponseDto();

        countryResponseDto1.setCca3("TUR");
        countryResponseDto1.setPopulation(15000);
        countryResponseDto1.setBorders(borders1);

        countryResponseDto2.setCca3("KAZ");
        countryResponseDto2.setPopulation(45000);
        countryResponseDto2.setBorders(borders2);

        countryResponseDto3.setCca3("MNG");
        countryResponseDto3.setPopulation(19000);
        countryResponseDto3.setBorders(borders3);

        countryResponseDto4.setCca3("PRK");
        countryResponseDto4.setPopulation(67000);
        countryResponseDto4.setBorders(borders4);

        responseArray[0] = countryResponseDto1;
        responseArray[1] = countryResponseDto2;
        responseArray[2] = countryResponseDto3;
        responseArray[3] = countryResponseDto4;

        CountryResponseDto[] newResponseArray = new CountryResponseDto[1];
        newResponseArray[0] = countryResponseDto1;

        when(restApiRequestHelper.fetchCountryDetails(anyString(), eq(HttpMethod.GET), anyMap())).thenReturn(responseArray);
        when(restApiRequestHelper.fetchCountryDetails(eq(countriesByCodeApiUrl), eq(HttpMethod.GET), anyMap())).thenReturn(newResponseArray);

        // Call the method under test
        CountryResponseDto countryResponseDto = countryService.getAsianCountryWithMostNonAsianBorders();

        assertNotNull(countryResponseDto);
        assertEquals("TUR", countryResponseDto.getCca3());
    }


    @Test
    void testGetAllAsianCountriesWithNullResponse() {

        CountryResponseDto[] responseArray = null;

        when(restApiRequestHelper.fetchCountryDetails(anyString(), eq(HttpMethod.GET), anyMap())).thenReturn(responseArray);

        // Call the method under test
        CountryResponseDto countryResponseDto = countryService.getAsianCountryWithMostNonAsianBorders();
        assertEquals(new CountryResponseDto(), countryResponseDto);

    }


    @Test
    void testGetAllAsianCountriesWithEmptyResponse() {

        CountryResponseDto[] responseArray = new CountryResponseDto[0];

        when(restApiRequestHelper.fetchCountryDetails(anyString(), eq(HttpMethod.GET), anyMap())).thenReturn(responseArray);

        // Call the method under test
        CountryResponseDto countryResponseDto = countryService.getAsianCountryWithMostNonAsianBorders();
        assertEquals(new CountryResponseDto(), countryResponseDto);

    }


    @Test
    void testGetAsianCountryWithAsianCountryBordersMapAsEmpty() {

        CountryResponseDto[] responseArray = new CountryResponseDto[4];

        CountryResponseDto countryResponseDto1 = new CountryResponseDto();
        CountryResponseDto countryResponseDto2 = new CountryResponseDto();
        CountryResponseDto countryResponseDto3 = new CountryResponseDto();
        CountryResponseDto countryResponseDto4 = new CountryResponseDto();

        countryResponseDto1.setCca3("TUR");
        countryResponseDto1.setPopulation(15000);

        countryResponseDto2.setCca3("KAZ");
        countryResponseDto2.setPopulation(45000);

        countryResponseDto3.setCca3("MNG");
        countryResponseDto3.setPopulation(19000);

        countryResponseDto4.setCca3("PRK");
        countryResponseDto4.setPopulation(67000);

        responseArray[0] = countryResponseDto1;
        responseArray[1] = countryResponseDto2;
        responseArray[2] = countryResponseDto3;
        responseArray[3] = countryResponseDto4;

        when(restApiRequestHelper.fetchCountryDetails(anyString(), eq(HttpMethod.GET), anyMap())).thenReturn(responseArray);

        // Call the method under test
        CountryResponseDto countryResponseDto = countryService.getAsianCountryWithMostNonAsianBorders();

        assertEquals(new CountryResponseDto(), countryResponseDto);
        assertNotNull(countryResponseDto);

    }


    @Test
    void testGetAsianCountryWithMostNonAsianBordersThrowsException() {
        CountryResponseDto[] responseArray = new CountryResponseDto[4];

        CountryResponseDto countryResponseDto1 = new CountryResponseDto();
        CountryResponseDto countryResponseDto2 = new CountryResponseDto();
        CountryResponseDto countryResponseDto3 = new CountryResponseDto();
        CountryResponseDto countryResponseDto4 = new CountryResponseDto();

        List<String> borders1 = new ArrayList<>();
        List<String> borders2 = new ArrayList<>();
        List<String> borders3 = new ArrayList<>();
        List<String> borders4 = new ArrayList<>();

        borders1 = Arrays.asList("ARM", "AZE", "BGR", "GEO", "GRC", "IRN", "IRQ", "SYR");
        borders2 = Arrays.asList("CHN", "KGZ", "RUS", "TKM", "UZB");
        borders3 = Arrays.asList("CHN", "RUS");
        borders4 = Arrays.asList("CHN", "KOR", "RUS");

        countryResponseDto1.setCca3("TUR");
        countryResponseDto1.setPopulation(15000);
        countryResponseDto1.setBorders(borders1);

        countryResponseDto2.setCca3("KAZ");
        countryResponseDto2.setPopulation(45000);
        countryResponseDto3.setBorders(borders2);

        countryResponseDto3.setCca3("MNG");
        countryResponseDto3.setPopulation(19000);
        countryResponseDto3.setBorders(borders3);

        countryResponseDto4.setCca3("PRK");
        countryResponseDto4.setPopulation(67000);
        countryResponseDto4.setBorders(borders4);

        responseArray[0] = countryResponseDto1;
        responseArray[1] = countryResponseDto2;
        responseArray[2] = countryResponseDto3;
        responseArray[3] = countryResponseDto4;

        CountryResponseDto[] newResponseArray = new CountryResponseDto[0];

        when(restApiRequestHelper.fetchCountryDetails(anyString(), eq(HttpMethod.GET), anyMap())).thenReturn(responseArray);
        when(restApiRequestHelper.fetchCountryDetails(eq(countriesByCodeApiUrl), eq(HttpMethod.GET), anyMap())).thenReturn(newResponseArray);

        // Call the method under test
        assertThrows(CountryNotFoundException.class, () -> countryService.getAsianCountryWithMostNonAsianBorders());

    }

}





