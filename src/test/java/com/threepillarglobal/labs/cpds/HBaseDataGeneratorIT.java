package com.threepillarglobal.labs.cpds;

import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.threepillarglobal.labs.cdps.dao.repository.*;
import com.threepillarglobal.labs.cdps.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.threepillarglobal.labs.hbase.util.HOperations;
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
@ContextConfiguration(locations = "classpath*:integrationTests-context.xml")
public class HBaseDataGeneratorIT {

    private static final Logger L = LoggerFactory.getLogger(HBaseSimpleIT.class);
    private static final String emailPattern = "Mario.Bross<ID>@3pg.com";
    private static final String namePrefix = "Mario Bross #";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String[] location = {"Romania|Cluj|Cluj-Napoca", "Romania|Timisoara|Timisoara", "USA|Washington|Washington DC"};
    private static final String[] activityType = {"Eating", "Sleeping", "Exercising"};
    private static final String[] vitaminsLookup = {"A", "B", "C", "D", "E"};
    private static final String[] mineralsLookup = {"Fe", "Zn", "Mg", "Ca", "Vn"};
    private static final String documentURLPattern = "http://docs.google.com/docID=";
    private static TestDataGeneratorConfig tdgc;

    @Resource(name = "hbaseConfiguration")
    private Configuration config;

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
        //table should be created at startup by DDL bu to be sure
        HOperations.createTable(User.class, new HBaseAdmin(config));
        HOperations.createTable(SensorData.class, new HBaseAdmin(config));
        HOperations.createTable(LivingData.class, new HBaseAdmin(config));
        HOperations.createTable(Location.class, new HBaseAdmin(config));
        HOperations.createTable(MedicalRecords.class, new HBaseAdmin(config));
        tdgc = readDataGeneratorConfig();
    }

    @After
    public void tearDown() throws IOException {
        //delete table
        //HOperations.deleteTable(User.class, new HBaseAdmin(config));
    }

    static boolean validateAgainstXSD(InputStream xml, InputStream xsd) {
        try {
            SchemaFactory factory
                    = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public TestDataGeneratorConfig readDataGeneratorConfig() {
        Date startDate = new Date(0),
                endDate = new Date(0);
        int userCount = -1,
                fullSensorDataPercent = -1,
                partialSensorDataFillPercent = -1,
                fullLivingHabitsPercent = -1,
                partialLivingHabitsFillPercent = -1;
        try {
            InputStream configFile2Validate = new FileInputStream(new File("src/test/resources/TestDataGenerator.xml"));
            InputStream schemaFile2Validate = new FileInputStream(new File("src/test/resources/TestDataGenerator.xsd"));

            InputStream configFile = new FileInputStream(new File("src/test/resources/TestDataGenerator.xml"));

            if (!validateAgainstXSD(configFile2Validate, schemaFile2Validate)) {
                throw new Exception("Failed xsd validation!");
            } else {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                // Setup a new eventReader
                XMLEventReader eventReader = inputFactory.createXMLEventReader(configFile);
                // read the XML document

                while (eventReader.hasNext()) {
                    XMLEvent event = eventReader.nextEvent();

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart().equals("UserCount")) {

                            event = eventReader.nextEvent();

                            userCount = Integer.parseInt(event.asCharacters().getData());
                            //System.out.println("User count: " + userCount);
                            event = eventReader.nextEvent();
                            continue;
                        }

                        if (event.asStartElement().getName().getLocalPart().equals("StartDate")) {
                            event = eventReader.nextEvent();

                            startDate = dateFormat.parse(event.asCharacters().getData());
                            //System.out.println("Start date: " + startDate);

                            eventReader.nextEvent();
                            continue;
                        }
                        if (event.asStartElement().getName().getLocalPart().equals("EndDate")) {
                            event = eventReader.nextEvent();

                            endDate = dateFormat.parse(event.asCharacters().getData());
                            //System.out.println("End date: " + endDate);

                            eventReader.nextEvent();
                            continue;
                        }

                        if (event.asStartElement().getName().getLocalPart().equals("FullSensorDataPercent")) {
                            event = eventReader.nextEvent();

                            fullSensorDataPercent = Integer.parseInt(event.asCharacters().getData());
                            //System.out.println("Full Sensor Data Percent: " + fullSensorDataPercent);

                            eventReader.nextEvent();
                            continue;
                        }

                        if (event.asStartElement().getName().getLocalPart().equals("PartialSensorDataFillPercent")) {
                            event = eventReader.nextEvent();

                            partialSensorDataFillPercent = Integer.parseInt(event.asCharacters().getData());
                            //System.out.println("Partial Sensor Data Fill Percent: " + partialSensorDataFillPercent);

                            eventReader.nextEvent();
                            continue;
                        }

                        if (event.asStartElement().getName().getLocalPart().equals("FullLivingHabitsPercent")) {
                            event = eventReader.nextEvent();

                            fullLivingHabitsPercent = Integer.parseInt(event.asCharacters().getData());
                            //System.out.println("Full Living Habits Percent: " + fullLivingHabitsPercent);

                            eventReader.nextEvent();
                            continue;
                        }

                        if (event.asStartElement().getName().getLocalPart().equals("PartialLivingHabitsFillPercent")) {
                            event = eventReader.nextEvent();

                            partialLivingHabitsFillPercent = Integer.parseInt(event.asCharacters().getData());
                            //System.out.println("Partial Living Habits Fill Percent: " + partialLivingHabitsFillPercent);

                            eventReader.nextEvent();
                            continue;
                        }

                    }
                }
            }
            if (userCount != -1 && startDate != new Date(0) && endDate != new Date(0)
                    && fullSensorDataPercent != -1 && partialSensorDataFillPercent != -1 && fullLivingHabitsPercent != -1
                    && partialLivingHabitsFillPercent != -1) {

                return new TestDataGeneratorConfig(userCount, startDate, endDate, fullSensorDataPercent, partialSensorDataFillPercent, fullLivingHabitsPercent, partialLivingHabitsFillPercent);

            } else {
                return new TestDataGeneratorConfig();
            }
        } catch (Exception ex) {
            if (userCount != -1 && startDate != new Date(0) && endDate != new Date(0)
                    && fullSensorDataPercent != -1 && partialSensorDataFillPercent != -1 && fullLivingHabitsPercent != -1
                    && partialLivingHabitsFillPercent != -1) {

                return new TestDataGeneratorConfig(userCount, startDate, endDate, fullSensorDataPercent, partialSensorDataFillPercent, fullLivingHabitsPercent, partialLivingHabitsFillPercent);

            } else {
                return new TestDataGeneratorConfig();
            }
        }
    }

    @Test
    public void testWriteLocations() {
        System.out.println("Starting to write locations");
        for (String location1 : location) {
            //LocationRepository locationRepository = new LocationRepository();
            Location.LocationDetails ld;
            String[] locationParts = location1.split("[|]");
            ld = new Location.LocationDetails(locationParts[0], locationParts[1], locationParts[2]);
            locationRepository.saveLocation(locationParts[2], locationParts[1], locationParts[0], ld);
        }
        System.out.println("Wrote locations!");
    }

    @Test
    public void testWriteAttachedDocument() {
        int numberOfUsers = tdgc.userCount;
        Date startDate = tdgc.startDate;
        Date endDate = tdgc.endDate;
        System.out.println("Starting to write attached documents for " + Integer.toString(numberOfUsers) + " users, between "
                + dateFormat.format(startDate) + " and " + dateFormat.format(endDate));

        Random rand = new Random();
        MedicalRecords.DocumentsAttached attachedDocument;
        int daysInInterval = getDaysInInterval(startDate, endDate);

        for (int i = 1; i <= numberOfUsers; i++) {
            attachedDocument = new MedicalRecords.DocumentsAttached(documentURLPattern + Integer.toString(i));
            medicalRecordsRepository.saveAttachedDocument(emailPattern.replace("<ID>", Integer.toString(i)), DateUtils.addDays(startDate, rand.nextInt(daysInInterval)), attachedDocument);
        }
        System.out.println("Finished writing attached documents!");
    }

    @Test
    public void testWriteMedicalRedord() {
        int numberOfUsers = tdgc.userCount;
        Date startDate = tdgc.startDate;
        Date endDate = tdgc.endDate;
        System.out.println("Starting to write medical records for " + Integer.toString(numberOfUsers) + " users, between "
                + dateFormat.format(startDate) + " and " + dateFormat.format(endDate));
        Random rand = new Random();
        Date medicalRecordDate;
        int bmi;
        int cholesterol;
        int triglycerides;
        String followUp;
        MedicalRecords.MedicalRecord medicalRecord;
        int daysInInterval = getDaysInInterval(startDate, endDate);
        
        for (int i = 1; i <= numberOfUsers; i++) {
            medicalRecordDate = DateUtils.addDays(startDate, rand.nextInt(daysInInterval));
            bmi = rand.nextInt(50);
            cholesterol = rand.nextInt(500);
            triglycerides = rand.nextInt(500);
            followUp = "Call by " + DateUtils.addDays(medicalRecordDate, rand.nextInt(100)).toString();
            medicalRecord = new MedicalRecords.MedicalRecord(medicalRecordDate, bmi, cholesterol, triglycerides, followUp);

            medicalRecordsRepository.saveMedicalRecord(emailPattern.replace("<ID>", Integer.toString(i)), medicalRecordDate, medicalRecord);
        }
        System.out.println("Finished writing medical records!");
    }

    @Test
    public void testWriteTestUsers() {
        int numberOfUsers = tdgc.userCount;
        System.out.println("Starting to write test users: " + Integer.toString(numberOfUsers));
        String rowKey;
        String secretKey = "sk";
        Boolean userStatus = true;
        String userPhone = "555-";
        Date dob = new Date();
        User.AccountData accData;
        User.PersonalData persData;
        User.MedicalNotes medNotes;
        Random rand = new Random();
        int locationID;
        for (int i = 1; i <= numberOfUsers; i++) {
            rowKey = emailPattern.replace("<ID>", Integer.toString(i));
            //account data
            secretKey = secretKey + Integer.toString(i);
            userPhone = userPhone + Integer.toString(i);
            accData = new User.AccountData(secretKey, userStatus, userPhone, rowKey);
            userRepository.saveAccountData(rowKey, accData);
            //personal data
            locationID = rand.nextInt(location.length);
            persData = new User.PersonalData(namePrefix + Integer.toString(i), dob, location[locationID]);
            userRepository.savePersonalData(rowKey, persData);
            //medical notes
            
            medNotes = new User.MedicalNotes(null, User.MedicalNotes.SMOKER.getRandom(), User.MedicalNotes.INHERITED_RISK.getRandom());
            userRepository.saveMedicalNotes(rowKey, medNotes);
            //TODO: family tree
        }
        System.out.println("Finished writing test users");
    }

    protected static int getDaysInInterval(Date startDate, Date endDate) {
        //get the number of days in the specified interval
        final long miliSecondsInADay = 1000 * 60 * 60 * 24;
        return Math.round((endDate.getTime() - startDate.getTime()) / miliSecondsInADay);
    }

    @Test
    public void testWriteFullTestSensorData() {
        int rangeFrom = 0;
        int rangeTo = Math.round(tdgc.userCount * tdgc.fullSensorDataPercent / 100);
        Date startDate = tdgc.startDate;
        Date endDate = tdgc.endDate;
        System.out.println("Starting to write full sensor data for users between" + Integer.toString(rangeFrom) + " and "
                + Integer.toString(rangeTo) + " and dates between "
                + dateFormat.format(startDate) + " and " + dateFormat.format(endDate));
        String currentActivityType;
        int activityDuration;
        Random rand = new Random();
        int avgHR, diastolicBP, systolicBP, calories;
        int daysInInterval = getDaysInInterval(startDate, endDate);
        for (int i = 0; i < daysInInterval; i++) {
            for (int j = rangeFrom; j <= rangeTo; j++) {
                SensorData sd;

                List<HourlyData> dailyData = new ArrayList<>();
                Date eventDate = DateUtils.addDays(startDate, i);

                for (int k = 0; k < 24; k++) {
                    HourlyData hh;
                    activityDuration = rand.nextInt(61);
                    currentActivityType = activityType[rand.nextInt(3)];
                    avgHR = rand.nextInt(200);
                    diastolicBP = 50 + rand.nextInt(100);
                    systolicBP = 100 + rand.nextInt(200);
                    calories = rand.nextInt(3000);
                    hh = new HourlyData(currentActivityType, activityDuration, avgHR, systolicBP, diastolicBP, calories);
                    dailyData.add(hh);
                }

                sd = new SensorData(eventDate, dailyData.get(0), dailyData.get(1), dailyData.get(2),
                        dailyData.get(3), dailyData.get(4), dailyData.get(5),
                        dailyData.get(6), dailyData.get(7), dailyData.get(8),
                        dailyData.get(9), dailyData.get(10), dailyData.get(11),
                        dailyData.get(12), dailyData.get(13), dailyData.get(14),
                        dailyData.get(15), dailyData.get(16), dailyData.get(17),
                        dailyData.get(18), dailyData.get(19), dailyData.get(20),
                        dailyData.get(21), dailyData.get(22), dailyData.get(23)
                );
                sensorDataRepository.saveSensorData(emailPattern.replace("<ID>", Integer.toString(j)), eventDate, sd);
            }
        }
        System.out.println("Finished generating full sensor data!");
    }

    @Test
    public void testWritePartialTestSensorData() {
        int rangeFrom = Math.round(tdgc.userCount * tdgc.fullSensorDataPercent / 100) + 1;
        int rangeTo = tdgc.userCount;
        Date startDate = tdgc.startDate; 
        Date endDate = tdgc.endDate; 
        int completeDataPercent = tdgc.partialSensorDataFillPercent;
        System.out.println("Starting to write partial sensor data for users between " + Integer.toString(rangeFrom) + " and "
                + Integer.toString(rangeTo) + " and dates between "
                + dateFormat.format(startDate) + " and " + dateFormat.format(endDate));
        String currentActivityType;
        int activityDuration;
        Random rand = new Random();
        int avgHR, diastolicBP, systolicBP, calories;
        int daysInInterval = getDaysInInterval(startDate, endDate);
        int daysWithDataToWrite = Math.round(daysInInterval * completeDataPercent / 100);

        for (int i = 1; i <= daysWithDataToWrite; i++) {
            for (int j = rangeFrom; j <= rangeTo; j++) {
                SensorData sd;
                Date eventDate = DateUtils.addDays(startDate, i);

                List<HourlyData> dailyData = new ArrayList<>();

                for (int k = 0; k < 24; k++) {
                    HourlyData hourlyData;
                    activityDuration = rand.nextInt(61);
                    currentActivityType = activityType[rand.nextInt(3)];
                    avgHR = rand.nextInt(200);
                    diastolicBP = 50 + rand.nextInt(100);
                    systolicBP = 100 + rand.nextInt(200);
                    calories = rand.nextInt(3000);
                    hourlyData = new HourlyData(currentActivityType, activityDuration, avgHR, systolicBP, diastolicBP, calories);

                    dailyData.add(hourlyData);
                }

                sd = new SensorData(eventDate, dailyData.get(0), dailyData.get(1), dailyData.get(2),
                        dailyData.get(3), dailyData.get(4), dailyData.get(5),
                        dailyData.get(6), dailyData.get(7), dailyData.get(8),
                        dailyData.get(9), dailyData.get(10), dailyData.get(11),
                        dailyData.get(12), dailyData.get(13), dailyData.get(14),
                        dailyData.get(15), dailyData.get(16), dailyData.get(17),
                        dailyData.get(18), dailyData.get(19), dailyData.get(20),
                        dailyData.get(21), dailyData.get(22), dailyData.get(23)
                );
                sensorDataRepository.saveSensorData(emailPattern.replace("<ID>", Integer.toString(j)), eventDate, sd);

            }
        }
        System.out.println("Finished writing partial sensor data!");
    }

    @Test
    public void testWriteFullTestLivingHabitsData() {
        int rangeFrom = 0;
        int rangeTo = Math.round(tdgc.userCount * tdgc.fullLivingHabitsPercent / 100);
        Date startDate = tdgc.startDate;
        Date endDate = tdgc.endDate;
        System.out.println("Starting to full living habits for users between " + Integer.toString(rangeFrom) + " and "
                + Integer.toString(rangeTo) + " and dates between "
                + dateFormat.format(startDate) + " and " + dateFormat.format(endDate));
        Random rand = new Random();
        int minsInDay = 1440;
        int minsOfSleep;
        int minsOfExcercise;
        int calories;
        int energy;
        int fat;
        int saturatedFat;
        int sugars;
        int sodium;
        int protein;
        int carbohydrates;
        String vitamins;
        String minerals;
        int water;
        int alcohol;
        int softDrinks;
        BigDecimal riskFactor = new BigDecimal(1.0);

        int daysInInterval = getDaysInInterval(startDate, endDate);
        for (int i = 1; i <= daysInInterval; i++) {
            for (int j = rangeFrom; j <= rangeTo; j++) {
                LivingData ld;
                minsOfSleep = rand.nextInt(minsInDay);
                minsOfExcercise = rand.nextInt(minsInDay - minsOfSleep);
                calories = rand.nextInt(5000);
                energy = rand.nextInt(5000);
                fat = rand.nextInt(1000);
                saturatedFat = rand.nextInt(1000);
                sugars = rand.nextInt(1000);
                sodium = rand.nextInt(100);
                protein = rand.nextInt(1000);
                carbohydrates = rand.nextInt(1000);
                vitamins = vitaminsLookup[rand.nextInt(vitaminsLookup.length)];
                minerals = mineralsLookup[rand.nextInt(mineralsLookup.length)];
                water = rand.nextInt(10000);
                alcohol = rand.nextInt(10000);
                softDrinks = rand.nextInt(10000);
                ld = new LivingData(minsOfSleep, minsOfExcercise, calories, energy, fat, saturatedFat, sugars, sodium,
                        protein, carbohydrates, vitamins, minerals, water, alcohol, softDrinks, riskFactor);
                livingDataRepository.saveLivingData(emailPattern.replace("<ID>", Integer.toString(j)), DateUtils.addDays(startDate, i), ld);
            }
        }
        System.out.println("Finished writing full living habits!");
    }

    @Test
    public void testWritePartialTestLivingHabitsData() {
        int rangeFrom = Math.round(tdgc.userCount * tdgc.fullLivingHabitsPercent / 100) + 1;
        int rangeTo = tdgc.userCount;
        Date startDate = tdgc.startDate; 
        Date endDate = tdgc.endDate; 
        int completeDataPercent = tdgc.partialLivingHabitsFillPercent;
        System.out.println("Starting to write partial living habits for users between " + Integer.toString(rangeFrom) + " and "
                + Integer.toString(rangeTo) + " and dates between "
                + dateFormat.format(startDate) + " and " + dateFormat.format(endDate));
        Random rand = new Random();
        final int minsInDay = 1440;
        int minsOfSleep;
        int minsOfExcercise;
        int calories;
        int energy;
        int fat;
        int saturatedFat;
        int sugars;
        int sodium;
        int protein;
        int carbohydrates;
        String vitamins;
        String minerals;
        int water;
        int alcohol;
        int softDrinks;
        BigDecimal riskFactor = new BigDecimal(1.0);
        LivingData ld;
        int daysInInterval = getDaysInInterval(startDate, endDate);
        int daysWithDataToWrite = Math.round(daysInInterval * completeDataPercent / 100);

        for (int i = 1; i <= daysWithDataToWrite; i++) {
            for (int j = rangeFrom; j <= rangeTo; j++) {
                minsOfSleep = rand.nextInt(minsInDay);
                minsOfExcercise = rand.nextInt(minsInDay - minsOfSleep);
                calories = rand.nextInt(5000);
                energy = rand.nextInt(5000);
                fat = rand.nextInt(1000);
                saturatedFat = rand.nextInt(1000);
                sugars = rand.nextInt(1000);
                sodium = rand.nextInt(100);
                protein = rand.nextInt(1000);
                carbohydrates = rand.nextInt(1000);
                vitamins = vitaminsLookup[rand.nextInt(vitaminsLookup.length)];
                minerals = mineralsLookup[rand.nextInt(mineralsLookup.length)];
                water = rand.nextInt(10000);
                alcohol = rand.nextInt(10000);
                softDrinks = rand.nextInt(10000);
                ld = new LivingData(minsOfSleep, minsOfExcercise, calories, energy, fat, saturatedFat, sugars, sodium,
                        protein, carbohydrates, vitamins, minerals, water, alcohol, softDrinks, riskFactor);
                livingDataRepository.saveLivingData(emailPattern.replace("<ID>", Integer.toString(j)), DateUtils.addDays(startDate, i), ld);
            }
        }
        System.out.println("Finished writing partial living habits!");
    }

    public class TestDataGeneratorConfig {

        private int userCount;
        private Date startDate;
        private Date endDate;
        private int fullSensorDataPercent;
        private int partialSensorDataFillPercent;
        private int fullLivingHabitsPercent;
        private int partialLivingHabitsFillPercent;

        TestDataGeneratorConfig(int cUserCount, Date cStartDate, Date cEndDate, int cFullSensorDataPercent, int cPartialSensorDataFillPercent, int cDullLivingHabitsPercent, int cPartialLivingHabitsFillPercent) {
            this.userCount = cUserCount;
            this.startDate = cStartDate;
            this.endDate = cEndDate;
            this.fullSensorDataPercent = cFullSensorDataPercent;
            this.partialSensorDataFillPercent = cPartialSensorDataFillPercent;
            this.fullLivingHabitsPercent = cPartialLivingHabitsFillPercent;
            this.partialLivingHabitsFillPercent = cPartialLivingHabitsFillPercent;
        }

        TestDataGeneratorConfig() {
            this.userCount = 50;
            this.startDate = new Date();
            this.endDate = new Date();
            this.fullSensorDataPercent = 20;
            this.partialSensorDataFillPercent = 30;
            this.fullLivingHabitsPercent = 40;
            this.partialLivingHabitsFillPercent = 50;
        }

    }

}
