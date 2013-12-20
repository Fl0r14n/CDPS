package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.EqualsAndHashCode;
import org.apache.hadoop.io.MD5Hash;

@HTable(name = "livingData")
@HColumnFamily(name = "dpp")
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class LivingData {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static byte[] toRowKey(String email, Date eventDate) {
        return (MD5Hash.digest(email).toString() + dateFormat.format(eventDate)).getBytes();
    }

    @HColumn
    private final int minsOfSleep;
    @HColumn
    private final int minsOfExcercise;
    @HColumn
    private final int calories;
    @HColumn
    private final int energy;
    @HColumn
    private final int fat;
    @HColumn
    private final int saturatedFat;
    @HColumn
    private final int sugars;
    @HColumn
    private final int sodium;
    @HColumn
    private final int protein;
    @HColumn
    private final int carbohydrates;
    @HColumn
    private final String vitamins;
    @HColumn
    private final String minerals;
    @HColumn
    private final int water;
    @HColumn
    private final int alcohol;
    @HColumn
    private final int softDrinks;
    @HColumn
    private final BigDecimal riskFactor;
}
