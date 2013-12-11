package com.threepillarglobal.labs.cdps.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;

public class MockDataGenerator {

	
	protected static SensorData generateSensorData(Date date){
		String[] activityType = {"Eating", "Sleeping", "Exercising"};
		List<List<String>> dailyData = new ArrayList<>();
		int activityDuration;
		String at;
	    Random rand = new Random();
	    int avgHR, avgBPLow, avgBPHigh, calories;
        Date eventDate = DateUtils.addDays(date, 1);
        
        for(int k = 0; k < 24; k++)
        {
            List<String> l = new ArrayList<>();
            activityDuration = rand.nextInt(61);
            at = activityType[rand.nextInt(3)];
            avgHR = rand.nextInt(200);
            avgBPLow = 65 + rand.nextInt(50);
            avgBPHigh = avgBPLow + rand.nextInt(50);
            calories = rand.nextInt(3000);
            l.add(at);
            l.add(Integer.toString(activityDuration));
            l.add(Integer.toString(avgHR));
            l.add(Integer.toString(avgBPLow) + "/" + Integer.toString(avgBPHigh));
            l.add(Integer.toString(calories));
            dailyData.add(l);
        }
        
        com.threepillarglobal.labs.cdps.domain.SensorData sd = new com.threepillarglobal.labs.cdps.domain.SensorData(eventDate, dailyData.get(0),dailyData.get(1),dailyData.get(2),
                            dailyData.get(3),dailyData.get(4),dailyData.get(5),
                            dailyData.get(6),dailyData.get(7),dailyData.get(8),
                            dailyData.get(9),dailyData.get(10),dailyData.get(11),
                            dailyData.get(12),dailyData.get(13),dailyData.get(14),
                            dailyData.get(15),dailyData.get(16),dailyData.get(17),
                            dailyData.get(18),dailyData.get(19),dailyData.get(20),
                            dailyData.get(21),dailyData.get(22),dailyData.get(23)
        );
        
        return new SensorData(sd);
        //System.out.println(wSD.toJSONString());
	}
	
	protected static List<User> fetchMockUserData(){
		List<User> data = new ArrayList<User>();
		data.add(new User("cpaun", "Cristian Paun", "03.07.1907"));
		data.add(new User("fchis", "Florian Chis",  "04.08.1997"));
		data.add(new User("soros", "Sebastian Oros", "23.11.2907"));
		data.add(new User("dmatei", "Dorel Matei", "04.08.1997"));
		data.add(new User("girish", "Girish Kumar",  "04.08.1997"));
		data.add(new User("mbanici","Marius Banici",  "04.08.1997"));
		data.add(new User("nsurdu", "Nicu Surdu",  "04.08.1997"));
		data.add(new User("poprea", "Paul Oprea",  "04.08.1997"));
		return data;
	}
	
	protected static List<SensorData> fetchMockSensorData(){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		List<SensorData> sdList = new ArrayList<SensorData>();
		try{
			for(int i=1; i<32; i++){
			sdList.add(MockDataGenerator.generateSensorData(dateFormat.parse(i+"-11-2012")));
			/*sdList.add(MockDataGenerator.generateSensorData(dateFormat.parse("12-11-2012")));
			sdList.add(MockDataGenerator.generateSensorData(dateFormat.parse("13-11-2012")));
			sdList.add(MockDataGenerator.generateSensorData(dateFormat.parse("14-11-2012")));
			sdList.add(MockDataGenerator.generateSensorData(dateFormat.parse("15-11-2012")));
			sdList.add(MockDataGenerator.generateSensorData(dateFormat.parse("16-11-2012")));*/
			}
		}catch(Exception ex){}
		return sdList;
	}
}
