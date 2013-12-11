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

    @HColumn(name = "minsOfSleep")
    private final int minsOfSleep;
    @HColumn(name = "minsOfExcercise")
    private final int minsOfExcercise;
    @HColumn(name = "calories")
    private final int calories;
    @HColumn(name = "energy")
    private final int energy;
    @HColumn(name = "fat")
    private final int fat;
    @HColumn(name = "saturatedFat")
    private final int saturatedFat;
    @HColumn(name = "sugars")
    private final int sugars;
    @HColumn(name = "sodium")
    private final int sodium;
    @HColumn(name = "protein")
    private final int protein;
    @HColumn(name = "carbohydrates")
    private final int carbohydrates;
    @HColumn(name = "vitamins")
    private final String vitamins;
    @HColumn(name = "minerals")
    private final String minerals;
    @HColumn(name = "water")
    private final int water;
    @HColumn(name = "alcohol")
    private final int alcohol;
    @HColumn(name = "softDrinks")
    private final int softDrinks;
    @HColumn(name = "riskFactor")
    private final BigDecimal riskFactor;
}
