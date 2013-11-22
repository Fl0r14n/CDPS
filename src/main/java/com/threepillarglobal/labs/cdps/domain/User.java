package com.threepillarglobal.labs.cdps.domain;

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

        private final String secretKey;
        private final Boolean active;
        private final String phone;
    }

    //should implement other hasing utilities like compare or do some super class for general table operations?
    @AllArgsConstructor
    @ToString
    @Getter
    public static class PersonalData {

        public static final String CFAMILY = "pd";

        private final String name;
        private final String dob;
        private final String locationId;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class MedicalNotes {

        public static final String CFAMILY = "mn";

        private final List<String> notes; //Not sure if this is ok. 
        private final String smoker;
        private final String riskGroup;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class FamilyTree {

        public static final String CFAMILY = "ft";

        public List<String> ancestors; //Is this the propper way to model this
        public List<String> descendants;
    }

}
