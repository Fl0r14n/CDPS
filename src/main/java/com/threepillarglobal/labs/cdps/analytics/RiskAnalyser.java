package com.threepillarglobal.labs.cdps.analytics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.joda.time.DateTime;
import org.joda.time.Period;

import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.Eventdate;
import com.threepillarglobal.labs.cdps.domain.HourlyData;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.MedicalNotes;

public class RiskAnalyser {

	/**
	 * Computes risk factor based on  User->MedicalNotes->dailyAvg Json data
	 * @param user
	 * @param eventdate
	 * @param from
	 * @param to
	 * @return
	 */
	public static CardioRisk  calculateRiskFactor(User user, List<Eventdate> eventdate, Date from, Date to){
		
		CardioRisk cardioRisk = new CardioRisk();
		
		List<Eventdate> inRangeList = new ArrayList<Eventdate>();
		
		if(from!=null && to!=null){
			for (Eventdate eventdate2 : eventdate) {
				if ( (eventdate2.getEventdate().after(from)) && (eventdate2.getEventdate().before(to) ))
					inRangeList.add(eventdate2);
			}
		}else{
			inRangeList = eventdate;
		}
		
		cardioRisk.setEventData(inRangeList);
	
		int age = new Period( new DateTime(Calendar.getInstance().getTime()), new DateTime(user.getPersonalData().getDob())).getYears();
		
		Integer ageIndex = RiskIndexUtils.getAgeRiskIndex(age);
		cardioRisk.setAgeIndex(ageIndex);
		MedicalNotes medicalNotes = user.getMedicalNotes();
		Integer smokingIndex =0, inheritanceIndex = 0;
		if(medicalNotes!=null){
			smokingIndex = RiskIndexUtils.getSmokeIndex(medicalNotes.getSmoker());
			cardioRisk.setSmokingIndex (smokingIndex);
			inheritanceIndex = RiskIndexUtils.getInheritedRiskIndex(medicalNotes.getInheritedRisk());
			cardioRisk.setInheritanceIndex (inheritanceIndex);
		}
		
		double meanLbpDailyValue = getAvgBpMeanValue(inRangeList, "getSystolicPressure");
		
		Integer diastolicIndex = RiskIndexUtils.getLowBloodPressureRiskIndex(meanLbpDailyValue);
		cardioRisk.setDiastolicIndex (diastolicIndex);
		double meanHbpDailyValue = getAvgBpMeanValue(inRangeList, "getDiastolicPressure");
		
		Integer systolicIndex = RiskIndexUtils.getLowBloodPressureRiskIndex(meanHbpDailyValue);
		cardioRisk.setSystolicIndex(systolicIndex);

		//System.out.println("# data load: " + inRangeList.size() + ageIndex + " - " +  smokingIndex + " - " +   inheritanceIndex + " - " +   diastolicIndex + " - " +   systolicIndex);
		
		double [] riskValues = {ageIndex, smokingIndex, inheritanceIndex, diastolicIndex, systolicIndex};
		double [] weights = {0.3, 0.2, 0.2, 0.15, 0.15};
		
		double riskFactor = new Mean().evaluate(riskValues, weights);
		cardioRisk.setRiskFactor(riskFactor);
		cardioRisk.setStrokeRisk (RiskIndexUtils.getStrokeRisk(riskFactor));
		
		return cardioRisk;		
	}
	
	public static  double getAvgBpMeanValue(List<Eventdate> evendList,  String metricName){
		List<Double> values = new ArrayList<Double>();
		for(Eventdate eD: evendList){			
			double dailyBpMeanValue = getAvgDailyBpMeanValue(eD, metricName);
			values.add(dailyBpMeanValue);
		}
		Double[] ds = values.toArray(new Double[values.size()]);
		double[] d = ArrayUtils.toPrimitive(ds);
		return new Mean().evaluate(d);
	}
	
	@SuppressWarnings("unchecked")
	public static  double getAvgDailyBpMeanValue(Eventdate sD, String metricName){
		  List<Double> values = new ArrayList<Double>();	
		  Method[] methods = Eventdate.class.getMethods();
		  try {
			  for(Method method : methods){
				  if(method.getName().startsWith("get") && !method.getName().equals("getClass") && !method.getName().equals("getEventdate") && method.getName().equals(metricName) ) {
					  
					  Method m = Eventdate.class.getMethod(metricName, null);
					  Object invoke = m.invoke(sD, null);
					  
					  if(invoke!=null){
						  values.add(Double.valueOf((Integer)invoke));
					  }
				  }
			}		
		  }catch(Exception ex){
			  ex.printStackTrace();
		  }
		  Double[] ds = values.toArray(new Double[values.size()]);
		  double[] d = ArrayUtils.toPrimitive(ds);
		  return new Mean().evaluate(d);
	}
	
	//--------------------------------solely used to flatten user's Sensor data into JSON via User->MedicalNotes->dailyAvg
	
	public  CardioRisk  calculateRiskFactor(User user, List<SensorData> sensorDataList){
		
		CardioRisk cardioRisk = new CardioRisk();
		
		cardioRisk.setSensorData(sensorDataList);
	
		int age = new Period( new DateTime(Calendar.getInstance().getTime()), new DateTime(user.getPersonalData().getDob())).getYears();
		
		Integer ageIndex = RiskIndexUtils.getAgeRiskIndex(age);
		cardioRisk.setAgeIndex(ageIndex);
		MedicalNotes medicalNotes = user.getMedicalNotes();
		Integer smokingIndex =0, inheritanceIndex = 0;
		if(medicalNotes!=null){
			smokingIndex = RiskIndexUtils.getSmokeIndex(medicalNotes.getSmoker());
			cardioRisk.setSmokingIndex (smokingIndex);
			inheritanceIndex = RiskIndexUtils.getInheritedRiskIndex(medicalNotes.getInheritedRisk());
			cardioRisk.setInheritanceIndex (inheritanceIndex);
		}
		
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
	public static  double getDailyBpMeanValue(SensorData sD, String metricName){
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

	public static  double getBpMeanValue(List<SensorData> sDList,  String metricName){
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
