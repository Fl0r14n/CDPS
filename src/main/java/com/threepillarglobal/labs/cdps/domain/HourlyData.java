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
    private final int duration;
    @HColumn(name = "averageHeartRate")
    private final int averageHeartRate;
    @HColumn(name = "systolicPressure")
    private final int systolicPressure;
    @HColumn(name = "diastolicPressure")
    private final int diastolicPressure;
    @HColumn(name = "caloriesBurned")
    private final int caloriesBurned;
}
