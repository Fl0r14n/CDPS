package com.threepillarglobal.labs.cpds;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.threepillarglobal.labs.cdps.analytics.RiskAnalyser;
import com.threepillarglobal.labs.cdps.analytics.RiskIndexUtils;
import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.Eventdate;
import com.threepillarglobal.labs.cdps.domain.Location.LocationDetails;
//import com.threepillarglobal.labs.cdps.domain.LocUserDailyAvgDataV1;
import com.threepillarglobal.labs.cdps.domain.Location;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.MedicalNotes;
import com.threepillarglobal.labs.cdps.domain.UserAverage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.WhileMatchFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.IdentityTableReducer;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MD5Hash;
import org.apache.hadoop.mapreduce.Job;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.jruby.runtime.Visibility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.threepillarglobal.labs.cdps.dao.repository.*;
import com.threepillarglobal.labs.cdps.service.api.ReportService;
import com.threepillarglobal.labs.cdps.service.api.RiskService;
import com.threepillarglobal.labs.hbase.util.HMarshaller;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:integrationTests-context.xml")
public class LocUsrAvgFlattener {

	
	@Autowired
	private HbaseTemplate hbaseTemplate;
	
    @Resource(name = "hbaseConfiguration")
    private Configuration config;

    
   	@Autowired
	private UserRepository userService;

	@Autowired
	LocationRepository locRepo;
	
	@Autowired
    @Qualifier(value = "riskFactorServiceImpl")
    private RiskService riskFactorsService;
	
	@Autowired
    @Qualifier(value = "reportServiceImpl")
    private ReportService reportService;
	
	@Autowired
	private SensorDataRepository sensorRepo;
	
	//@Test 
	public void convert() throws Exception{
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		List<SensorData> sensorData = sensorRepo.findSensorDataForUserInInterval("Robert Downey Jr._47@eris.com", 
				 dateFormat.parse("2014-01-28"), 
				 dateFormat.parse("2014-01-28"));
		
		System.out.println("[1] " + sensorData.size() );
		
		List<Eventdate> edList = RiskIndexUtils.convert(sensorData);
		
		System.out.println("[2] " + edList.size());
		
	}
	
	//@Test
	public void updateAvgValues() throws Exception{
		try{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
			List<User> findAllUser = userService.findAllUser();
			int i=0;
			for (User u : findAllUser) {
			   
				//User u = userService.findUser("Robert Downey Jr._47@eris.com");
				
				MedicalNotes medicalNotes = u.getMedicalNotes();
				
				ObjectMapper dailyAvg = new ObjectMapper();
				
				UserAverage uA = new UserAverage();
				List<Eventdate> lD = new ArrayList<Eventdate>();
				
				//System.out.println("..... processing " + u.getAccountData().getEmail() + "\n" + u.getMedicalNotes().getDailyAvg());
				
				List<SensorData> sensorData = sensorRepo.findSensorDataForUserInInterval(u.getAccountData().getEmail(), 
																						 dateFormat.parse("2014-01-01"), 
																						 dateFormat.parse("2016-01-01"));
				
				
				if(sensorData!=null && !sensorData.isEmpty()){
					System.out.println("=== Processing sensor data from: " +  dateFormat.parse("2014-01-01") + " to: " + 
							dateFormat.parse("2016-01-01") + " = " + sensorData.size() + " records ====");
					
					for (SensorData sensorData2 : sensorData) {
						Eventdate eD = new Eventdate();
						eD.setEventdate(sensorData2.getEventDate());
						eD.setDiastolicPressure((int)RiskAnalyser.getDailyBpMeanValue(sensorData2, "getSystolicPressure"));
						eD.setSystolicPressure((int)RiskAnalyser.getDailyBpMeanValue(sensorData2, "getDiastolicPressure"));
						lD.add(eD);
						uA.setEventdate(lD);
					}
					
					String sUA = dailyAvg.writeValueAsString(uA);
					
					//System.out.println("===\n" + sUA + "\n====");
					
					medicalNotes.setDailyAvg(sUA);
					
					userService.saveMedicalNotes(u.getAccountData().getEmail(), medicalNotes);
				}else{
					System.out.println("=== EMPTYING OUT");
					medicalNotes.setDailyAvg(dailyAvg.writeValueAsString(uA));
					userService.saveMedicalNotes(u.getAccountData().getEmail(), medicalNotes);
				}
				
				//u = userService.findUser("Robert Downey Jr._47@eris.com");
				
				System.out.println("^^^ " + i + " out of " + findAllUser.size() + " users: done");
				//System.out.println(u.getMedicalNotes().getDailyAvg());
				i++;
			}
	
		}catch(Exception ex){
			ex.printStackTrace();
		}
	} 
    
    @Test
    public void highRiskPatientNoByLocation()  {

    	 List<LocationDetails> locations = locRepo.findAllLocations();
     	 Map<String, AtomicInteger> riskByLocation = new HashedMap(locations.size());
     	 for(LocationDetails loc : locations){
     		riskByLocation.put(loc.getCity()+"|"+loc.getCounty()+"|" + loc.getCountry(), new AtomicInteger(0));
     	 }
     	
     	 List<User> findAllUser = userService.findAllUser();     	    	  
    	 
     	 int i=0;
    	 for (User user : findAllUser) {
    		 
    		 //System.out.println("#"+i + " [" + user.getAccountData().getEmail() + "]");
    		 
    		 //if(i<=1030)
    		 //{
	    		 
	    		 
	    		ObjectMapper dailyAvg = new ObjectMapper();
	        	String dailyAvgJsonString = user.getMedicalNotes().getDailyAvg();
	       		UserAverage uAV;
				try {
					if(dailyAvgJsonString!=null){
						 uAV = dailyAvg.readValue(dailyAvgJsonString, UserAverage.class);
						 if(uAV!=null){
				   			 CardioRisk cR = new RiskAnalyser().calculateRiskFactor(user, uAV.getEventdate(), 
													new SimpleDateFormat("yyyy-MM-dd").parse("2014-01-01"),
											 		new SimpleDateFormat("yyyy-MM-dd").parse("2014-03-01") );
							
				   			if(cR.getRiskFactor()>2.0){
								if(user.getPersonalData()!=null && user.getPersonalData().getLocationId()!=null && riskByLocation.get(user.getPersonalData().getLocationId())!=null)
						             riskByLocation.get(user.getPersonalData().getLocationId()).incrementAndGet();
				   			}
						 }
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
    		 }
   			i++;
		 //}else{
			 //System.out.println("+++ BANNED!");
		//	 i++;
		 //}
    	 }
    	 
    	 
    	 for(LocationDetails loc : locations){
      		System.out.println("# " + loc.getCity()+"|"+loc.getCounty()+"|" + loc.getCountry() + " -> "+  riskByLocation.get(loc.getCity()+"|"+loc.getCounty()+"|" + loc.getCountry()).get() );
      	 }
    	    	
	}

    
    
}

