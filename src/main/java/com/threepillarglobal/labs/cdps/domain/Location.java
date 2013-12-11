package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@HTable(name = "location", columnFamilies = {"ld", "res"})
public class Location {

    public static final String separator = "|";

    //should implement other hasing utilities like compare or do some super class for general table operations?
    public static String toRowKey(String countryName, String countyName, String cityName) { //should I return byte[]?
        //Test the validity of email? throw some asserions?
        return countryName + separator + countyName + separator + cityName;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @HColumnFamily(name = "ld")
    public static class LocationDetails {

        @HColumn(name = "city")
        private final String city;
        @HColumn(name = "county")
        private final String county;
        @HColumn(name = "country")
        private final String country;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @HColumnFamily(name = "res")
    public static class Residents {

        @HColumn(name = "residents")
        private final List<String> residents;
        //we might add the username as column descriptor and the hash value as payload or vice versa,
        // but I don't know how to incorporate this in the model
    }
}
