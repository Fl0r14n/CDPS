package com.threepillarglobal.labs.cdps.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.apache.hadoop.io.MD5Hash;

@HTable(name = "user")
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class User {

    public static byte[] toRowKey(String email) {
        return MD5Hash.digest(email).getDigest();
    }

    @HColumnFamily(name = "ad")
    private final AccountData accountData;
    @HColumnFamily(name = "pd")
    private final PersonalData personalData;
    @HColumnFamily(name = "mn")
    private final MedicalNotes medicalNotes;
    @HColumnFamily(name = "ft")
    private final FamilyTree familyTree;

    @HColumnFamily(name = "ad")
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class AccountData {

        @HColumn
        private final String secretKey;
        @HColumn
        private final Boolean active;
        @HColumn
        private final String phone;
        @HColumn
        private final String email;
    }

    @HColumnFamily(name = "pd")
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class PersonalData {

        @HColumn
        private final String name;
        @HColumn
        private final Date dob;
        @HColumn
        private final String locationId;
    }

    @HColumnFamily(name = "mn")
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class MedicalNotes {

        public enum SMOKER {

            NO,
            CASUAL,
            HEAVY,
            PASSIVE;
            
            public static SMOKER getRandom() {
                    return values()[(int) (Math.random() * values().length)];
            }
        }

        public static enum INHERITED_RISK {

            LOW,
            MEDIUM,
            HIGH;
            
            public static INHERITED_RISK getRandom() {
                return values()[(int) (Math.random() * values().length)];
            }
        }

        @HColumn
        private final List<String> notes; //Not sure if this is ok.
        @HColumn
        private final SMOKER smoker;
        @HColumn
        private final INHERITED_RISK inheritedRisk;
        
        @HColumn
        @JsonIgnore
        private String dailyAvg;
    }

    @HColumnFamily(name = "ft")
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class FamilyTree {

        @HColumn
        public final List<String> ancestors; //Is this the propper way to model this
        @HColumn
        public final List<String> descendants;
    }
}
