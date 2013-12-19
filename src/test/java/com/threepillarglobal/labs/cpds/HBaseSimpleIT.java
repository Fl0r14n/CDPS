package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.cdps.analytics.RiskAnalyser;
import com.threepillarglobal.labs.cdps.dao.repository.SensorDataRepository;
import com.threepillarglobal.labs.cdps.dao.repository.UserRepository;
import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.AccountData;
import com.threepillarglobal.labs.cdps.service.api.UserService;
import com.threepillarglobal.labs.hbase.util.HOperations;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This class will be used to insert mock data into HBase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:integrationTests-context.xml")
public class HBaseSimpleIT {

    @Resource(name = "hbaseConfiguration")
    private Configuration config;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws IOException {
        //table should be created at startup by DDL but to be sure
        //HOperations.createTable(User.class, new HBaseAdmin(config));
    }

    @After
    public void tearDown() throws IOException {
        //delete table
        //HOperations.deleteTable(User.class, new HBaseAdmin(config));
    }

    @Autowired
    @Qualifier(value = "userServiceImpl")
    private UserService userService;
	
	@Autowired
	private SensorDataRepository sensorRepo;
	
	@Test
    public void  getCardioRisk() {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 
		String uid = "Mario.Bross1@3pg.com";
				
		try{
		DateTime b = new DateTime(dateFormat.parse("2013-12-19"));
		User user = userService.getUser(uid);
		DateTime e = new DateTime(dateFormat.parse("2013-12-20"));
		//if(user!=null)
		//	System.out.println("User fetched: " + uid + ' ' +  user + " in " + Seconds.secondsBetween(b, e).getSeconds() % 60 + " secs");
        
        
        List<SensorData> sensorData = sensorRepo.findSensorDataForUserInInterval(uid, dateFormat.parse("2013-12-19"), dateFormat.parse("2013-12-20"));
        //if(sensorData!=null)
        //	System.out.println("=== Sensor data list [" + b + " : " + e + "] size: " + sensorData.size() + " " + sensorData.get(0).toString() + " retrieved in " + Seconds.secondsBetween(b, e).getSeconds() % 60 + " seconds");
		
        CardioRisk cR; 
        
        if(user!=null && sensorData!=null){
        	cR = new RiskAnalyser().calculateRiskFactor(user, sensorData );
        	System.out.println(cR.toString());
        }
        
		}catch(Exception ec){
			ec.printStackTrace();
		}
		
		
    }
    
    @Test
    public void populate_hbase_with_some_accont_data_print_it_then_delete_it() {
    	
        /*//TODO
        for (int i = 0; i < 10; i++) {
            AccountData ad = new AccountData("secret" + i, (i % 2 == 1) ? Boolean.TRUE : Boolean.FALSE, "072900000" + i, "secret" + i + "@3pg.com");
            userRepository.saveAccountData("key@" + i, ad);
        }
        for (AccountData ad : userRepository.findAllAccountData()) {
            System.out.println(ad.toString());
        }*/
    	
    }
}
