package com.threepillarglobal.labs.cdps.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.hbase.util.Bytes;

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
        
        //TODO here List<String> should be replaced with something specific like activity description or something
        //TODO what is the advantage of 24 columns in a column family vs 24 column families of hourly description?
        private final List<String> h00; 
        private final List<String> h01;
        private final List<String> h02;
        private final List<String> h03;
        private final List<String> h04;
        private final List<String> h05;
        private final List<String> h06;
        private final List<String> h07;
        private final List<String> h08;
        private final List<String> h09;
        private final List<String> h10;
        private final List<String> h11;
        private final List<String> h12;
        private final List<String> h13;
        private final List<String> h14;
        private final List<String> h15;
        private final List<String> h16;
        private final List<String> h17;
        private final List<String> h18;
        private final List<String> h19;
        private final List<String> h20;
        private final List<String> h21;
        private final List<String> h22;
        private final List<String> h23;
    }
}
