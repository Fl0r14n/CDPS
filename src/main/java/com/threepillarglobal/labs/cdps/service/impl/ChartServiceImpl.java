package com.threepillarglobal.labs.cdps.service.impl;

import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.service.api.ChartService;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service(value = "chartServiceImpl")
public class ChartServiceImpl implements ChartService {


    @Override
    public List<SensorData> getSensorData(Date startDate, Date endDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
