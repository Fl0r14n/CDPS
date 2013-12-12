package com.threepillarglobal.labs.cdps.service.api;

import com.threepillarglobal.labs.cdps.domain.SensorData;
import java.util.List;

public interface ChartService {

    List<SensorData> getSensorData(String id);
    
    List<SensorData> getSensorData(String startDate, String endDate);
}
