package com.threepillarglobal.labs.cdps.analytics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;

import com.threepillarglobal.labs.cdps.web.SensorData;

public class RistTest {

	private static String colChartData = "[{\"16\":{\"10\":{\"141\":\"63.535226402281\",\"31241\":\"63.4552264022809\"},\"11\":{\"1241\":\"33.3652264022809\",\"311\":\"633.2952264022808\"},\"59\":{\"1241\":\"625.695226402266\",\"31241\":\"625.6152264022669\"},\"08\":{\"1241\":\"633.8552264022811\",\"31241\":\"633.6652264022811\"},\"09\":{\"1241\":\"633.695226402281\",\"31241\":\"633.615226402281\"},\"04\":{\"1241\":\"634.4952264022814\",\"31241\":\"634.4152264022814\"},\"05\":{\"1241\":\"634.3352264022814\",\"31241\":\"634.2552264022813\"},\"06\":{\"1241\":\"634.1652264022813\",\"31241\":\"634.0952264022812\"},\"06\":{\"1241\":\"634.0152264022812\",\"31241\":\"633.9352264022812\"},\"00\":{\"1241\":\"635.1352264022818\",\"31241\":\"635.0552264022816\"},\"01\":{\"1241\":\"634.9652264022816\",\"31241\":\"634.8952264022816\"},\"02\":{\"1241\":\"634.8152264022816\",\"31241\":\"634.6352264022816\"},\"03\":{\"1241\":\"634.6552264022815\",\"31241\":\"634.5652264022815\"}},\"19\":{\"10\":{\"1241\":\"614.4152264022694\",\"31241\":\"614.3352264022693\"},\"11\":{\"1241\":\"614.2552264022693\",\"31241\":\"614.1652264022692\"},\"12\":{\"1241\":\"614.0952264022692\",\"31241\":\"614.0152264022692\"},\"13\":{\"1241\":\"613.9352264022691\",\"31241\":\"613.8552264022691\"},\"14\":{\"1241\":\"613.665226402269\",\"31241\":\"613.695226402269\"},\"15\":{\"1241\":\"613.615226402269\",\"31241\":\"613.5352264022689\"},\"02\":{\"1241\":\"615.69522640228\",\"31241\":\"615.61522640228\"},\"03\":{\"1241\":\"615.5352264022699\",\"31241\":\"615.4552264022699\"}},\"day\":\"2013-12-05\"},{\"16\":{\"10\":{\"1241\":\"933.535226402281\",\"31241\":\"633.4552264022809\"},\"11\":{\"1241\":\"633.3652264022809\",\"31241\":\"233.2952264022808\"},\"59\":{\"1241\":\"325.695226402266\",\"31241\":\"625.6152264022669\"},\"08\":{\"1241\":\"433.8552264022811\",\"31241\":\"633.6652264022811\"},\"09\":{\"1241\":\"633.695226402281\",\"31241\":\"633.615226402281\"},\"04\":{\"1241\":\"634.4952264022814\",\"31241\":\"634.4152264022814\"},\"05\":{\"1241\":\"634.3352264022814\",\"31241\":\"634.2552264022813\"},\"06\":{\"1241\":\"634.1652264022813\",\"31241\":\"634.0952264022812\"},\"06\":{\"1241\":\"634.0152264022812\",\"31241\":\"633.9352264022812\"},\"00\":{\"1241\":\"635.1352264022818\",\"31241\":\"635.0552264022816\"},\"01\":{\"1241\":\"634.9652264022816\",\"31241\":\"634.8952264022816\"},\"02\":{\"1241\":\"4.8152264022816\",\"31241\":\"634.6352264022816\"},\"03\":{\"1241\":\"634.6552264022815\",\"31241\":\"634.5652264022815\"}},\"19\":{\"10\":{\"1241\":\"994.4152264022694\",\"31241\":\"614.3352264022693\"},\"11\":{\"1241\":\"614.2552264022693\",\"31241\":\"614.1652264022692\"},\"12\":{\"1241\":\"614.0952264022692\",\"31241\":\"614.0152264022692\"},\"13\":{\"1241\":\"613.9352264022691\",\"31241\":\"613.8552264022691\"},\"14\":{\"1241\":\"613.665226402269\",\"31241\":\"613.695226402269\"},\"15\":{\"1241\":\"613.615226402269\",\"31241\":\"653.5352264022689\"},\"02\":{\"1241\":\"515.69522640228\",\"31241\":\"15.61522640228\"},\"03\":{\"1241\":\"215.5352264022699\",\"31241\":\"188.4552264022699\"}},\"day\":\"2013-12-06\"}]";
	
	public static void main(String[] args) {
		System.out.println(RiskIndexUtils.getAgeRiskIndex(Integer.valueOf(73)));
		System.out.println(RiskIndexUtils.getSmokeIndex(RiskIndexUtils.SMOKER.HEAVY));
		System.out.println(RiskIndexUtils.getLowBloodPressureRiskIndex(Integer.valueOf(95)));
		System.out.println(RiskIndexUtils.getHighBloodPressureRiskIndex(Integer.valueOf(155)));
		//
		checkRisk();
	}

	
	private static void checkRisk(){
		String[] activityType = {"Eating", "Sleeping", "Exercising"};
		List<List<String>> dailyData = new ArrayList<>();
		int activityDuration;
		String at;
	    Random rand = new Random();
	    int avgHR, avgBPLow, avgBPHigh, calories;
        Date eventDate = DateUtils.addDays(Calendar.getInstance().getTime(), 1);
        
        for(int k = 0; k < 24; k++)
        {
            List<String> l = new ArrayList<>();
            activityDuration = rand.nextInt(61);
            at = activityType[rand.nextInt(3)];
            avgHR = rand.nextInt(200);
            avgBPLow = 50 + rand.nextInt(100);
            avgBPHigh = 100 + rand.nextInt(200);
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
        
        SensorData wSD = new SensorData(sd);
        System.out.println(wSD.toJSONString());
	}
	
	
}
