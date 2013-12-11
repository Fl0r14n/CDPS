package com.threepillarglobal.labs.cdps.domain;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.joda.time.DateTime;
import org.joda.time.Period;

import com.threepillarglobal.labs.cdps.analytics.RiskIndexUtils;

@Getter
@AllArgsConstructor
public class CardioRisk {

	private   Integer ageIndex;
	private   Integer smokingIndex;
	private   Integer inheritanceIndex;
	private  Integer lbPIndex;
	private  Integer hbPIndex;
	//TODO
	private  Integer cholesterolIndex;
	private  Integer triglyceridesIndex;
	private  Integer bmsIndex;
	private  Integer excerciseIndex;
	private  Integer caloriesIndex;
	private  Integer drinkIntakeIndex;
	
	private  Double riskFactor;
	
	public CardioRisk(){}
	
	public  Double calculateRiskFactor(User user, List<SensorData> sensorDataList){
		int age = new Period( new DateTime(Calendar.getInstance().getTime()), new DateTime(user.getPersonalData().getDob())).getYears();
		ageIndex = RiskIndexUtils.getAgeRiskIndex(age);
		smokingIndex = RiskIndexUtils.getSmokeIndex(user.getMedicalNotes().getSmoker());
		inheritanceIndex = RiskIndexUtils.getInheritedRiskIndex(user.getMedicalNotes().getInheritedRisk());
		
		double meanLbpDailyValue = getBpMeanValue(sensorDataList, "getSystolicPressure");
		lbPIndex = RiskIndexUtils.getLowBloodPressureRiskIndex(meanLbpDailyValue);
		double meanHbpDailyValue = getBpMeanValue(sensorDataList, "getDiastolicPressure");
		hbPIndex = RiskIndexUtils.getLowBloodPressureRiskIndex(meanHbpDailyValue);
				
		double [] riskValues = {ageIndex, smokingIndex, inheritanceIndex, lbPIndex, hbPIndex};
		double [] weights = {0.3, 0.2, 0.2, 0.15, 0.15};
		
		riskFactor = new Mean().evaluate(riskValues, weights); 
		return riskFactor;
	} 
	
	
	@SuppressWarnings("unchecked")
	private  double getDailyBpMeanValue(SensorData sD, String metricName){
		  List<Double> values = new ArrayList<Double>();	
		  Method[] methods = sD.getClass().getMethods();
		  try {
			  for(Method method : methods){
				  if(!method.getName().startsWith("getG")) {
					  HourlyData data = (HourlyData)method.invoke(sD, null);
					  Method m = data.getClass().getMethod(metricName, null);
					  values.add(Double.valueOf((Double)m.invoke(data, null)));
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
			values.add(getDailyBpMeanValue(sD, metricName));
		}
		Double[] ds = values.toArray(new Double[values.size()]);
		double[] d = ArrayUtils.toPrimitive(ds);
		return new Mean().evaluate(d);
	}
	
}
