package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.hbase.repository.HRepository;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SensorDataRepository {

    @Autowired
    public SensorDataRepository(HbaseTemplate hbaseTemplate) {
        sensorDataRepo = new HRepository<SensorData>(SensorData.class, hbaseTemplate) {
        };
    }
    private final HRepository<SensorData> sensorDataRepo;

    public SensorData saveSensorData(final String email, final Date eventDate, final SensorData sensorData) {
        return sensorDataRepo.save(SensorData.toRowKey(email, eventDate), sensorData);
    }

    public SensorData findSensorDataForUserAtDate(final String email, final Date eventDate) {
        return sensorDataRepo.findOne(SensorData.toRowKey(email, eventDate));

    }

    public List<SensorData> findSensorDataForUserInInterval(final String email, final Date eventStartDate, final Date eventEndDate) {
        return sensorDataRepo.findAll(SensorData.toRowKey(email, eventStartDate), SensorData.toRowKey(email, eventEndDate));
    }
}
