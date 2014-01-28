package com.threepillarglobal.labs.cdps.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.threepillarglobal.labs.cdps.analytics.RiskAnalyser;
import com.threepillarglobal.labs.cdps.analytics.RiskIndexUtils;
import com.threepillarglobal.labs.cdps.dao.repository.SensorDataRepository;
import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.Eventdate;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.UserAverage;
import com.threepillarglobal.labs.cdps.service.api.RiskService;
import com.threepillarglobal.labs.cdps.service.api.UserService;

import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value = "riskFactorServiceImpl")
public class RiskFactorServiceImpl implements RiskService {

	private static final int DATA_RESOLUTION_LIMIT = 30;

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
		//	System.out.println("=== User fetched: " + uid + ' ' +  user + " in " + Seconds.secondsBetween(b, e).getSeconds() % 60 + " secs");
        
        
		int noOfDays = Days.daysBetween( new DateTime(from), new DateTime(to)).getDays();
		
        b = new DateTime(Calendar.getInstance().getTime());
        
        if(noOfDays<DATA_RESOLUTION_LIMIT){
        	//System.out.println("=== days: " + noOfDays + " computing SensorData..");
	        List<SensorData> sensorData = sensorRepo.findSensorDataForUserInInterval(uid, from, to);
	        e = new DateTime(Calendar.getInstance().getTime());
			
			if(user!=null && sensorData!=null){
				//System.out.println("=== Sensor data list [" + from + " : " + to + "] size: " + sensorData.size() + " retrieved in " + Seconds.secondsBetween(b, e).getSeconds() % 60 + " seconds");
				b = new DateTime(Calendar.getInstance().getTime());
				
				//CardioRisk cR = new RiskAnalyser().calculateRiskFactor(user, sensorData );
				
				List<Eventdate> eventList = RiskIndexUtils.convert(sensorData);		
				
				CardioRisk cR = new RiskAnalyser().calculateRiskFactor(user, eventList, null, null );
				cR.setEventData(eventList);
				
				e = new DateTime(Calendar.getInstance().getTime());
				//System.out.println("=== Cardio risk computed in: " + Seconds.secondsBetween(b, e).getSeconds() % 60 + " seconds");
				return cR;
			}
			else
				return new CardioRisk();
        }else{
        	 //System.out.println("=== days: " + noOfDays + " computing UserAverage ..");
        	 ObjectMapper dailyAvg = new ObjectMapper();
        	 String dailyAvgJsonString = user.getMedicalNotes().getDailyAvg();
        	 if(null!=dailyAvgJsonString){
	        	 try {
	        		 UserAverage uAV = dailyAvg.readValue(dailyAvgJsonString, UserAverage.class);
	        		 if(user!=null && uAV!=null){
	        			 CardioRisk cR = new RiskAnalyser().calculateRiskFactor(user, uAV.getEventdate(), from, to );
	        			 return cR;
	        		 }else
	     				return new CardioRisk();
				} catch (Exception ex){
				}
        	 }
        }
        return new CardioRisk();
	}
    
}
