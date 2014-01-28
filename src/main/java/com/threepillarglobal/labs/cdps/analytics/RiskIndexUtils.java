package com.threepillarglobal.labs.cdps.analytics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import com.threepillarglobal.labs.cdps.domain.Eventdate;
import com.threepillarglobal.labs.cdps.domain.HourlyData;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;

public class RiskIndexUtils {
	
	private static NavigableMap<Double,String> STROKE_RISK =
	        new TreeMap<Double, String>();
	private static NavigableMap<Integer,Integer> AGE_INDEX_THRESHOLDS =
	        new TreeMap<Integer, Integer>();
	private static NavigableMap<Double,Integer> LBP_INDEX_THRESHOLDS =
	        new TreeMap<Double, Integer>();
	private static NavigableMap<Double,Integer> HBP_INDEX_THRESHOLDS =
	        new TreeMap<Double, Integer>();
	private static Map<User.MedicalNotes.SMOKER,Integer> SMOKE_THRESHOLDS =
	        new HashMap<User.MedicalNotes.SMOKER, Integer>();
	private static NavigableMap<User.MedicalNotes.INHERITED_RISK,Integer> INHERITED_RISKS_INDEX =
	        new TreeMap<User.MedicalNotes.INHERITED_RISK, Integer>();
	
	/*
	 * Stroke risk factor
	 */
    static {
        STROKE_RISK.put(0.0, "LOW");
        STROKE_RISK.put(4.0, "MEDIUM");
        STROKE_RISK.put(6.0, "HIGH");
        STROKE_RISK.put(99.0, "IMMINENT");
    }
    
	/*
    Age		age_idx value:
	0-25yr 		1
	25-40yr		3
	40-55		4
	55-75		7
	75+		    9
	 */
    static {
        AGE_INDEX_THRESHOLDS.put(0,  1);
        AGE_INDEX_THRESHOLDS.put(25, 3);
        AGE_INDEX_THRESHOLDS.put(40, 4);
        AGE_INDEX_THRESHOLDS.put(55, 7);
        AGE_INDEX_THRESHOLDS.put(75, 9);
    }
    
    /*
     Smoking group:	smoking_idx value
	non smoker		0
	casual smoker*	5
	heavy smoker**	8
	passive smoker	3
     */
    static {
    	SMOKE_THRESHOLDS.put(User.MedicalNotes.SMOKER.NO,  0);
    	SMOKE_THRESHOLDS.put(User.MedicalNotes.SMOKER.CASUAL,  5);
    	SMOKE_THRESHOLDS.put(User.MedicalNotes.SMOKER.HEAVY,  8);
    	SMOKE_THRESHOLDS.put(User.MedicalNotes.SMOKER.PASSIVE,  3);
    }
    
    /*
    lbp/diastolic values			blood_pressure_idx
	60  				 1	
	75	 				 3		
	90	 				 4
	100					 7
    */
    static {
    	LBP_INDEX_THRESHOLDS.put(0.0,  0);
    	LBP_INDEX_THRESHOLDS.put(60.0,  1);
    	LBP_INDEX_THRESHOLDS.put(75.0,  3);
    	LBP_INDEX_THRESHOLDS.put(90.0,  4);
    	LBP_INDEX_THRESHOLDS.put(100.0,  7);
    	LBP_INDEX_THRESHOLDS.put(110.0,  9);
    	LBP_INDEX_THRESHOLDS.put(111.0,  10);
    }
    
    /*
    hbp/systolic values			blood_pressure_idx
	110  				1	
	120	 				3		
	130	 				4
	150					7
	150+				9
    */
    static {
    	HBP_INDEX_THRESHOLDS.put(0.0,    0);
    	HBP_INDEX_THRESHOLDS.put(110.0,  1);
    	HBP_INDEX_THRESHOLDS.put(120.0,  3);
    	HBP_INDEX_THRESHOLDS.put(130.0,  4);
    	HBP_INDEX_THRESHOLDS.put(150.0,  7);
    	HBP_INDEX_THRESHOLDS.put(151.0,  9);
    	HBP_INDEX_THRESHOLDS.put(161.0,  10);
    }
    
    /**
     * Inherited risks
     */
    static{
    	INHERITED_RISKS_INDEX.put(User.MedicalNotes.INHERITED_RISK.LOW, 1);
    	INHERITED_RISKS_INDEX.put(User.MedicalNotes.INHERITED_RISK.MEDIUM, 3);
    	INHERITED_RISKS_INDEX.put(User.MedicalNotes.INHERITED_RISK.HIGH, 5);
    }
    
    public static Integer getAgeRiskIndex(Integer age){
    	return AGE_INDEX_THRESHOLDS.get(AGE_INDEX_THRESHOLDS.floorKey(age));
    }
    
    public static Integer getSmokeIndex(User.MedicalNotes.SMOKER smoker){
    	return SMOKE_THRESHOLDS.get(smoker);
    }
    
    public static Integer getLowBloodPressureRiskIndex(Double lbpVal){
    	return LBP_INDEX_THRESHOLDS.get(LBP_INDEX_THRESHOLDS.floorKey(lbpVal));
    }
    
    public static Integer getHighBloodPressureRiskIndex(Double hbpVal){
    	return HBP_INDEX_THRESHOLDS.get(HBP_INDEX_THRESHOLDS.floorKey(hbpVal));
    }
    
    public static Integer getInheritedRiskIndex(User.MedicalNotes.INHERITED_RISK inheritedRisk){
    	return INHERITED_RISKS_INDEX.get(inheritedRisk);
    }
	
    public static String getStrokeRisk(Double dbl){
    	return STROKE_RISK.get(STROKE_RISK.floorKey(dbl));
    }
    
    /**
     * unify UI
     * @param sdList
     * @return
     */
    public static List<Eventdate> convert(List<SensorData> sdList){
    	List<Eventdate> edList = null;
    	if(sdList!=null && !sdList.isEmpty()){
    		edList = new ArrayList<Eventdate>();
	    	for (SensorData sd : sdList) {	    		
				hourlyDataToEvent(edList, sd.getH00(), sd.getEventDate(), 0);
				hourlyDataToEvent(edList, sd.getH01(), sd.getEventDate(), 1);
				hourlyDataToEvent(edList, sd.getH02(), sd.getEventDate(), 2);
				hourlyDataToEvent(edList, sd.getH03(), sd.getEventDate(), 3);
				hourlyDataToEvent(edList, sd.getH04(), sd.getEventDate(), 4);
				hourlyDataToEvent(edList, sd.getH05(), sd.getEventDate(), 5);
				hourlyDataToEvent(edList, sd.getH06(), sd.getEventDate(), 6);
				hourlyDataToEvent(edList, sd.getH07(), sd.getEventDate(), 7);
				hourlyDataToEvent(edList, sd.getH08(), sd.getEventDate(), 8);
				hourlyDataToEvent(edList, sd.getH09(), sd.getEventDate(), 9);
				hourlyDataToEvent(edList, sd.getH10(), sd.getEventDate(), 10);
				hourlyDataToEvent(edList, sd.getH11(), sd.getEventDate(), 11);
				hourlyDataToEvent(edList, sd.getH12(), sd.getEventDate(), 12);
				hourlyDataToEvent(edList, sd.getH13(), sd.getEventDate(), 13);
				hourlyDataToEvent(edList, sd.getH14(), sd.getEventDate(), 14);
				hourlyDataToEvent(edList, sd.getH15(), sd.getEventDate(), 15);
				hourlyDataToEvent(edList, sd.getH16(), sd.getEventDate(), 16);
				hourlyDataToEvent(edList, sd.getH17(), sd.getEventDate(), 17);
				hourlyDataToEvent(edList, sd.getH18(), sd.getEventDate(), 18);
				hourlyDataToEvent(edList, sd.getH19(), sd.getEventDate(), 19);
				hourlyDataToEvent(edList, sd.getH20(), sd.getEventDate(), 20);
				hourlyDataToEvent(edList, sd.getH21(), sd.getEventDate(), 21);
				hourlyDataToEvent(edList, sd.getH22(), sd.getEventDate(), 22);
				hourlyDataToEvent(edList, sd.getH23(), sd.getEventDate(), 23);
			}
    	}
    	return edList;
    }

	private static void hourlyDataToEvent(List<Eventdate> edList, HourlyData hd, Date d, int offset) {
		if(hd!=null){
			Eventdate ed = new Eventdate();
			ed.setEventdate(dateHourInc(d,offset));
			ed.setSystolicPressure(hd.getSystolicPressure());
			ed.setDiastolicPressure(hd.getDiastolicPressure());
			edList.add(ed);
		}
	}
    
    private static final long hoursInMillis = 60L * 60L * 1000L;
    private static Date dateHourInc(Date inDate, int h){
    	return new Date(inDate.getTime() + 
                (h * hoursInMillis));
    }
   
    
    
}
