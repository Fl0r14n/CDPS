package com.threepillarglobal.labs.cdps.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Date;

public class MedicalRecords {

    public static final String TABLE = "medicalRecords";

    @AllArgsConstructor
    @ToString
    @Getter
    public static class MedicalRecord {

        public static final String CFAMILY = "mr";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);

        public static byte[] BMEDICALRECORDDATE = Bytes.toBytes("medicalRecordDate"); //do some reflection here?
        public static byte[] BBMI = Bytes.toBytes("active"); //do some reflection here?
        public static byte[] BCHOLESTEROL = Bytes.toBytes("phone"); //do some reflection here?
        public static byte[] BTRIGLYCERIDES = Bytes.toBytes("triglycerides"); //do some reflection here?
        public static byte[] BFOLLOWUP = Bytes.toBytes("followUp"); //do some reflection here?

        private final Date medicalRecordDate;
        private final int bmi;
        private final int cholesterol;
        private final int triglycerides;
        private final String followUp;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class DocumentsAttached {

        public static final String CFAMILY = "da";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);

        public static byte[] BDOCUMENTURL = Bytes.toBytes("documentURL"); //do some reflection here?

        private final String documentURL;
    }
}
