package com.threepillarglobal.labs.cdps.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.MedicalNotes.INHERITED_RISK;

public class MockDataGenerator {

    private static String emailPattern = "user<ID>@3pg.com";
    private static String namePrefix = "John Doe #";
    private static String[] location = {"Romania|Cluj|Cluj-Napoca", "Romania|Timisoara|Timisoara", "USA|Washington|Washington DC"};

    public static List<User> fetchMockUserData(int numberOfUsers) {
        String rowKey;
        String secretKey = "sk";
        Boolean userStatus = true;
        String userPhone = "555-";
        Date dob = new Date();
        //UserRepository userRepo = new UserRepository();
        User.AccountData accData;
        User.PersonalData persData;
        User.MedicalNotes medNotes;
        Random rand = new Random();
        int locationID;
        Boolean smoker;
        List<User> userList = new ArrayList<User>();
        for (int i = 1; i <= numberOfUsers; i++) {

            rowKey = emailPattern.replace("<ID>", Integer.toString(i));
            //account data
            secretKey = secretKey + Integer.toString(i);
            userPhone = userPhone + Integer.toString(i);
            accData = new User.AccountData(secretKey, userStatus, userPhone);
	            //userRepo.saveAccountData(rowKey, accData);
            //personal data
            namePrefix = namePrefix + Integer.toString(i);
            locationID = rand.nextInt(location.length);
            persData = new User.PersonalData(namePrefix, dob, location[locationID]);
	            //userRepo.savePersonalData(rowKey, persData);
            //medical notes
            smoker = rand.nextBoolean();
            medNotes = new User.MedicalNotes(null, User.MedicalNotes.SMOKER.CASUAL, INHERITED_RISK.HIGH);
	            //userRepo.saveMedicalNotes(rowKey, medNotes);
            //TODO: family tree
            User user = new User(accData, persData, medNotes);
            userList.add(user);
        }
        return userList;
    }

}
