package com.threepillarglobal.labs.cdps.analytics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.joda.time.DateTime;
import org.joda.time.Period;

import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.HourlyData;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;

public class RiskAnalyser {

	
	public  CardioRisk  calculateRiskFactor(User user, List<SensorData> sensorDataList){
		
		CardioRisk cardioRisk = new CardioRisk();
		
		cardioRisk.setSensorData(sensorDataList);
	
		int age = new Period( new DateTime(Calendar.getInstance().getTime()), new DateTime(user.getPersonalData().getDob())).getYears();
		
		Integer ageIndex = RiskIndexUtils.getAgeRiskIndex(age);
		cardioRisk.setAgeIndex(ageIndex);
		Integer smokingIndex = RiskIndexUtils.getSmokeIndex(user.getMedicalNotes().getSmoker());
		cardioRisk.setSmokingIndex (smokingIndex);
		Integer inheritanceIndex = RiskIndexUtils.getInheritedRiskIndex(user.getMedicalNotes().getInheritedRisk());
		cardioRisk.setInheritanceIndex (inheritanceIndex);
		
		double meanLbpDailyValue = getBpMeanValue(sensorDataList, "getSystolicPressure");
		Integer diastolicIndex = RiskIndexUtils.getLowBloodPressureRiskIndex(meanLbpDailyValue);
		cardioRisk.setDiastolicIndex (diastolicIndex);
		double meanHbpDailyValue = getBpMeanValue(sensorDataList, "getDiastolicPressure");
		Integer systolicIndex = RiskIndexUtils.getLowBloodPressureRiskIndex(meanHbpDailyValue);
		cardioRisk.setSystolicIndex(systolicIndex);

		//System.out.println("# data load: " + sensorDataList.size() + ageIndex + " - " +  smokingIndex + " - " +   inheritanceIndex + " - " +   diastolicIndex + " - " +   systolicIndex);
		
		double [] riskValues = {ageIndex, smokingIndex, inheritanceIndex, diastolicIndex, systolicIndex};
		double [] weights = {0.3, 0.2, 0.2, 0.15, 0.15};
		
		double riskFactor = new Mean().evaluate(riskValues, weights);
		cardioRisk.setRiskFactor(riskFactor);
		cardioRisk.setStrokeRisk (RiskIndexUtils.getStrokeRisk(riskFactor));
		
		return cardioRisk;
	} 
	
	
	@SuppressWarnings("unchecked")
	private  double getDailyBpMeanValue(SensorData sD, String metricName){
		  List<Double> values = new ArrayList<Double>();	
		  Method[] methods = SensorData.class.getMethods();
		  try {
			  for(Method method : methods){
				  if(method.getName().startsWith("get") && !method.getName().equals("getClass") && !method.getName().equals("getEventDate") ) {
					  HourlyData data = (HourlyData)method.invoke(sD, null);
					  Method m = HourlyData.class.getMethod(metricName, null);
					  Object invoke = m.invoke(data, null);
					  if(invoke instanceof Integer){
						  values.add(Double.valueOf((Integer)invoke));
					  }
				  }
			}		
		  }catch(Exception ex){
		  }
		  Double[] ds = values.toArray(new Double[values.size()]);
		  double[] d = ArrayUtils.toPrimitive(ds);
		  return new Mean().evaluate(d);
	}

	private  double getBpMeanValue(List<SensorData> sDList,  String metricName){
		List<Double> values = new ArrayList<Double>();
		for(SensorData sD: sDList){
			double dailyBpMeanValue = getDailyBpMeanValue(sD, metricName);
			values.add(dailyBpMeanValue);
		}
		Double[] ds = values.toArray(new Double[values.size()]);
		double[] d = ArrayUtils.toPrimitive(ds);
		return new Mean().evaluate(d);
	}
}
