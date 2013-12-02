package com.threepillarglobal.labs.cpds;

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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.threepillarglobal.labs.cdps.dao.repository.*;
import com.threepillarglobal.labs.cdps.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang.time.DateUtils;

public class TestDataGeneratorIT {
    
    private static String emailPattern = "user<ID>@3pg.com";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

    public TestDataGeneratorConfig readDataGeneratorConfig() throws Exception {
        Date startDate = new Date(0),
                endDate = new Date(0);
        int userCount = -1,
                fullSensorDataPercent = -1,
                partialSensorDataFillPercent = -1,
                fullLivingHabitsPercent = -1,
                partialLivingHabitsFillPercent = -1;

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
        if (userCount != -1 && startDate != new Date(0) && endDate != new Date(0) &&
                fullSensorDataPercent != -1 && partialSensorDataFillPercent != -1 && fullLivingHabitsPercent != -1 &&
                partialLivingHabitsFillPercent != -1) {

            return new TestDataGeneratorConfig(userCount, startDate, endDate, fullSensorDataPercent, partialSensorDataFillPercent, fullLivingHabitsPercent, partialLivingHabitsFillPercent);

        } else {
            return new TestDataGeneratorConfig();
        }
    }

    public void GenerateData (TestDataGeneratorConfig tdgc)
    {
        writeTestUsers(tdgc.userCount);

        int usersWithFullSensorData = Math.round(tdgc.userCount * tdgc.fullSensorDataPercent / 100);
        writeFullTestSensorData(0, usersWithFullSensorData, tdgc.startDate, tdgc.endDate);

        writePartialTestSensorData(0, tdgc.userCount - usersWithFullSensorData, tdgc.startDate, tdgc.endDate, tdgc.partialSensorDataFillPercent);

        int usersWithFullLivingHabitsData = Math.round(tdgc.userCount * tdgc.fullLivingHabitsPercent / 100);
        writeFullTestLivingHabitsData(0, usersWithFullLivingHabitsData, tdgc.startDate, tdgc.endDate);
        writePartialTestLivingHabitsData(0, tdgc.userCount - usersWithFullLivingHabitsData, tdgc.startDate, tdgc.endDate, tdgc.partialLivingHabitsFillPercent);

    }

    protected static void writeTestUsers(int numberOfUsers)
    {
        String rowKey;
        String secretKey = "sk";
        Boolean userStatus = true;
        String userPhone = "555-";
        String name = "John Doe #";
        Date dob = new Date();
        UserRepository userRepo = new UserRepository();
        User.AccountData accData;
        User.PersonalData persData;
        User.MedicalNotes medNotes;
        Random rand = new Random();
        int locationID;
        Boolean smoker;
        String[] location = {"Cluj-Napoca", "Timisoara", "Washington"};
        for(int i = 1; i <= numberOfUsers; i++ )
        {
            rowKey = emailPattern.replace("<ID>", Integer.toString(i));
            //account data
            secretKey = secretKey + Integer.toString(i);
            userPhone = userPhone + Integer.toString(i);
            accData = new User.AccountData(secretKey, userStatus, userPhone);
            userRepo.saveAccountData(rowKey, accData);
            //personal data
            name = name + Integer.toString(i);
            locationID = rand.nextInt(3);
            persData = new User.PersonalData(name, dob, location[locationID]);
            userRepo.savePersonalData(rowKey, persData);
            //medical notes
            smoker = rand.nextBoolean();
            medNotes = new User.MedicalNotes(null, smoker, Integer.toString(locationID));
            userRepo.saveMedicalNotes(rowKey, medNotes);
            //TODO: family tree
            
            //TODO: call method that writes in hbase
        }
    }

    protected static int getDaysInInterval(Date startDate, Date endDate)
    {
        //get the number of days in the specified interval
        final long miliSecondsInADay = 1000 * 60 * 60 * 24;
        return Math.round((endDate.getTime() - startDate.getTime()) / miliSecondsInADay);
    }

    protected static void writeFullTestSensorData(int rangeFrom, int rangeTo, Date startDate, Date endDate)
    {
        String[] activityType = {"Eating", "Sleeping", "Exercising"};
        String at;
        SensorDataRepository sdRepo = new SensorDataRepository();
        int activityDuration;
        Random rand = new Random();
        int avgHR, avgBP, calories;
        int daysInInterval = getDaysInInterval(startDate, endDate);
        for(int i = 0; i < daysInInterval; i++)
        {
            for(int j = rangeFrom; j<= rangeTo; j++)
            {
                SensorData sd;
                
                    List<List<String>> dailyData = new ArrayList<>();
                    
                    for(int k = 0; k < 24; k++)
                    {
                        List<String> l = new ArrayList<>();
                        activityDuration = rand.nextInt(61);
                        at = activityType[rand.nextInt(3)];
                        avgHR = rand.nextInt(200);
                        avgBP = rand.nextInt(300);
                        calories = rand.nextInt(3000);
                        l.add(Integer.toString(activityDuration));
                        l.add(at);
                        l.add(Integer.toString(avgHR));
                        l.add(Integer.toString(avgBP));
                        l.add(Integer.toString(calories));
                        dailyData.add(l);
                    }
                    
                    sd = new SensorData(dailyData.get(0),dailyData.get(1),dailyData.get(2),
                                        dailyData.get(3),dailyData.get(4),dailyData.get(5),
                                        dailyData.get(6),dailyData.get(7),dailyData.get(8),
                                        dailyData.get(9),dailyData.get(10),dailyData.get(11),
                                        dailyData.get(12),dailyData.get(13),dailyData.get(14),
                                        dailyData.get(15),dailyData.get(16),dailyData.get(17),
                                        dailyData.get(18),dailyData.get(19),dailyData.get(20),
                                        dailyData.get(21),dailyData.get(22),dailyData.get(23)
                    );
                    sdRepo.saveSensorData(emailPattern.replace("<ID>", Integer.toString(j)), DateUtils.addDays(startDate, i), sd);
                
            }
            //TODO: gather params for sensor & call hbase write method
        }
    }

    protected static void writePartialTestSensorData(int rangeFrom, int RangeTo, Date startDate, Date endDate, int completeDataPercent)
    {
        int daysInInterval = getDaysInInterval(startDate, endDate);
        int daysWithDataToWrite = Math.round(daysInInterval * completeDataPercent / 100);

        for(int i = 1; i <= daysWithDataToWrite; i++)
        {
            //TODO: gather params for sensor & call hbase write method
        }
    }
    protected static void writeFullTestLivingHabitsData(int rangeFrom, int RangeTo, Date startDate, Date endDate)
    {
        int daysInInterval = getDaysInInterval(startDate, endDate);
        for(int i = 1; i <= daysInInterval; i++)
        {
            //TODO: gather params for sensor & call hbase write method
        }
    }

    protected static void writePartialTestLivingHabitsData(int rangeFrom, int RangeTo, Date startDate, Date endDate, int completeDataPercent)
    {
        int daysInInterval = getDaysInInterval(startDate, endDate);
        int daysWithDataToWrite = Math.round(daysInInterval * completeDataPercent / 100);

        for(int i = 1; i <= daysWithDataToWrite; i++)
        {
            //TODO: gather params for sensor & call hbase write method
        }
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

