package com.threepillarglobal.labs.cdps.dao.config;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.MedicalRecords;
import com.threepillarglobal.labs.cdps.domain.Location;
import com.threepillarglobal.labs.hbase.util.HOperations;
import javax.annotation.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class DDL implements InitializingBean {

    @Resource(name = "hbaseConfiguration")
    private Configuration config;

    private HBaseAdmin admin;

    @Override
    public void afterPropertiesSet() throws Exception {
        admin = new HBaseAdmin(config);
        createTables();
    }

    private void createTables() throws Exception {
        HOperations.createTable(User.class, admin);
        HOperations.createTable(LivingData.class, admin);
        HOperations.createTable(SensorData.class, admin);
        HOperations.createTable(MedicalRecords.class, admin);
        HOperations.createTable(Location.class, admin);
    }
}
