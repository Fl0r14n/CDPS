package com.threepillarglobal.labs.cdps.service.mock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;

import com.threepillarglobal.labs.cdps.domain.HourlyData;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.MedicalNotes.INHERITED_RISK;

public class MockDataGenerator {

	
	protected static List<SensorData> generateSensorData(int rangeFrom, int rangeTo, Date startDate, Date endDate){
		
		
		String[] activityType = {"Eating", "Sleeping", "Exercising"};
		 List<SensorData> sdList = new ArrayList<SensorData>();
		 int activityDuration;
	        Random rand = new Random();
	        int avgHR, diastolicBP, systolicBP, calories;
	        String currentActivityType = null;
	        int daysInInterval2 = getDaysInInterval(startDate, endDate);
			int daysInInterval = daysInInterval2>0 ? daysInInterval2  : 1;
	        for (int i = 0; i < daysInInterval; i++) {
	            //for (int j = rangeFrom; j <= rangeTo; j++) {
	                SensorData sd;

	                List<HourlyData> dailyData = new ArrayList<>();
	                Date eventDate = DateUtils.addDays(startDate, i);

	                for (int k = 0; k < 24; k++) {
	                    HourlyData hh;
	                    activityDuration = rand.nextInt(61);
	                    currentActivityType = activityType[rand.nextInt(3)];
	                    avgHR = rand.nextInt(200);
	                    diastolicBP = 50 + rand.nextInt(100);
	                    systolicBP = 100 + rand.nextInt(200);
	                    calories = rand.nextInt(3000);
	                    hh = new HourlyData(currentActivityType, activityDuration, avgHR, systolicBP, diastolicBP, calories);
	                    dailyData.add(hh);
	                }
	                sd = new SensorData(eventDate, dailyData.get(0), dailyData.get(1), dailyData.get(2),
	                        dailyData.get(3), dailyData.get(4), dailyData.get(5),
	                        dailyData.get(6), dailyData.get(7), dailyData.get(8),
	                        dailyData.get(9), dailyData.get(10), dailyData.get(11),
	                        dailyData.get(12), dailyData.get(13), dailyData.get(14),
	                        dailyData.get(15), dailyData.get(16), dailyData.get(17),
	                        dailyData.get(18), dailyData.get(19), dailyData.get(20),
	                        dailyData.get(21), dailyData.get(22), dailyData.get(23)
	                );
	                sdList.add(sd);
	            //}
	        }
	                
		return sdList;
	}
	
	
	 protected static int getDaysInInterval(Date startDate, Date endDate) {
		return Days.daysBetween(new DateTime(startDate), new DateTime(endDate)).getDays() +1;
	 }
	 
	
	
	private static String emailPattern = "user<ID>@3pg.com";
    private static String namePrefix = "John Doe #";
    private static String[] location = {"Romania|Cluj|Cluj-Napoca", "Romania|Timisoara|Timisoara", "USA|Washington|Washington DC"};
    
	public static List<User> fetchMockUserData(int numberOfUsers) {
	        String rowKey;
	        String secretKey = "sk";
	        Boolean userStatus = true;
	        String userPhone = "555-";
	        Date dob = new Date();
	        //UserRepository userRepo = new UserRepository();
	        User.AccountData accData;
	        User.PersonalData persData;
	        User.MedicalNotes medNotes;
	        Random rand = new Random();
	        int locationID;
	        Boolean smoker;
	        List<User> userList = new ArrayList<User>();
	        for (int i = 1; i <= numberOfUsers; i++) {
	        	
	            rowKey = emailPattern.replace("<ID>", Integer.toString(i));
	            //account data
	            secretKey = secretKey + Integer.toString(i);
	            userPhone = userPhone + Integer.toString(i);
	            accData = new User.AccountData(secretKey, userStatus, userPhone);
	            //userRepo.saveAccountData(rowKey, accData);
	            //personal data
	            namePrefix = namePrefix + Integer.toString(i);
	            locationID = rand.nextInt(location.length);
	            persData = new User.PersonalData(namePrefix, dob, location[locationID]);
	            //userRepo.savePersonalData(rowKey, persData);
	            //medical notes
	            smoker = rand.nextBoolean();
	            medNotes = new User.MedicalNotes(null, User.MedicalNotes.SMOKER.CASUAL, INHERITED_RISK.HIGH);
	            //userRepo.saveMedicalNotes(rowKey, medNotes);
	            //TODO: family tree
	            User user = new User(rowKey, accData, persData, medNotes);
	            userList.add(user);
	        }
	        return userList;
	}
	 
	@SuppressWarnings("finally")
	public static List<SensorData> fetchMockSensorData(Date sDate, Date eDate){		
		return generateSensorData(0, 1, sDate, eDate);

	}
}
