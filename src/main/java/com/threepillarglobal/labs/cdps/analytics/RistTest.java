package com.threepillarglobal.labs.cdps.analytics;


public class RistTest {

	
	public static void main(String[] args) {
		System.out.println(RiskIndexUtils.getAgeRiskIndex(Integer.valueOf(73)));
		System.out.println(RiskIndexUtils.getSmokeIndex(RiskIndexUtils.SMOKER.HEAVY));
		System.out.println(RiskIndexUtils.getLowBloodPressureRiskIndex(Integer.valueOf(95)));
		System.out.println(RiskIndexUtils.getHighBloodPressureRiskIndex(Integer.valueOf(155)));
		//
		//checkRisk();
	}

	
	/*private static void checkRisk(){
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
	}*/
	
	
}
