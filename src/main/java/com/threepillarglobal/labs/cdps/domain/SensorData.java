package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@HTable(name = "sensorData")
@HColumnFamily(name = "gd")
@AllArgsConstructor
@ToString
@Getter
public class SensorData {

    //TODO here List<String> should be replaced with something specific like activity description or something
    @HColumn(name = "h00")
    private final List<String> h00;
    @HColumn(name = "h01")
    private final List<String> h01;
    @HColumn(name = "h02")
    private final List<String> h02;
    @HColumn(name = "h03")
    private final List<String> h03;
    @HColumn(name = "h04")
    private final List<String> h04;
    @HColumn(name = "h05")
    private final List<String> h05;
    @HColumn(name = "h06")
    private final List<String> h06;
    @HColumn(name = "h07")
    private final List<String> h07;
    @HColumn(name = "h08")
    private final List<String> h08;
    @HColumn(name = "h09")
    private final List<String> h09;
    @HColumn(name = "h10")
    private final List<String> h10;
    @HColumn(name = "h11")
    private final List<String> h11;
    @HColumn(name = "h12")
    private final List<String> h12;
    @HColumn(name = "h13")
    private final List<String> h13;
    @HColumn(name = "h14")
    private final List<String> h14;
    @HColumn(name = "h15")
    private final List<String> h15;
    @HColumn(name = "h16")
    private final List<String> h16;
    @HColumn(name = "h17")
    private final List<String> h17;
    @HColumn(name = "h18")
    private final List<String> h18;
    @HColumn(name = "h19")
    private final List<String> h19;
    @HColumn(name = "h20")
    private final List<String> h20;
    @HColumn(name = "h21")
    private final List<String> h21;
    @HColumn(name = "h22")
    private final List<String> h22;
    @HColumn(name = "h23")
    private final List<String> h23;
}
