package com.threepillarglobal.labs.cdps.dao.config;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.MedicalRecords;
import com.threepillarglobal.labs.cdps.domain.Location;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import javax.annotation.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
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
        //admin = new HBaseAdmin(config);
        //createTables();
    }

    private void createTables() throws Exception {
        createTable(User.class);
        createTable(LivingData.class);
        createTable(SensorData.class);
        createTable(MedicalRecords.class);
        createTable(Location.class);
    }

    private void createTable(Class<?> clazz) throws Exception {
        String tableName = HAnnotation.getTableName(clazz);
        if (!admin.tableExists(tableName)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            {
                for (String columnFamily : HAnnotation.getColumnFamilyNames(clazz)) {
                    tableDescriptor.addFamily(new HColumnDescriptor(columnFamily));
                }
            }
            admin.createTable(tableDescriptor);
        }
    }
}
