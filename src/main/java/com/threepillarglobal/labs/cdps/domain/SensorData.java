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
    @HColumn
    private final Date eventDate;
    @HColumnFamily
    private final HourlyData h00;
    @HColumnFamily
    private final HourlyData h01;
    @HColumnFamily
    private final HourlyData h02;
    @HColumnFamily
    private final HourlyData h03;
    @HColumnFamily
    private final HourlyData h04;
    @HColumnFamily
    private final HourlyData h05;
    @HColumnFamily
    private final HourlyData h06;
    @HColumnFamily
    private final HourlyData h07;
    @HColumnFamily
    private final HourlyData h08;
    @HColumnFamily
    private final HourlyData h09;
    @HColumnFamily
    private final HourlyData h10;
    @HColumnFamily
    private final HourlyData h11;
    @HColumnFamily
    private final HourlyData h12;
    @HColumnFamily
    private final HourlyData h13;
    @HColumnFamily
    private final HourlyData h14;
    @HColumnFamily
    private final HourlyData h15;
    @HColumnFamily
    private final HourlyData h16;
    @HColumnFamily
    private final HourlyData h17;
    @HColumnFamily
    private final HourlyData h18;
    @HColumnFamily
    private final HourlyData h19;
    @HColumnFamily
    private final HourlyData h20;
    @HColumnFamily
    private final HourlyData h21;
    @HColumnFamily
    private final HourlyData h22;
    @HColumnFamily
    private final HourlyData h23;
}
