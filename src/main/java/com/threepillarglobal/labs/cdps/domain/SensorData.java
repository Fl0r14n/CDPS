package com.threepillarglobal.labs.cdps.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.hbase.util.Bytes;

import java.math.BigDecimal;
import java.util.List;

public class SensorData {
    public static final String TABLE = "sensorData";

    @AllArgsConstructor
    @ToString
    @Getter
    public static class GatheredData {

        public static final String CFAMILY = "gd";
        public static byte[] BCFAMILY = Bytes.toBytes(CFAMILY);
        public static byte[] BH00 = Bytes.toBytes("h00"); //do some reflection here?
        public static byte[] BH01 = Bytes.toBytes("h01"); //do some reflection here?
        public static byte[] BH02 = Bytes.toBytes("h02"); //do some reflection here?
        public static byte[] BH03 = Bytes.toBytes("h03"); //do some reflection here?
        public static byte[] BH04 = Bytes.toBytes("h04"); //do some reflection here?
        public static byte[] BH05 = Bytes.toBytes("h05"); //do some reflection here?
        public static byte[] BH06 = Bytes.toBytes("h06"); //do some reflection here?
        public static byte[] BH07 = Bytes.toBytes("h07"); //do some reflection here?
        public static byte[] BH08 = Bytes.toBytes("h08"); //do some reflection here?
        public static byte[] BH09 = Bytes.toBytes("h09"); //do some reflection here?
        public static byte[] BH10 = Bytes.toBytes("h10"); //do some reflection here?
        public static byte[] BH11 = Bytes.toBytes("h11"); //do some reflection here?
        public static byte[] BH12 = Bytes.toBytes("h12"); //do some reflection here?
        public static byte[] BH13 = Bytes.toBytes("h13"); //do some reflection here?
        public static byte[] BH14 = Bytes.toBytes("h14"); //do some reflection here?
        public static byte[] BH15 = Bytes.toBytes("h15"); //do some reflection here?
        public static byte[] BH16 = Bytes.toBytes("h16"); //do some reflection here?
        public static byte[] BH17 = Bytes.toBytes("h17"); //do some reflection here?
        public static byte[] BH18 = Bytes.toBytes("h18"); //do some reflection here?
        public static byte[] BH19 = Bytes.toBytes("h19"); //do some reflection here?
        public static byte[] BH20 = Bytes.toBytes("h20"); //do some reflection here?
        public static byte[] BH21 = Bytes.toBytes("h21"); //do some reflection here?
        public static byte[] BH22 = Bytes.toBytes("h22"); //do some reflection here?
        public static byte[] BH23 = Bytes.toBytes("h23"); //do some reflection here?

        private static List<String> h00;
        private static List<String> h01;
        private static List<String> h02;
        private static List<String> h03;
        private static List<String> h04;
        private static List<String> h05;
        private static List<String> h06;
        private static List<String> h07;
        private static List<String> h08;
        private static List<String> h09;
        private static List<String> h10;
        private static List<String> h11;
        private static List<String> h12;
        private static List<String> h13;
        private static List<String> h14;
        private static List<String> h15;
        private static List<String> h16;
        private static List<String> h17;
        private static List<String> h18;
        private static List<String> h19;
        private static List<String> h20;
        private static List<String> h21;
        private static List<String> h22;
        private static List<String> h23;
    }
}
