package com.threepillarglobal.labs.cdps.service.api;

import com.threepillarglobal.labs.cdps.domain.SensorData;

import java.util.Date;
import java.util.List;

public interface ChartService {

    List<SensorData> getSensorData(Date startDate, Date endDate);
    
}
