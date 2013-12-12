package com.threepillarglobal.labs.cdps.service.mock;

import java.util.Date;

import com.threepillarglobal.labs.cdps.analytics.RiskAnalyser;
import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.service.api.RiskService;

import org.springframework.stereotype.Service;

@Service(value = "riskFactorServiceMock")
public class RiskFactorServiceMock implements RiskService {
	
	@Override
	public CardioRisk getCardioRisk(String uid, Date from, Date to) {
		return new RiskAnalyser().calculateRiskFactor(MockDataGenerator.fetchMockUserData(2).get(0), MockDataGenerator.fetchMockSensorData( from, to) );
	}
    
}
