package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.*;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import java.util.List;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    public List<AccountData> findAllAccountData() {
        final String tableName = HAnnotation.getTableName(User.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(AccountData.class);
        return hbaseTemplate.find(tableName, cfamilyName, new RowMapper<AccountData>() {
            @Override
            public AccountData mapRow(Result result, int rowNum) throws Exception {
                return HMarshaller.unmarshall(AccountData.class, result);
            }
        });
    }

    public AccountData saveAccountData(final String email, final AccountData account) {
        final String tableName = HAnnotation.getTableName(User.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(AccountData.class);
        return hbaseTemplate.execute(tableName, new TableCallback<AccountData>() {
            @Override
            public AccountData doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email).getDigest());
                HMarshaller.marshall(account, p);
                table.put(p);
                return account;
            }
        });
    }
    
    public PersonalData savePersonalData(final String email, final PersonalData personalData) {
        final String tableName = HAnnotation.getTableName(User.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(PersonalData.class);
        return hbaseTemplate.execute(tableName, new TableCallback<PersonalData>() {
            @Override
            public PersonalData doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email).getDigest());
                HMarshaller.marshall(personalData, p);
                table.put(p);
                return personalData;
            }
        });
    }
    
    public MedicalNotes saveMedicalNotes(final String email, final MedicalNotes medicalNotes) {
        final String tableName = HAnnotation.getTableName(User.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(MedicalNotes.class);
        return hbaseTemplate.execute(tableName, new TableCallback<MedicalNotes>() {
            @Override
            public MedicalNotes doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email).getDigest());
                HMarshaller.marshall(medicalNotes, p);
                table.put(p);
                return medicalNotes;
            }
        });
    }
    
    public FamilyTree saveFamilyTree(final String email, final FamilyTree familyTree) {
        final String tableName = HAnnotation.getTableName(User.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(FamilyTree.class);
        return hbaseTemplate.execute(tableName, new TableCallback<FamilyTree>() {
            @Override
            public FamilyTree doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email).getDigest());
                HMarshaller.marshall(familyTree, p);
                table.put(p);
                return familyTree;
            }
        });
    }
}
