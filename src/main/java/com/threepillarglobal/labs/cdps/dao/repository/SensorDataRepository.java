package com.threepillarglobal.labs.cdps.dao.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SensorDataRepository {
    
    @Autowired
    private HbaseTemplate hbaseTemplate;
}
