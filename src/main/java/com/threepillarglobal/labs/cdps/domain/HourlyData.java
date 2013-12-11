package com.threepillarglobal.labs.cdps.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class HourlyData {
    
    private final String activityType;
    private final int duration;
    private final int averageHeartRate;
    private final int systolicPressure;
    private final int diastolicPressure;
    private final int caloriesBurned;
}
