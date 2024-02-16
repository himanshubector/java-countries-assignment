package com.acc.countries.utils;

import com.acc.countries.payload.CountryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


/**
 * The Helper class RestApiRequestHelper
 */
@Component
public class RestApiRequestHelper {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Create HttpEntity with http headers.
     *
     * @return HttpEntity the http entity
     */
    public HttpEntity createHttpEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return entity;
    }

    /**
     * Get custom ResponseEntity array T[]
     *
     * @param <T>          the type parameter
     * @param url          the url
     * @param headers      the headers
     * @param responseType the response type
     * @return the T[]
     */
    public <T> T[] getCustomResponseEntityArray(String url, HttpHeaders headers, Class<T[]> responseType) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<T[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        return responseEntity.getBody();
    }

    /**
     * Fetch country details CountryResponseDto[]
     *
     * @param url        the url
     * @param httpMethod the http method
     * @param params     the params
     * @return the CountryResponseDto[]
     */
    public CountryResponseDto[] fetchCountryDetails(String url, HttpMethod httpMethod, Map<String, String> params) {
        HttpEntity httpEntity = createHttpEntityWithHeaders();
        ResponseEntity<CountryResponseDto[]> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, CountryResponseDto[].class, params);
        return responseEntity.getBody();
    }
}
