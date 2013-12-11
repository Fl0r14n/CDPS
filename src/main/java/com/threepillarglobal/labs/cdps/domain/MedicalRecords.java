package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.io.MD5Hash;

import java.util.Date;
import lombok.EqualsAndHashCode;

@HTable(name = "medicalRecords", columnFamilies = {"mr", "da"})
public class MedicalRecords {

    public static byte[] toRowKey(String email, Date eventDate) {
        return MD5Hash.digest(email + eventDate.toString()).getDigest();
    }

    @HColumnFamily(name = "mr")
    @AllArgsConstructor
    @ToString
    @Getter
    @EqualsAndHashCode
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

    @HColumnFamily(name = "da")
    @AllArgsConstructor
    @ToString
    @Getter
    @EqualsAndHashCode
    public static class DocumentsAttached {

        @HColumn(name = "documentURL")
        private final String documentURL;
    }
}
