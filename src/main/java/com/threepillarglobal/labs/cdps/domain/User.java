package com.threepillarglobal.labs.cdps.domain;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.MD5Hash;

public class User {

    public static final String TABLE = "user";

    public static MD5Hash toRowKey(String email) { //should I return byte[]?
        //Test the validity of email? throw some asserions?        
        return MD5Hash.digest(email);
    }

    //should implement other hasing utilities like compare or do some super class for general table operations?
    @AllArgsConstructor
    @ToString
    @Getter
    public static class AccountData {

        public static final String CFAMILY = "ad";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);
        public static byte[] BSECRETKEY = Bytes.toBytes("secretKey"); //do some reflection here?
        public static byte[] BACTIVE = Bytes.toBytes("active"); //do some reflection here?
        public static byte[] BPHONE = Bytes.toBytes("phone"); //do some reflection here?

        private static String secretKey;
        private static Boolean active;
        private static String phone;
    }

    //should implement other hasing utilities like compare or do some super class for general table operations?
    @AllArgsConstructor
    @ToString
    @Getter
    public static class PersonalData {

        public static final String CFAMILY = "pd";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);
        public static byte[] BNAME = Bytes.toBytes("name"); //do some reflection here?
        public static byte[] BDOB = Bytes.toBytes("dob"); //do some reflection here?
        public static byte[] BLOCATIONID = Bytes.toBytes("locationID"); //do some reflection here?


        private static String name;
        private static Date dob;
        private static String locationId;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class MedicalNotes {

        public static final String CFAMILY = "mn";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);
        public static byte[] BNAME = Bytes.toBytes("notes"); //do some reflection here?
        public static byte[] BDOB = Bytes.toBytes("smoker"); //do some reflection here?
        public static byte[] BLOCATIONID = Bytes.toBytes("riskGroup"); //do some reflection here?

        private static List<String> notes; //Not sure if this is ok.
        private static Boolean smoker;
        private static String riskGroup;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class FamilyTree {

        public static final String CFAMILY = "ft";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);
        public static byte[] BNAME = Bytes.toBytes("ancestors"); //do some reflection here?
        public static byte[] BDOB = Bytes.toBytes("descendants"); //do some reflection here?

        public List<String> ancestors; //Is this the propper way to model this
        public List<String> descendants;
    }

}
