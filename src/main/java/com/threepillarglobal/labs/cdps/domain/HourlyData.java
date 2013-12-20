package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class HourlyData {
    
    @HColumn
    private final String activityType;
    @HColumn
    private final Integer duration;
    @HColumn
    private final Integer averageHeartRate;
    @HColumn
    private final Integer systolicPressure;
    @HColumn
    private final Integer diastolicPressure;
    @HColumn
    private final Integer caloriesBurned;
}
