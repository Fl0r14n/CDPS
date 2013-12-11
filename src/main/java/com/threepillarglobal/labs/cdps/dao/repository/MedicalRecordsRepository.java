package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.MedicalRecords.*;
import com.threepillarglobal.labs.cdps.domain.MedicalRecords;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class MedicalRecordsRepository {
    
    @Autowired
    private HbaseTemplate hbaseTemplate;

    public MedicalRecord saveMedicalRecord(final String email, final Date recordDate, final MedicalRecord medicalRecord) {
        final String tableName = HAnnotation.getTableName(MedicalRecord.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(MedicalRecord.class);
        return hbaseTemplate.execute(tableName, new TableCallback<MedicalRecord>() {
            @Override
            public MedicalRecord doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(MedicalRecords.toRowKey(email, recordDate));
                HMarshaller.marshall(medicalRecord, p);
                table.put(p);
                return medicalRecord;
            }
        });

    }

    public DocumentsAttached saveAttachedDocument(final String email, final Date recordDate, final DocumentsAttached attachedDocument)
    {
        final String tableName = HAnnotation.getTableName(DocumentsAttached.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(DocumentsAttached.class);
        return hbaseTemplate.execute(tableName, new TableCallback<DocumentsAttached>() {
            @Override
            public DocumentsAttached doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(MedicalRecords.toRowKey(email, recordDate));
                HMarshaller.marshall(attachedDocument, p);
                table.put(p);
                return attachedDocument;
            }
        });

    }
}
