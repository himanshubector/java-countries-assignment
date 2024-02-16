package com.acc.countries.payload;

import lombok.Data;
import java.util.List;
import java.util.Map;


/**
 * The Response Dto class CountryResponseDto
 */
@Data
public class CountryResponseDto {
    private NameResponseDto name;
    private List<String> tld;
    private String cca2;
    private String ccn3;
    private String cca3;
    private String cioc;
    private boolean independent;
    private String status;
    private boolean unMember;
    private Map<String, CurrencyResponseDto> currencies;
    private IddResponseDto idd;
    private List<String> capital;
    private List<String> altSpellings;
    private String region;
    private String subregion;
    private Map<String, String> languages;
    private Map<String, TranslationResponseDto> translations;
    private List<Double> latlng;
    private boolean landlocked;
    private List<String> borders;
    private double area;
    private DemonymsResponseDto demonyms;
    private String flag;
    private Map<String, String> maps;
    private int population;
    private String fifa;
    private CarResponseDto car;
    private List<String> timezones;
    private List<String> continents;
    private FlagsResponseDto flags;
    private CoatOfArmsResponseDto coatOfArms;
    private String startOfWeek;
    private CapitalInfoResponseDto capitalInfo;
    private PostalCodeResponseDto postalCode;


    /**
     * The type NameResponseDto
     */
    @Data
    static class NameResponseDto {
        private String common;
        private String official;
        private Map<String, NativeNameResponseDto> nativeName;
    }


    /**
     * The type NativeNameResponseDto
     */
    @Data
    static class NativeNameResponseDto {
        private String common;
        private String official;
    }


    /**
     * The type CurrencyResponseDto
     */
    @Data
    static class CurrencyResponseDto {
        private String name;
        private String symbol;
    }

    /**
     * The type CurrencyNameSymbolDto
     */
    @Data
    static class CurrencyNameSymbolDto {
        private String name;
        private String symbol;
    }

    /**
     * The type IddResponseDto.
     */
    @Data
    static class IddResponseDto {
        private String root;
        private List<String> suffixes;
    }

    /**
     * The type TranslationResponseDto.
     */
    @Data
    static class TranslationResponseDto {
        private String official;
        private String common;
    }

    /**
     * The type DemonymsResponseDto.
     */
    @Data
    static class DemonymsResponseDto {
        private Map<String, String> eng;
        private Map<String, String> fra;
    }

    /**
     * The type CarResponseDto.
     */
    @Data
    static class CarResponseDto {
        private List<String> signs;
        private String side;
    }

    /**
     * The type FlagsResponseDto.
     */
    @Data
    static class FlagsResponseDto {
        private String png;
        private String svg;
        private String alt;
    }

    /**
     * The type CoatOfArmsResponseDto.
     */
    @Data
    static class CoatOfArmsResponseDto {
        private String png;
        private String svg;
    }

    /**
     * The type CapitalInfoResponseDto.
     */
    @Data
    static class CapitalInfoResponseDto {
        private List<Double> latlng;
    }

    /**
     * The type PostalCodeResponseDto.
     */
    @Data
    static class PostalCodeResponseDto {
        private String format;
        private String regex;
    }

}