package com.threepillarglobal.labs.cdps.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.*;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.threepillarglobal.labs.cdps.analytics.RiskIndexUtils;

public class SensorData {

	private enum Metric {LBP, HBP, PULSE, CALORIES};
	private String bloodPressureRisk ;

	public String getBloodPressureRisk() {
		return bloodPressureRisk;
	}

	public void setBloodPressureRisk(String bloodPressureRisk) {
		this.bloodPressureRisk = bloodPressureRisk;
	}


	private List<HourlyData> hourlyData = new ArrayList<>();
	public List<HourlyData> getHourlyData() {
		return hourlyData;
	}

	public void setHourlyData(List<HourlyData> hourlyData) {
		this.hourlyData = hourlyData;
	}


	private Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public class HourlyData{
		private Integer hBloodPressure;
		private Integer heartRate;
		private String  activityType;
		private String hour;
		private Integer lBloodPressure;
		
		public HourlyData(){}
		
		public Integer getlBloodPressure() {
			return lBloodPressure;
		}
		public void setlBloodPressure(Integer lBloodPressure) {
			this.lBloodPressure = lBloodPressure;
		}
		public Integer gethBloodPressure() {
			return hBloodPressure;
		}
		public void sethBloodPressure(Integer hBloodPressure) {
			this.hBloodPressure = hBloodPressure;
		}
		public Integer getHeartRate() {
			return heartRate;
		}
		public void setHeartRate(Integer heartRate) {
			this.heartRate = heartRate;
		}
		public String getActivityType() {
			return activityType;
		}
		public void setActivityType(String activityType) {
			this.activityType = activityType;
		}
		public String getHour() {
			return hour;
		}
		public void setHour(String hour) {
			this.hour = hour;
		}
		@Override
		public String toString() {
		 	try {
				return new ObjectMapper().writeValueAsString(this).toString();
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 	return "Tough luck" ;
		}
	}
	
	@SuppressWarnings("unchecked")
	public SensorData(com.threepillarglobal.labs.cdps.domain.SensorData sD){
		
		  String hRisk = "BP above risk tresholds [" + new SimpleDateFormat("dd-MM-yyyy").format(sD.getEventDate()) + "]:<br/>";
		  String lbpRisk = hRisk;
		  setDate(sD.getEventDate());
		  Method[] methods = sD.getClass().getMethods();
		  try {
			  List<String> list = null;
			  HourlyData hD = null;
			  for(Method method : methods){
				  if(isGetter(method)) {
					  hD = new HourlyData();
					  Object data = method.invoke(sD, null);
					  if(data instanceof List<?>){
					    list = (List<String>)data;
					    if(list!=null && list.size()==5){
						  String hour = method.getName().substring(4, method.getName().length()-1);
						  hD.setHour(hour);
						  hD.setActivityType((String)list.get(0));
					      hD.setHeartRate(Integer.valueOf(list.get(2)));
					      String[] bP = list.get(3).split("/");
					      Integer lbvalueOf = Integer.valueOf(bP[0]);
						  hD.setlBloodPressure(lbvalueOf);
						  lbpRisk = riskFormat(lbpRisk, lbvalueOf, Metric.LBP, hour, RiskIndexUtils.getLowBloodPressureRiskIndex( lbvalueOf ));
					      Integer hbpvalueOf = Integer.valueOf(bP[1]);
						  hD.sethBloodPressure(hbpvalueOf);					      
						  lbpRisk = riskFormat(lbpRisk, hbpvalueOf, Metric.HBP, hour, RiskIndexUtils.getHighBloodPressureRiskIndex( Integer.valueOf(bP[1]) ));
					      hourlyData.add(hD);
					  }
					 }
				  }
			  }
			  if(hourlyData!=null){
				  setHourlyData(hourlyData);
				  if(!lbpRisk.equals(hRisk)){
					  setBloodPressureRisk(lbpRisk);
				  }
			  }
			} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}		    	
	}
	//2013-12-05
	public String toJSONString(){
		 ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			JsonGenerator jGenerator = new JsonFactory().createJsonGenerator(new OutputStreamWriter(bout, "UTF-8"));
			jGenerator.writeStartObject(); 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = formatter.format(getDate());
			jGenerator.writeStringField("date", formattedDate);
			jGenerator.writeStringField("riskInfo",getBloodPressureRisk());
			
			for(HourlyData hD: getHourlyData()){				
				jGenerator.writeFieldName(hD.getHour());
				jGenerator.writeStartObject();
				jGenerator.writeStringField("activityType", hD.getActivityType());
				jGenerator.writeStringField("lowBloodPressure", String.valueOf(hD.getlBloodPressure()));
				jGenerator.writeStringField("highBloodPressure", String.valueOf(hD.gethBloodPressure()));
				jGenerator.writeStringField("heartRate", String.valueOf(hD.getHeartRate()));				
				jGenerator.writeEndObject(); 
			}
			
			jGenerator.writeEndObject(); // }
			jGenerator.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bout.toString();
	}
	
	private String riskFormat(String text, Integer value, Metric metric, String hour, Integer riskIndex){
		
		if(Integer.valueOf(hour)<12)
			hour = hour + "AM";
		else
			hour = hour + "PM";
		
		if(riskIndex>9){
			if(metric.equals(Metric.LBP)){
				text = text + " [ " + hour + " LBP RI: " +  riskIndex + "] (" + value + ")<br/>" ;
			}else
				if(metric.equals(Metric.HBP)){
					text = text + " [ " + hour + " HBP RI: " +  riskIndex + "] (" + value + ")<br/>" ;
				}
		}
		return text;
	}
	
	public static boolean isGetter(Method method){
		  if(!method.getName().startsWith("get"))      return false;
		  if(method.getParameterTypes().length != 0)   return false;  
		  if(void.class.equals(method.getReturnType())) return false;
		  return true;
	}
	
}
