package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@HTable(name = "medicalRecords", columnFamilies = {"mr", "da"})
public class MedicalRecords {

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
