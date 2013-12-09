package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import java.util.Date;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

@Repository
public class LivingDataRepository {
    
    @Autowired
    private HbaseTemplate hbaseTemplate;
    
    public LivingData saveLivingData(final String email, final Date eventDate, final LivingData livingData) {
        final String tableName = HAnnotation.getTableName(LivingData.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(LivingData.class);
        return hbaseTemplate.execute(tableName, new TableCallback<LivingData>() {
            @Override
            public LivingData doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(LivingData.toRowKey(email, eventDate).getBytes());
                HMarshaller.marshall(livingData, p);
                table.put(p);
                return livingData;
            }
        });
    }
}
