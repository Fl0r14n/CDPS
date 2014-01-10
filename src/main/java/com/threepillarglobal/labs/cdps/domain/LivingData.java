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
    private final Integer minsOfSleep;
    @HColumn
    private final Integer minsOfExcercise;
    @HColumn
    private final Integer calories;
    @HColumn
    private final Integer energy;
    @HColumn
    private final Integer fat;
    @HColumn
    private final Integer saturatedFat;
    @HColumn
    private final Integer sugars;
    @HColumn
    private final Integer sodium;
    @HColumn
    private final Integer protein;
    @HColumn
    private final Integer carbohydrates;
    @HColumn
    private final String vitamins;
    @HColumn
    private final String minerals;
    @HColumn
    private final Integer water;
    @HColumn
    private final Integer alcohol;
    @HColumn
    private final Integer softDrinks;
    @HColumn
    private final BigDecimal riskFactor;
}
