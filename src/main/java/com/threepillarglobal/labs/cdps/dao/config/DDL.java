package com.threepillarglobal.labs.cdps.dao.config;

import com.threepillarglobal.labs.cdps.domain.User;
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
                //TODO add the rest of the column families
            }
            admin.createTable(tableDescriptor);
        }
    }
}
