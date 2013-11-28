package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.io.MD5Hash;

@HTable(name = "user", columnFamilies = {"ad","pd","mn","ft"})
public class User {

    //should implement other hasing utilities like compare or do some super class for general table operations?
    public static MD5Hash toRowKey(String email) { //should I return byte[]?
        //Test the validity of email? throw some asserions?        
        return MD5Hash.digest(email);
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @HColumnFamily(name = "ad")
    public static class AccountData {

        @HColumn(name = "secretKey")
        private final String secretKey;
        @HColumn(name = "active")
        private final Boolean active;
        @HColumn(name = "phone")
        private final String phone;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @HColumnFamily(name = "pd")
    public static class PersonalData {

        @HColumn(name = "name")
        private final String name;
        @HColumn(name = "dob")
        private final Date dob;
        @HColumn(name = "locationId")
        private final String locationId;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @HColumnFamily(name = "mn")
    public static class MedicalNotes {

        @HColumn(name = "notes")
        private final List<String> notes; //Not sure if this is ok.
        @HColumn(name = "smoker")
        private final Boolean smoker;
        @HColumn(name = "riskGroup")
        private final String riskGroup;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @HColumnFamily(name = "ft")
    public static class FamilyTree {

        @HColumn(name = "ancestors")
        public final List<String> ancestors; //Is this the propper way to model this
        @HColumn(name = "descendants")
        public final List<String> descendants;
    }
}
