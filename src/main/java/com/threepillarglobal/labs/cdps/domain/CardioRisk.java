package com.threepillarglobal.labs.cdps.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CardioRisk {
	private  Integer ageIndex;
	private  Integer smokingIndex;
	private  Integer inheritanceIndex;
	private  Integer diastolicIndex;
	private  Integer systolicIndex;
	private  Integer cholesterolIndex;
	private  Integer triglyceridesIndex;
	private  Integer bmsIndex;
	private  Integer excerciseIndex;
	private  Integer caloriesIndex;
	private  Integer drinkIntakeIndex;
	
	private  Double riskFactor;
	private  String strokeRisk;
	
	private List<SensorData> sensorData;
}
