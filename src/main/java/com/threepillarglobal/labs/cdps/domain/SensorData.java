package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import org.apache.hadoop.io.MD5Hash;

@HTable(name = "sensorData")
@HColumnFamily(name = "gd")
@AllArgsConstructor
@ToString
@Getter
@EqualsAndHashCode
public class SensorData {
    
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public static byte[] toRowKey(String email, Date eventDate) {
        return (MD5Hash.digest(email).toString() + dateFormat.format(eventDate)).getBytes();
    }
    
    //helps UI to plot the graph, it reduces the processing, so we don't deduce the date from the rowkey
    @HColumn(name = "eventDate")
    private final Date eventDate;
    @HColumnFamily(name = "h00")
    private final HourlyData h00;
    @HColumnFamily(name = "h01")
    private final HourlyData h01;
    @HColumnFamily(name = "h02")
    private final HourlyData h02;
    @HColumnFamily(name = "h03")
    private final HourlyData h03;
    @HColumnFamily(name = "h04")
    private final HourlyData h04;
    @HColumnFamily(name = "h05")
    private final HourlyData h05;
    @HColumnFamily(name = "h06")
    private final HourlyData h06;
    @HColumnFamily(name = "h07")
    private final HourlyData h07;
    @HColumnFamily(name = "h08")
    private final HourlyData h08;
    @HColumnFamily(name = "h09")
    private final HourlyData h09;
    @HColumnFamily(name = "h10")
    private final HourlyData h10;
    @HColumnFamily(name = "h11")
    private final HourlyData h11;
    @HColumnFamily(name = "h12")
    private final HourlyData h12;
    @HColumnFamily(name = "h13")
    private final HourlyData h13;
    @HColumnFamily(name = "h14")
    private final HourlyData h14;
    @HColumnFamily(name = "h15")
    private final HourlyData h15;
    @HColumnFamily(name = "h16")
    private final HourlyData h16;
    @HColumnFamily(name = "h17")
    private final HourlyData h17;
    @HColumnFamily(name = "h18")
    private final HourlyData h18;
    @HColumnFamily(name = "h19")
    private final HourlyData h19;
    @HColumnFamily(name = "h20")
    private final HourlyData h20;
    @HColumnFamily(name = "h21")
    private final HourlyData h21;
    @HColumnFamily(name = "h22")
    private final HourlyData h22;
    @HColumnFamily(name = "h23")
    private final HourlyData h23;
}
