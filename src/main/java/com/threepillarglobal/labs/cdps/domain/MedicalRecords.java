package com.threepillarglobal.labs.cdps.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.hbase.util.Bytes;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: soros
 * Date: 11/26/13
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
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

        private static Date medicalRecordDate;
        private static int bmi;
        private static int cholesterol;
        private static int triglycerides;
        private static String followUp;
    }

    @AllArgsConstructor
    @ToString
    @Getter
    public static class DocumentsAttached {

        public static final String CFAMILY = "da";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);

        public static byte[] BDOCUMENTURL = Bytes.toBytes("documentURL"); //do some reflection here?

        private static String documentURL;
    }
}
