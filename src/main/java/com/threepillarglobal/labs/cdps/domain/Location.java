package com.threepillarglobal.labs.cdps.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.hbase.util.Bytes;

public class Location {

    public static final String TABLE = "location";

    @AllArgsConstructor
    @ToString
    @Getter
    public static class LocationDetails {

        public static final String CFAMILY = "ld";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);
        public static byte[] BCITY = Bytes.toBytes("city"); //do some reflection here?
        public static byte[] BCOUNTY = Bytes.toBytes("county"); //do some reflection here?
        public static byte[] BZIP = Bytes.toBytes("zip"); //do some reflection here?
        public static byte[] BCOUNTRY = Bytes.toBytes("country"); //do some reflection here?

        private final String city;
        private final String county;
        private final String zip;
        private final String country;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class Residents {

        public static final String CFAMILY = "res";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);
        public static byte[] BResidents = Bytes.toBytes("residents"); //do some reflection here?

        private final List<String> residents;
        //we might add the username as column descriptor and the hash value as payload or vice versa,
        // but I don't know how to incorporate this in the model
    }

}
