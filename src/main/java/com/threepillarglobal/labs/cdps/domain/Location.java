package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@HTable(name = "location")
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Location {

    private static final String separator = "|";

    public static byte[] toRowKey(String countryName, String countyName, String cityName) {
        return (countryName + separator + countyName + separator + cityName).getBytes();
    }

    @HColumnFamily(name = "ld")
    private final LocationDetails locationDetails;
    @HColumnFamily(name = "res")
    private final Residents residents;
    
    @HColumnFamily(name = "ld")
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class LocationDetails {

        @HColumn
        private final String city;
        @HColumn
        private final String county;
        @HColumn
        private final String country;
    }

    @HColumnFamily(name = "res")
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Residents {

        @HColumn
        private final List<String> residents;
        //we might add the username as column descriptor and the hash value as payload or vice versa,
        // but I don't know how to incorporate this in the model
    }
}
