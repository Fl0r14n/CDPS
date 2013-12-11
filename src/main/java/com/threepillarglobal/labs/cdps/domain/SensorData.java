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
    @HColumn(name = "h00")
    private final HourlyData h00;
    @HColumn(name = "h01")
    private final HourlyData h01;
    @HColumn(name = "h02")
    private final HourlyData h02;
    @HColumn(name = "h03")
    private final HourlyData h03;
    @HColumn(name = "h04")
    private final HourlyData h04;
    @HColumn(name = "h05")
    private final HourlyData h05;
    @HColumn(name = "h06")
    private final HourlyData h06;
    @HColumn(name = "h07")
    private final HourlyData h07;
    @HColumn(name = "h08")
    private final HourlyData h08;
    @HColumn(name = "h09")
    private final HourlyData h09;
    @HColumn(name = "h10")
    private final HourlyData h10;
    @HColumn(name = "h11")
    private final HourlyData h11;
    @HColumn(name = "h12")
    private final HourlyData h12;
    @HColumn(name = "h13")
    private final HourlyData h13;
    @HColumn(name = "h14")
    private final HourlyData h14;
    @HColumn(name = "h15")
    private final HourlyData h15;
    @HColumn(name = "h16")
    private final HourlyData h16;
    @HColumn(name = "h17")
    private final HourlyData h17;
    @HColumn(name = "h18")
    private final HourlyData h18;
    @HColumn(name = "h19")
    private final HourlyData h19;
    @HColumn(name = "h20")
    private final HourlyData h20;
    @HColumn(name = "h21")
    private final HourlyData h21;
    @HColumn(name = "h22")
    private final HourlyData h22;
    @HColumn(name = "h23")
    private final HourlyData h23;
}
