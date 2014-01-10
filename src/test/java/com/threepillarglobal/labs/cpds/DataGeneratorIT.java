package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.cdps.dao.repository.LivingDataRepository;
import com.threepillarglobal.labs.cdps.dao.repository.LocationRepository;
import com.threepillarglobal.labs.cdps.dao.repository.MedicalRecordsRepository;
import com.threepillarglobal.labs.cdps.dao.repository.SensorDataRepository;
import com.threepillarglobal.labs.cdps.dao.repository.UserRepository;
import com.threepillarglobal.labs.cdps.domain.HourlyData;
import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.cdps.domain.Location;
import com.threepillarglobal.labs.cdps.domain.MedicalRecords;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.hbase.util.HOperations;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.annotation.Resource;
import org.apache.commons.lang.time.DateUtils;
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
    private static final Random random = new Random();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    @Resource(name = "configurationProperties")
    private Properties config;
    @Resource(name = "locationList")
    private List<String> locations;
    @Resource(name = "namePrefixList")
    private List<String> namePrefixList;

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
    public void populate() throws Exception {
        generateLocations();
        List<String> emails = generateUsers();
        generateSensorData(emails);
        generateLivingHabits();
    }

    private void generateLocations() {
        Map<byte[], Location.LocationDetails> locationDetails = new HashMap<>();
        for (String location : locations) {
            String[] locationParts = location.split("|");
            String country = locationParts[0].trim();
            String county = locationParts[1].trim();
            String city = locationParts[2].trim();
            locationDetails.put(Location.toRowKey(country, county, city), new Location.LocationDetails(city, county, country));
        }
        locationRepository.saveLocationDetails(locationDetails);
    }

    //returns the email list;
    private List<String> generateUsers() {
        List<String> emails = new ArrayList<>();
        int userCount = Integer.parseInt(config.getProperty("userCount"));
        if (userCount > 0 && namePrefixList.size() > 0) {
            Map<byte[], User> users = new HashMap<>();
            int i = 0;
            while (i < userCount) {
                for (int j = 0; j < namePrefixList.size(); j++) {
                    String name = namePrefixList.get(j) + "_" + j;
                    String email = name + getEmailSufix(i);
                    emails.add(email);
                    String locationId = "" + locations.get(random.nextInt(locations.size()));
                    User.AccountData accountData = new User.AccountData("hackme", Boolean.TRUE, getPhone(), email);
                    User.PersonalData personalData = new User.PersonalData(name, new Date(), locationId);
                    User.MedicalNotes medicalNotes = new User.MedicalNotes(null, User.MedicalNotes.SMOKER.getRandom(), User.MedicalNotes.INHERITED_RISK.getRandom());
                    User user = new User(accountData, personalData, medicalNotes, null);
                    users.put(User.toRowKey(email), user);
                    i++;
                    if (i < userCount) {
                        break;
                    }
                }
            }
            userRepository.saveUsers(users);
        }
        return emails;
    }

    private void generateSensorData(List<String> emails) throws ParseException {
        for (String email : emails) {
            Date startDate = dateFormat.parse(config.getProperty("fromDate"));
            Date stopDate = dateFormat.parse(config.getProperty("toDate"));
            int totalDays = getDaysInInterval(startDate, stopDate);
            for (int i = 0; i < totalDays; i++) {
                Date currentDate = DateUtils.addDays(startDate, i);
//                HourlyData h00 = new HourlyData(activityType, duration, averageHeartRate, systolicPressure, diastolicPressure, caloriesBurned);
                SensorData sensorData = new SensorData(currentDate, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
                sensorDataRepository.saveSensorData(email, currentDate, sensorData);
            }
        }
    }

    private void generateLivingHabits() {

    }

    private int getDaysInInterval(Date startDate, Date endDate) {
        return Math.round((endDate.getTime() - startDate.getTime()) / miliSecondsInADay);
    }
    private static final long miliSecondsInADay = 1000 * 60 * 60 * 24;

    private String getEmailSufix(int id) {
        switch (id % 4) {
            case 0: {
                return "@earth.com";
            }
            case 1: {
                return "@mars.com";
            }
            case 2: {
                return "@jupiter.com";
            }
            case 3: {
                return "@moon.com";
            }
        }
        return "";
    }

    private String getPhone() {
        long result;
        do {
            result = random.nextLong();
        } while (result > 10000000000L && result < 99999999999L);
        return "+" + result;
    }
}
