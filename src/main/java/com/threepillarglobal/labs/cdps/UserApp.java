package com.threepillarglobal.labs.cdps;

import com.threepillarglobal.labs.cdps.dao.repository.UserRepository;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.util.UserUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserApp {

    private static final Logger L = LoggerFactory.getLogger(UserApp.class);

    public static void main(String[] args) throws Exception {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("/META-INF/spring/application-context.xml", UserApp.class);
        L.info("HBase Application Running");
        context.registerShutdownHook();

        UserUtils userUtils = context.getBean(UserUtils.class);
        {
            userUtils.initialize();
            userUtils.addUsers();
        }

        UserRepository userRepository = context.getBean(UserRepository.class);
        List<User> users = userRepository.findAll();
        System.out.println("Number of users = " + users.size());
        System.out.println(users);
    }
}
