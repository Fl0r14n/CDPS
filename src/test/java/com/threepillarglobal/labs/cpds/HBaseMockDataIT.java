package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.cdps.dao.repository.UserRepository;
import com.threepillarglobal.labs.cdps.domain.User.AccountData;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This class will be used to insert mock data into HBase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:integrationTests-context.xml")
@Ignore
public class HBaseMockDataIT {

    private static final Logger L = LoggerFactory.getLogger(HBaseMockDataIT.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Before
    public void setUp() {
        //TODO
        //TODO delete table first???
    }
    
    @After
    public void tearDown() {
        //TODO
    }
    
    @Test
    public void populateDB() {
        //TODO
        for(int i=0;i<10;i++) {
            AccountData ad = new AccountData("secret"+i, (i%2==1)?Boolean.TRUE:Boolean.FALSE, "072900000"+i);
            userRepository.saveAccountData("key@"+i, ad);
        }
    }
    
    @Test
    public void print_all_user_data() {
        for(AccountData ad :userRepository.findAllAccountData()) {
            System.out.println(ad.toString());
        }
    }
    
    
//    public static void main(String[] args) {
//        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/application-context.xml", HBaseMockDataIT.class);
//        L.info("HBase Application Running");
//        context.registerShutdownHook();
//        UserRepository userRepository = context.getBean(UserRepository.class);
//    }
}
