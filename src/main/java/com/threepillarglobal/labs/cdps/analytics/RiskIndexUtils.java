package com.threepillarglobal.labs.cdps.analytics;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class RiskIndexUtils {

	public enum SMOKER {NO, CASUAL, HEAVY, PASSIVE}
	
	private static NavigableMap<Integer,Integer> AGE_INDEX_THRESHOLDS =
	        new TreeMap<Integer, Integer>();
	private static NavigableMap<Integer,Integer> LBP_INDEX_THRESHOLDS =
	        new TreeMap<Integer, Integer>();
	private static NavigableMap<Integer,Integer> HBP_INDEX_THRESHOLDS =
	        new TreeMap<Integer, Integer>();
	private static Map<SMOKER,Integer> SMOKE_THRESHOLDS =
	        new HashMap<SMOKER, Integer>();
	
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
    	SMOKE_THRESHOLDS.put(SMOKER.NO,  0);
    	SMOKE_THRESHOLDS.put(SMOKER.CASUAL,  5);
    	SMOKE_THRESHOLDS.put(SMOKER.HEAVY,  8);
    	SMOKE_THRESHOLDS.put(SMOKER.PASSIVE,  3);
    }
    
    /*
    lbp/diastolic values			blood_pressure_idx
	60  				 1	
	75	 				 3		
	90	 				 4
	100					 7
    */
    static {
    	LBP_INDEX_THRESHOLDS.put(0,  0);
    	LBP_INDEX_THRESHOLDS.put(60,  1);
    	LBP_INDEX_THRESHOLDS.put(75,  3);
    	LBP_INDEX_THRESHOLDS.put(90,  4);
    	LBP_INDEX_THRESHOLDS.put(100,  7);
    	LBP_INDEX_THRESHOLDS.put(110,  9);
    	LBP_INDEX_THRESHOLDS.put(111,  10);
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
    	HBP_INDEX_THRESHOLDS.put(0,    0);
    	HBP_INDEX_THRESHOLDS.put(110,  1);
    	HBP_INDEX_THRESHOLDS.put(120,  3);
    	HBP_INDEX_THRESHOLDS.put(130,  4);
    	HBP_INDEX_THRESHOLDS.put(150,  7);
    	HBP_INDEX_THRESHOLDS.put(151,  9);
    	HBP_INDEX_THRESHOLDS.put(161,  10);
    }
    
    public static Integer getAgeRiskIndex(Integer age){
    	return AGE_INDEX_THRESHOLDS.get(AGE_INDEX_THRESHOLDS.floorKey(age));
    }
    
    public static Integer getSmokeIndex(SMOKER smoker){
    	return SMOKE_THRESHOLDS.get(smoker);
    }
    
    public static Integer getLowBloodPressureRiskIndex(Integer lbpVal){
    	return LBP_INDEX_THRESHOLDS.get(LBP_INDEX_THRESHOLDS.floorKey(lbpVal));
    }
    
    public static Integer getHighBloodPressureRiskIndex(Integer hbpVal){
    	return HBP_INDEX_THRESHOLDS.get(HBP_INDEX_THRESHOLDS.floorKey(hbpVal));
    }
    
	
}
