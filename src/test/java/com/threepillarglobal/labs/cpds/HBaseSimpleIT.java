package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.cdps.dao.repository.UserRepository;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.AccountData;
import com.threepillarglobal.labs.hbase.util.HOperations;
import java.io.IOException;
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

/**
 * This class will be used to insert mock data into HBase
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:integrationTests-context.xml")
public class HBaseSimpleIT {

    private static final Logger L = LoggerFactory.getLogger(HBaseSimpleIT.class);

    @Resource(name = "hbaseConfiguration")
    private Configuration config;
    
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws IOException {
        //table should be created at startup by DDL but to be sure
        HOperations.createTable(User.class, new HBaseAdmin(config));
    }

    @After
    public void tearDown() throws IOException {
        //delete table
        HOperations.deleteTable(User.class, new HBaseAdmin(config));
    }

    @Test
    public void populate_hbase_with_some_accont_data_print_it_then_delete_it() {
        //TODO
        for (int i = 0; i < 10; i++) {
            AccountData ad = new AccountData("secret" + i, (i % 2 == 1) ? Boolean.TRUE : Boolean.FALSE, "072900000" + i);
            userRepository.saveAccountData("key@" + i, ad);
        }
        for (AccountData ad : userRepository.findAllAccountData()) {
            System.out.println(ad.toString());
        }
    }

//    public static void main(String[] args) {
//        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/application-context.xml", HBaseSimpleIT.class);
//        L.info("HBase Application Running");
//        context.registerShutdownHook();
//        UserRepository userRepository = context.getBean(UserRepository.class);
//    }
}
