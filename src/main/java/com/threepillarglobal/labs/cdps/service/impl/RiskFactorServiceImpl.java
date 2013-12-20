package com.threepillarglobal.labs.cdps.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.threepillarglobal.labs.cdps.analytics.RiskAnalyser;
import com.threepillarglobal.labs.cdps.dao.repository.SensorDataRepository;
import com.threepillarglobal.labs.cdps.dao.repository.UserRepository;
import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.service.api.RiskService;
import com.threepillarglobal.labs.cdps.service.api.UserService;
import com.threepillarglobal.labs.cdps.service.mock.MockDataGenerator;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value = "riskFactorServiceImpl")
public class RiskFactorServiceImpl implements RiskService {

	@Autowired
    @Qualifier(value = "userServiceImpl")
    private UserService userService;
	
	@Autowired
	private SensorDataRepository sensorRepo;
	
	@Override
	public CardioRisk getCardioRisk(String uid, Date from, Date to) {
		
		//System.err.println("=== From: " + from + " To: " + to);
		
		DateTime b = new DateTime(Calendar.getInstance().getTime());
		User user = userService.getUser(uid);
		DateTime e = new DateTime(Calendar.getInstance().getTime());
		//if(user!=null)
		//	System.out.println("User fetched: " + uid + ' ' +  user + " in " + Seconds.secondsBetween(b, e).getSeconds() % 60 + " secs");
        
        
		
        b = new DateTime(Calendar.getInstance().getTime());
        List<SensorData> sensorData = sensorRepo.findSensorDataForUserInInterval(uid, from, to);
        e = new DateTime(Calendar.getInstance().getTime());
        //if(sensorData!=null)
        //	System.out.println("=== Sensor data list [" + from + " : " + to + "] size: " + sensorData.size() + " " + sensorData.get(0).toString() + " retrieved in " + Seconds.secondsBetween(b, e).getSeconds() % 60 + " seconds");
		
		
		if(user!=null && sensorData!=null)
			return new RiskAnalyser().calculateRiskFactor(user, sensorData );
		else
			return new CardioRisk();
	}
    
}
