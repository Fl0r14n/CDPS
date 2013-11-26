package com.threepillarglobal.labs.cdps.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.hbase.util.Bytes;

import java.math.BigDecimal;
import java.util.List;

public class LivingData {
    public static final String TABLE = "livingData";

    @AllArgsConstructor
    @ToString
    @Getter
    public static class DailyPatientProfile {

        public static final String CFAMILY = "dpp";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);
        public static byte[] BHOURSOFSLEEP = Bytes.toBytes("minsOfSleep"); //do some reflection here?
        public static byte[] BHOURSOFEXCERCISE = Bytes.toBytes("minsOfExercise"); //do some reflection here?
        public static byte[] BCALORIES = Bytes.toBytes("calories"); //do some reflection here?
        public static byte[] BENERGY = Bytes.toBytes("energy"); //do some reflection here?
        public static byte[] BFAT = Bytes.toBytes("fat"); //do some reflection here?
        public static byte[] BSATURATEDFAT = Bytes.toBytes("saturatedFat"); //do some reflection here?
        public static byte[] BSUGARS = Bytes.toBytes("sugars"); //do some reflection here?
        public static byte[] BSODIUM = Bytes.toBytes("sodium"); //do some reflection here?
        public static byte[] BPROTEIN = Bytes.toBytes("sodium"); //do some reflection here?
        public static byte[] BCARBOHYDRATES = Bytes.toBytes("carbohydrates"); //do some reflection here?
        public static byte[] BVITAMINS = Bytes.toBytes("vitamins"); //do some reflection here?
        public static byte[] BMINERALS = Bytes.toBytes("minerals"); //do some reflection here?
        public static byte[] BWATER = Bytes.toBytes("water"); //do some reflection here?
        public static byte[] BALCOHOL = Bytes.toBytes("alcohol"); //do some reflection here?
        public static byte[] BSOFTDRINKS = Bytes.toBytes("softDrinks"); //do some reflection here?
        public static byte[] BRISKFACTOR = Bytes.toBytes("riskFactor"); //do some reflection here?

        private static int minsOfSleep;
        private static int minsOfExcercise;
        private static int calories;
        private static int energy;
        private static int fat;
        private static int saturatedFat;
        private static int sugars;
        private static int sodium;
        private static int protein;
        private static int carbohydrates;
        private static String vitamins;
        private static String minerals;
        private static int water;
        private static int alcohol;
        private static int softDrinks;
        private static BigDecimal riskFactor;
    }
}
