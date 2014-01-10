package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.cdps.dao.repository.LivingDataRepository;
import com.threepillarglobal.labs.cdps.dao.repository.LocationRepository;
import com.threepillarglobal.labs.cdps.dao.repository.MedicalRecordsRepository;
import com.threepillarglobal.labs.cdps.dao.repository.SensorDataRepository;
import com.threepillarglobal.labs.cdps.dao.repository.UserRepository;
import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.cdps.domain.Location;
import com.threepillarglobal.labs.cdps.domain.MedicalRecords;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.hbase.util.HOperations;
import java.io.IOException;
import java.util.Properties;
import javax.annotation.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:integrationTests-context.xml", "classpath*:dataGenerator-context.xml"})
public class DataGeneratorIT {

    private static final Logger L = LoggerFactory.getLogger(DataGeneratorIT.class);

    @Resource(name = "dataGeneratorConfiguration")
    private Properties config;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SensorDataRepository sensorDataRepository;
    @Autowired
    private MedicalRecordsRepository medicalRecordsRepository;
    @Autowired
    private LivingDataRepository livingDataRepository;
    @Autowired
    private LocationRepository locationRepository;

    @Before
    public void setUp() throws IOException {
        //table should be created at startup by DDL but to be sure
        HOperations.createTable(User.class, new HBaseAdmin(hBaseConfig));
        HOperations.createTable(SensorData.class, new HBaseAdmin(hBaseConfig));
        HOperations.createTable(LivingData.class, new HBaseAdmin(hBaseConfig));
        HOperations.createTable(Location.class, new HBaseAdmin(hBaseConfig));
        HOperations.createTable(MedicalRecords.class, new HBaseAdmin(hBaseConfig));
    }
    @Resource(name = "hbaseConfiguration")
    private Configuration hBaseConfig;

    @After
    public void tearDown() {
        //DO NOTHING
    }

    @Test
    public void populate() {
        generateUsers();
    }
    
    
    private void generateUsers() {
        int userCount = Integer.parseInt(config.getProperty("userCount"));
        System.out.println("Users: "+userCount);
    }
}
