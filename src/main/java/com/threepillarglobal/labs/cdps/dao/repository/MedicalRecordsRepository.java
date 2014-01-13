package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.MedicalRecords.*;
import com.threepillarglobal.labs.cdps.domain.MedicalRecords;
import com.threepillarglobal.labs.hbase.repository.HRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

@Repository
public class MedicalRecordsRepository {

    @Autowired
    public MedicalRecordsRepository(HbaseTemplate hbaseTemplate) {
        medicalRecordRepo = new HRepository<MedicalRecord>(MedicalRecords.class, hbaseTemplate) {
        };
        documentsAttachedRepo = new HRepository<DocumentsAttached>(MedicalRecords.class, hbaseTemplate) {
        };
    }
    private final HRepository<MedicalRecord> medicalRecordRepo;
    private final HRepository<DocumentsAttached> documentsAttachedRepo;

    public MedicalRecord saveMedicalRecord(final String email, final Date recordDate, final MedicalRecord medicalRecord) {
        return medicalRecordRepo.save(MedicalRecords.toRowKey(email, recordDate), medicalRecord);
    }
    
    public void saveMedicalRecords(Map<byte[], MedicalRecord> medicalRecords) {
        medicalRecordRepo.save(medicalRecords);
    }

    public DocumentsAttached saveAttachedDocument(final String email, final Date recordDate, final DocumentsAttached attachedDocument) {
        return documentsAttachedRepo.save(MedicalRecords.toRowKey(email, recordDate), attachedDocument);
    }
    
    public void saveAttachedDocuments(Map<byte[], DocumentsAttached> attachedDocuments) {
        documentsAttachedRepo.save(attachedDocuments);
    }
}
