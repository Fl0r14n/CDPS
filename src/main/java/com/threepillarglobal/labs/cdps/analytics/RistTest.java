package com.threepillarglobal.labs.cdps.analytics;

import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.web.MockDataGenerator;


public class RistTest {

	
	public static void main(String[] args) {
		/*System.out.println(RiskIndexUtils.getAgeRiskIndex(Integer.valueOf(73)));
		System.out.println(RiskIndexUtils.getSmokeIndex(User.MedicalNotes.SMOKER.HEAVY));
		System.out.println(RiskIndexUtils.getLowBloodPressureRiskIndex(Double.valueOf(95)));
		System.out.println(RiskIndexUtils.getHighBloodPressureRiskIndex(Double.valueOf(155.0)));*/
		
		System.out.println ( new CardioRisk().calculateRiskFactor(MockDataGenerator.fetchMockUserData(2).get(0), MockDataGenerator.fetchMockSensorData("1-11-2012", "3-11-2012")));
		
	}

	
	
}
