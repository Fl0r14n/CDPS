package com.threepillarglobal.labs.cdps.dao.config;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.MedicalRecords;
import com.threepillarglobal.labs.cdps.domain.Location;
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
        admin = new HBaseAdmin(config);
        createTables();
    }

    private void createTables() throws Exception {
        //TODO create tables
        if (!admin.tableExists(User.TABLE)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(User.TABLE);
            {
                tableDescriptor.addFamily(new HColumnDescriptor(User.AccountData.CFAMILY));
                tableDescriptor.addFamily(new HColumnDescriptor(User.PersonalData.CFAMILY));
                tableDescriptor.addFamily(new HColumnDescriptor(User.FamilyTree.CFAMILY));
                tableDescriptor.addFamily(new HColumnDescriptor(User.MedicalNotes.CFAMILY));
            }
            admin.createTable(tableDescriptor);
        }
        if (!admin.tableExists(LivingData.TABLE)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(LivingData.TABLE);
            {
                tableDescriptor.addFamily(new HColumnDescriptor(LivingData.DailyPatientProfile.CFAMILY));
            }
            admin.createTable(tableDescriptor);
        }
        if (!admin.tableExists(SensorData.TABLE)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(SensorData.TABLE);
            {
                tableDescriptor.addFamily(new HColumnDescriptor(SensorData.GatheredData.CFAMILY));
            }
            admin.createTable(tableDescriptor);
        }
        if (!admin.tableExists(MedicalRecords.TABLE)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(MedicalRecords.TABLE);
            {
                tableDescriptor.addFamily(new HColumnDescriptor(MedicalRecords.MedicalRecord.CFAMILY));
                tableDescriptor.addFamily(new HColumnDescriptor(MedicalRecords.DocumentsAttached.CFAMILY));
            }
            admin.createTable(tableDescriptor);
        }
        if (!admin.tableExists(Location.TABLE)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(Location.TABLE);
            {
                tableDescriptor.addFamily(new HColumnDescriptor(Location.LocationDetails.CFAMILY));
                tableDescriptor.addFamily(new HColumnDescriptor(Location.Residents.CFAMILY));
            }
            admin.createTable(tableDescriptor);
        }
    }
}
