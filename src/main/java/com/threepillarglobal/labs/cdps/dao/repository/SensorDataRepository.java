package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import org.apache.hadoop.hbase.client.*;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;


@Repository
public class SensorDataRepository {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    public SensorData saveSensorData(final String email, final Date eventDate, final SensorData sensorData) {
        final String tableName = HAnnotation.getTableName(SensorData.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(SensorData.class);
        return hbaseTemplate.execute(tableName, new TableCallback<SensorData>() {
            @Override
            public SensorData doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(SensorData.toRowKey(email, eventDate).getBytes());
                HMarshaller.marshall(sensorData, p);
                table.put(p);
                return sensorData;
            }
        });
    }

    public List<SensorData> findSensorDataForUserAtDate(final String email, final Date eventDate)
    {
        final String tableName = HAnnotation.getTableName(SensorData.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(SensorData.class);
        Scan s = new Scan(new Get(SensorData.toRowKey(email, eventDate).getBytes()));
        return hbaseTemplate.find(tableName, s,  new RowMapper<SensorData>() {
            @Override
            public SensorData mapRow(Result result, int i) throws Exception {
                return HMarshaller.unmarshall(SensorData.class, result);
            }
        });
    }

    public List<SensorData> findSensorDataForUserInInterval(final String email, final Date eventStartDate, final Date eventEndDate)
    {
        final String tableName = HAnnotation.getTableName(SensorData.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(SensorData.class);
        Scan s = new Scan(SensorData.toRowKey(email, eventStartDate).getBytes(), SensorData.toRowKey(email, eventEndDate).getBytes());
        return hbaseTemplate.find(tableName, s,  new RowMapper<SensorData>() {
            @Override
            public SensorData mapRow(Result result, int i) throws Exception {
                return HMarshaller.unmarshall(SensorData.class, result);
            }
        });
    }
}

