package com.threepillarglobal.labs.cpds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.Location.LocationDetails;
import com.threepillarglobal.labs.cdps.domain.Location;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.IdentityTableReducer;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MD5Hash;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.threepillarglobal.labs.cdps.dao.repository.*;
import com.threepillarglobal.labs.cdps.service.api.RiskService;
import com.threepillarglobal.labs.hbase.util.HMarshaller;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:integrationTests-context.xml")
public class HBaseMapReduceIT {


    @Resource(name = "hbaseConfiguration")
    private Configuration config;

    
   	@Autowired
	private UserRepository userService;
	private static List<User> userList;

	@Autowired
	LocationRepository locRepo;
	
	@Autowired
    @Qualifier(value = "riskFactorServiceImpl")
    private RiskService riskFactorsService;
	
    //statics
    
    static UserRepository sUserRepo;
    @Autowired
    public void setStaticUserRepository(UserRepository userRepo) {
    	HBaseMapReduceIT.sUserRepo = userRepo;
    }
    
    static LocationRepository slocRepo;
    @Autowired
    public void setStaticLocationRepository(LocationRepository locRepo) {
    	HBaseMapReduceIT.slocRepo = locRepo;
    }
    
    static RiskService sriskFactorsService;
    @Autowired
    @Qualifier(value = "riskFactorServiceImpl")
    public void setStaticRiskService(RiskService riskFactorsService) {
    	HBaseMapReduceIT.sriskFactorsService = riskFactorsService;
    }
    
    
    static Map<User, List<SensorData>> userMap =  new HashMap<User, List<SensorData>>();
    
    
    @Test
    public void run_mapreduce_job() throws IOException, InterruptedException, ClassNotFoundException {

    	//fetch all users
    	userList = userService.findAllUser();
        
    	if(userList.size()==0)
    		return;

        Job job = new Job(config, com.threepillarglobal.labs.cpds.HBaseMapReduceIT.class.getName());                
        job.setJarByClass(com.threepillarglobal.labs.cpds.HBaseMapReduceIT.class);        
         Scan scan = new Scan();
        {
         scan.setCaching(500);        
         scan.setCacheBlocks(false);  
         scan.setFilter(new FirstKeyOnlyFilter());
        }
		
        
		 TableMapReduceUtil.initTableMapperJob(
				Bytes.toString("sensorData".getBytes()),
				scan,
				UserByLocationMapper.class,
				ImmutableBytesWritable.class,
				Put.class,
		 job);
	  
		 TableMapReduceUtil.initTableReducerJob(
				 			Bytes.toString("summary_user".getBytes()),
				 			UserByLocationReducer.class,
				 			job);
	 
	     job.setNumReduceTasks(1);
	         
         if (!job.waitForCompletion(true)) {
            throw new IOException("error running job");
         }
         
  }

    
   
    //key out, value out
    public static class UserByLocationMapper extends TableMapper<ImmutableBytesWritable, Put> {

   		

	@Override
	protected void setup(org.apache.hadoop.mapreduce.Mapper<ImmutableBytesWritable,Result,ImmutableBytesWritable,Put>.Context context) throws IOException ,InterruptedException {
		
		if(userList!=null && userList.size()>0){
			userMap =  new HashMap<User, List<SensorData>>(userList.size());
			
			for(User u : userList){
				userMap.put(u, new ArrayList<SensorData>());
			}
		}
		
	};
    
	@Override
	protected void cleanup(org.apache.hadoop.mapreduce.Mapper<ImmutableBytesWritable,Result,ImmutableBytesWritable,Put>.Context context) throws IOException ,InterruptedException {
		 //outputStatistics();
	};
	
	   
    @Override
    protected void map(ImmutableBytesWritable key, Result result, Context context) throws IOException, InterruptedException {
        	
        	for(User u : userList){
        			
        			String email = u.getAccountData().getEmail();
        			if( Bytes.toString(key.get()).startsWith(MD5Hash.digest(email).toString())){
        				SensorData sD = null;
						try {
							sD = HMarshaller.unmarshall(SensorData.class, result);
							if(sD!=null){
								userMap.get(u).add(sD);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
        			}
        	}
        }
    
    
    
    

    }

    //key in, value in, key out
    public static class UserByLocationReducer extends TableReducer<ImmutableBytesWritable, IntWritable, ImmutableBytesWritable> {

    	private  void outputStatistics(org.apache.hadoop.mapreduce.Reducer<ImmutableBytesWritable,IntWritable,ImmutableBytesWritable,org.apache.hadoop.io.Writable>.Context context) throws IOException ,InterruptedException {
        	
        	List<LocationDetails> locations = slocRepo.findAllLocations();
        	Map<String, AtomicInteger> riskByLocation = new HashedMap(locations.size());
        	for(LocationDetails loc : locations){
        		riskByLocation.put(loc.getCity()+"|"+loc.getCounty()+"|" + loc.getCountry(), new AtomicInteger(0));
        	}
        	
        	System.out.println("\n\nPatients on records\n----------------------------\n");
        	for(User aKey : userMap.keySet()) {
     			List<SensorData> aValue = userMap.get(aKey);
     		    try {
     		    	if(aValue.size()>0){
     		    				CardioRisk cardioRisk = sriskFactorsService.getCardioRisk(aKey.getAccountData().getEmail(), 
    																			   new SimpleDateFormat("yyyy-MM-dd").parse("2014-01-01"), 
    																			   	new SimpleDateFormat("yyyy-MM-dd").parse("2015-06-06"));
    							System.out.println("Patient " + aKey.getPersonalData().getName() + " [" + aKey.getPersonalData().getLocationId() + "]" + 
     		    							" .... stroke risk index=" + cardioRisk.getRiskFactor() + ", risk:" + cardioRisk.getStrokeRisk() + "" );
    							
    							if(cardioRisk.getRiskFactor()>3.0)
    								riskByLocation.get(aKey.getPersonalData().getLocationId()).incrementAndGet();
    							
     		    	}/*else{
     		    		System.out.println("*** no data for user: " + aKey.getPersonalData().getName());
     		    	}*/
     			} catch (ParseException e) {
     				e.printStackTrace();
     			}
        	}
        	
        	System.out.println("\n\nNo of patients with stroke risk by location\n----------------------------\n");
        	for(String locId : riskByLocation.keySet()) {
        		System.out.println(locId + " = " + riskByLocation.get(locId));
        		save(context, locId, riskByLocation.get(locId).intValue());        	}
        	System.out.println("----------------------------\n");
        }
    	
    	@Override
    	protected void setup(org.apache.hadoop.mapreduce.Reducer<ImmutableBytesWritable,IntWritable,ImmutableBytesWritable,org.apache.hadoop.io.Writable>.Context context) throws IOException ,InterruptedException {
    		
    		outputStatistics(context);
            
    	};
    	

    	protected void save(org.apache.hadoop.mapreduce.Reducer<ImmutableBytesWritable,IntWritable,ImmutableBytesWritable,org.apache.hadoop.io.Writable>.Context context, String locId, int value) throws IOException ,InterruptedException {
    		
    		ImmutableBytesWritable outkey =
    		          new ImmutableBytesWritable(new Put(locId.getBytes()).getRow()); 
    		
            Put put = new Put(outkey.get());
            put.add(Bytes.toBytes("details"), Bytes.toBytes("total"), Bytes.toBytes(value));
            context.write(outkey, put);
            
    	};
        
        
    }
}
