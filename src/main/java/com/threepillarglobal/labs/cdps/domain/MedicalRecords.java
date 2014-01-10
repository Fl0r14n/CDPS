package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import java.text.SimpleDateFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.io.MD5Hash;

import java.util.Date;
import lombok.EqualsAndHashCode;

@HTable(name = "medicalRecords")
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class MedicalRecords {
    
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static byte[] toRowKey(String email, Date eventDate) {
        return (MD5Hash.digest(email).toString() + dateFormat.format(eventDate)).getBytes();
    }

    @HColumnFamily(name = "mr")
    private final MedicalRecord medicalRecord;
    @HColumnFamily(name = "da")
    private final DocumentsAttached documentsAttached;

    @HColumnFamily(name = "mr")
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class MedicalRecord {

        @HColumn
        private final Date medicalRecordDate;
        @HColumn
        private final Integer bmi;
        @HColumn
        private final Integer cholesterol;
        @HColumn
        private final Integer triglycerides;
        @HColumn
        private final String followUp;
    }

    @HColumnFamily(name = "da")
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class DocumentsAttached {

        @HColumn
        private final String documentURL;
    }
}
