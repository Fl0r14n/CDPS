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
    
    @HColumn(name = "activityType")
    private final String activityType;
    @HColumn(name = "duration")
    private final Integer duration;
    @HColumn(name = "averageHeartRate")
    private final Integer averageHeartRate;
    @HColumn(name = "systolicPressure")
    private final Integer systolicPressure;
    @HColumn(name = "diastolicPressure")
    private final Integer diastolicPressure;
    @HColumn(name = "caloriesBurned")
    private final Integer caloriesBurned;
}
