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
import com.threepillarglobal.labs.cdps.domain.MedicalRecords.DocumentsAttached;
import com.threepillarglobal.labs.cdps.domain.MedicalRecords.MedicalRecord;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.hbase.util.HOperations;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.annotation.Resource;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;
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
    private List<String> locationList;
    @Resource(name = "namePrefixList")
    private List<String> namePrefixList;
    @Resource(name = "emailList")
    private List<String> emailList;
    @Resource(name = "activityTypeList")
    private List<String> activityTypeList;
    @Resource(name = "vitaminList")
    private List<String> vitaminList;
    @Resource(name = "mineralList")
    private List<String> mineralList;    
    private Date startDate;
    private Date stopDate;
    private int totalDays;
    private boolean writeToDatabase;
    
    @Before
    public void setUp() throws IOException, ParseException {
        //table should be created at startup by DDL but to be sure
        HOperations.createTable(User.class, new HBaseAdmin(hBaseConfig));
        HOperations.createTable(SensorData.class, new HBaseAdmin(hBaseConfig));
        HOperations.createTable(LivingData.class, new HBaseAdmin(hBaseConfig));
        HOperations.createTable(Location.class, new HBaseAdmin(hBaseConfig));
        HOperations.createTable(MedicalRecords.class, new HBaseAdmin(hBaseConfig));
        //other init stuff
        startDate = dateFormat.parse(config.getProperty("fromDate"));
        stopDate = dateFormat.parse(config.getProperty("toDate"));
        totalDays = getDaysInInterval(startDate, stopDate);
        writeToDatabase = Boolean.parseBoolean(config.getProperty("writeToDatabase"));
        if(writeToDatabase) {
            L.info("Write To Database is active!=============================");
        }
    }
    @Resource(name = "hbaseConfiguration")
    private Configuration hBaseConfig;
    
    @After
    public void tearDown() {
        //DO NOTHING
    }
    
    @Test
    public void populate() throws Exception {
        if (Boolean.parseBoolean(config.getProperty("generateLocations"))) {
            generateLocations();
        }
        if (Boolean.parseBoolean(config.getProperty("generateUsers"))) {
            emails = generateUsers();
        }
        if (Boolean.parseBoolean(config.getProperty("generateSensorData"))) {
            generateSensorData(emails);
        }
        if (Boolean.parseBoolean(config.getProperty("generateLivingHabits"))) {
            generateLivingHabits(emails);
        }
        if (Boolean.parseBoolean(config.getProperty("generateMedicalRecors"))) {
            generateMedicalRecords(emails);
        }
        if (Boolean.parseBoolean(config.getProperty("generateMedicalRecors"))) {
            generateAttachedDocuments(emails);
        }
    }
    private List<String> emails;
    
    private void generateLocations() {
        L.info("Generating locations=========================================");
        Map<byte[], Location.LocationDetails> locationDetails = new LinkedHashMap<>();
        for (String location : locationList) {
            String[] locationParts = location.split("[|]");
            String country = locationParts[0].trim();
            String county = locationParts[1].trim();
            String city = locationParts[2].trim();
            L.debug("Country: " + country + " County: " + county + " City: " + city);
            locationDetails.put(Location.toRowKey(country, county, city), new Location.LocationDetails(city, county, country));
        }
        if (writeToDatabase) {
            locationRepository.saveLocationDetails(locationDetails);
        }
    }

    //returns the email list;
    private List<String> generateUsers() {
        L.info("Generating users=============================================");
        List<String> resultedEmails = new ArrayList<>();
        int userCount = Integer.parseInt(config.getProperty("userCount"));
        if (userCount > 0 && namePrefixList.size() > 0) {
            Map<byte[], User> users = new LinkedHashMap<>();
            int i = 0;
            while (i < userCount) {
                for (int j = 0; j < namePrefixList.size(); j++) {
                    String name = namePrefixList.get(j) + "_" + i;
                    String email = name + getEmailSufix(i);
                    resultedEmails.add(email);
                    String locationId = "" + locationList.get(random.nextInt(locationList.size()));
                    User.AccountData accountData = new User.AccountData("hackme", Boolean.TRUE, getPhone(), email);
                    User.PersonalData personalData = new User.PersonalData(name, new Date(), locationId);
                    User.MedicalNotes medicalNotes = new User.MedicalNotes(null, User.MedicalNotes.SMOKER.getRandom(), User.MedicalNotes.INHERITED_RISK.getRandom());
                    User user = new User(accountData, personalData, medicalNotes, null);
                    L.debug("id:" + i + ":" + user.toString());
                    users.put(User.toRowKey(email), user);
                    i++;
                    if (i >= userCount) {
                        break;
                    }
                }
            }
            if (writeToDatabase) {
                userRepository.saveUsers(users);
            }
        }
        return resultedEmails;
    }
    
    private void generateSensorData(List<String> emails) {
        L.info("Generate sensor data=========================================");
        if (emails != null) {
            for (String email : emails) {
                Map<byte[], SensorData> sensorDataMap = new LinkedHashMap<>();
                for (int i = 0; i < totalDays; i++) {
                    Date currentDate = DateUtils.addDays(startDate, i);
                    SensorData sensorData = getSenorData(currentDate);
                    L.debug(sensorData.toString());
                    sensorDataMap.put(SensorData.toRowKey(email, currentDate), sensorData);
                }
                if (writeToDatabase) {
                    sensorDataRepository.saveSensorData(sensorDataMap);
                }
            }
        }
    }
    
    private void generateLivingHabits(List<String> emails) {
        L.info("Generate living habits=======================================");
        if (emails != null) {
            for (String email : emails) {
                Map<byte[], LivingData> livingDataMap = new LinkedHashMap<>();
                for (int i = 0; i < totalDays; i++) {
                    Date currentDate = DateUtils.addDays(startDate, i);
                    LivingData livingData = getLivingData(currentDate);
                    L.debug(livingData.toString());
                    livingDataMap.put(LivingData.toRowKey(email, currentDate), livingData);
                }
                livingDataRepository.saveLivingData(livingDataMap);;
            }
        }
    }
    
    private void generateMedicalRecords(List<String> emails) {
        L.info("Generate medical recors======================================");
        if (emails != null) {
            Map<byte[], MedicalRecord> medicalRecordsMap = new LinkedHashMap<>();
            for (String email : emails) {
                //TODO do this smarter
                Date medicalRecordDate = DateUtils.addDays(startDate, random.nextInt(totalDays));
                Integer bmi = random.nextInt(50);
                Integer cholesterol = random.nextInt(500);
                Integer triglycerides = random.nextInt(500);
                String followUp = "Call by " + DateUtils.addDays(medicalRecordDate, random.nextInt(100)).toString();
                MedicalRecords.MedicalRecord medicalRecord = new MedicalRecords.MedicalRecord(medicalRecordDate, bmi, cholesterol, triglycerides, followUp);
                L.debug(medicalRecord.toString());
                medicalRecordsMap.put(MedicalRecords.toRowKey(email, medicalRecordDate), medicalRecord);
            }
            if (writeToDatabase) {
                medicalRecordsRepository.saveMedicalRecords(medicalRecordsMap);
            }
        }
    }
    
    private void generateAttachedDocuments(List<String> emails) throws ParseException {
        L.info("Generate attached documents==================================");
        String documentURLPrefix = config.getProperty("documentURLPrefix");
        if (emails != null) {
            Map<byte[], DocumentsAttached> documentsAttachedMap = new LinkedHashMap<>();
            int i = 0;
            for (String email : emails) {
                Date uploadDate = DateUtils.addDays(startDate, random.nextInt(totalDays));
                String documentURL = documentURLPrefix + Bytes.toStringBinary(MedicalRecords.toRowKey(email, uploadDate));
                DocumentsAttached document = new DocumentsAttached(documentURL);
                L.debug(document.toString());
                documentsAttachedMap.put(MedicalRecords.toRowKey(email, uploadDate), document);
            }
            if (writeToDatabase) {
                medicalRecordsRepository.saveAttachedDocuments(documentsAttachedMap);
            }
        }
    }

    //==========================================================================
    private int getDaysInInterval(Date startDate, Date endDate) {
        return Math.round((endDate.getTime() - startDate.getTime()) / miliSecondsInADay);
    }
    private static final long miliSecondsInADay = 1000 * 60 * 60 * 24;
    
    private String getEmailSufix(int id) {
        return "@"+emailList.get(id % emailList.size());
    }
    
    private String getPhone() {
        long result;
        do {
            result = random.nextLong();
        } while (result < 10000000000L || result > 99999999999L);
        return "+" + result;
    }
    
    private SensorData getSenorData(Date currentDate) {
        //TODO make this much smarter
        List<HourlyData> h = new ArrayList<>(24);
        for (int j = 0; j < 24; j++) {
            String currentActivity = activityTypeList.get(random.nextInt(activityTypeList.size()));
            Integer duration = random.nextInt(61);
            Integer averageHeartRate = random.nextInt(200);
            Integer systolicPressure = 100 + random.nextInt(200);
            Integer diastolicPressure = 50 + random.nextInt(100);
            Integer caloriesBurned = random.nextInt(3000);
            HourlyData hourlyData = new HourlyData(currentActivity, duration, averageHeartRate, systolicPressure, diastolicPressure, caloriesBurned);
            h.add(hourlyData);
        }
        return new SensorData(currentDate,
                h.get(0),
                h.get(1),
                h.get(2),
                h.get(3),
                h.get(4),
                h.get(5),
                h.get(6),
                h.get(7),
                h.get(8),
                h.get(9),
                h.get(10),
                h.get(11),
                h.get(12),
                h.get(13),
                h.get(14),
                h.get(15),
                h.get(16),
                h.get(17),
                h.get(18),
                h.get(19),
                h.get(20),
                h.get(21),
                h.get(22),
                h.get(23)
        );
    }
    
    private LivingData getLivingData(Date currentDate) {
        //TODO make this much smarter
        Integer minsOfSleep = random.nextInt(minsInDay);
        Integer minsOfExcercise = random.nextInt(minsInDay);
        Integer calories = random.nextInt(5000);
        Integer energy = random.nextInt(5000);
        Integer fat = random.nextInt(1000);
        Integer saturatedFat = random.nextInt(1000);
        Integer sugars = random.nextInt(1000);
        Integer sodium = random.nextInt(100);
        Integer protein = random.nextInt(1000);
        Integer carbohydrates = random.nextInt(1000);
        String vitamins = vitaminList.get(random.nextInt(vitaminList.size()));
        String minerals = mineralList.get(random.nextInt(mineralList.size()));
        Integer water = random.nextInt(10000);
        Integer alcohol = random.nextInt(10000);
        Integer softDrinks = random.nextInt(10000);
        BigDecimal riskFactor = new BigDecimal(1.0);
        return new LivingData(minsOfSleep, minsOfExcercise, calories, energy, fat, saturatedFat, sugars, sodium, protein, carbohydrates, vitamins, minerals, water, alcohol, softDrinks, riskFactor);
    }
    private static final int minsInDay = 1440;
}
