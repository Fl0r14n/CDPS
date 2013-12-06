package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.io.MD5Hash;

import java.util.Date;

@HTable(name = "medicalRecords", columnFamilies = {"mr", "da"})
public class MedicalRecords {

    //should implement other hasing utilities like compare or do some super class for general table operations?
    public static MD5Hash toRowKey(String email, Date eventDate) { //should I return byte[]?
        //Test the validity of email? throw some asserions?
        return MD5Hash.digest(email + eventDate.toString());
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @HColumnFamily(name = "mr")
    public static class MedicalRecord {

        @HColumn(name = "medicalRecordDate")
        private final Date medicalRecordDate;
        @HColumn(name = "bmi")
        private final int bmi;
        @HColumn(name = "colesterol")
        private final int cholesterol;
        @HColumn(name = "triglycerides")
        private final int triglycerides;
        @HColumn(name = "followUp")
        private final String followUp;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    @HColumnFamily(name = "da")
    public static class DocumentsAttached {

        @HColumn(name = "documentURL")
        private final String documentURL;
    }
}
