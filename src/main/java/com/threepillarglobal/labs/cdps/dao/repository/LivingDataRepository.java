package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.hbase.repository.HRepository;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LivingDataRepository {

    @Autowired
    public LivingDataRepository(HbaseTemplate hbaseTemplate) {
        livingDataRepo = new HRepository<LivingData>(LivingData.class, hbaseTemplate) {
        };
    }
    private final HRepository<LivingData> livingDataRepo;
    
    
    @Autowired
    private HbaseTemplate hbaseTemplate;
    
    public LivingData saveLivingData(final String email, final Date eventDate, final LivingData livingData) {
        return livingDataRepo.save(LivingData.toRowKey(email, eventDate), livingData);
    }
    
    public void saveLivingData(Map<byte[], LivingData> livingData) {
        livingDataRepo.save(livingData);
    }
}
