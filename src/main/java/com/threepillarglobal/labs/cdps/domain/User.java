package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.io.MD5Hash;

@HTable(name = "user", columnFamilies = {"ad", "pd", "mn", "ft"})
public class User {

    public static byte[] toRowKey(String email) {
        return MD5Hash.digest(email).getDigest();
    }

    @HColumnFamily(name = "ad")
    @AllArgsConstructor
    @ToString
    @Getter
    @EqualsAndHashCode
    public static class AccountData {

        @HColumn(name = "secretKey")
        private final String secretKey;
        @HColumn(name = "active")
        private final Boolean active;
        @HColumn(name = "phone")
        private final String phone;
    }

    @HColumnFamily(name = "pd")
    @AllArgsConstructor
    @ToString
    @Getter
    @EqualsAndHashCode
    public static class PersonalData {

        @HColumn(name = "name")
        private final String name;
        @HColumn(name = "dob")
        private final Date dob;
        @HColumn(name = "locationId")
        private final String locationId;
    }

    @HColumnFamily(name = "mn")
    @AllArgsConstructor
    @ToString
    @Getter
    @EqualsAndHashCode
    public static class MedicalNotes {

        public static enum INHERITED_RISK {

            LOW,
            MEDIUM,
            HIGH
        }

        @HColumn(name = "notes")
        private final List<String> notes; //Not sure if this is ok.
        @HColumn(name = "smoker")
        private final Boolean smoker;
        @HColumn(name = "inheritedRisk")
        private final INHERITED_RISK inheritedRisk;
    }

    @HColumnFamily(name = "ft")
    @AllArgsConstructor
    @ToString
    @Getter
    @EqualsAndHashCode
    public static class FamilyTree {

        @HColumn(name = "ancestors")
        public final List<String> ancestors; //Is this the propper way to model this
        @HColumn(name = "descendants")
        public final List<String> descendants;
    }
}
