package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.*;
import com.threepillarglobal.labs.hbase.repository.HRepository;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import java.util.List;

import org.apache.hadoop.hbase.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private HbaseTemplate hbaseTemplate;
    
    
//    HRepository<PersonalData> personalDataRepo = new HRepository<>(User.class);
//    HRepository<MedicalNotes> medicalNotesRepo = new HRepository<>(User.class);
//    HRepository<FamilyTree> familyTreeRepo = new HRepository<>(User.class);

    public UserRepository() {
        this.accountDataRepo = new HRepository<>(User.class);
    }
    private final HRepository<AccountData> accountDataRepo;

    public List<AccountData> findAllAccountData() {
        return accountDataRepo.findAll();
//        final String tableName = HAnnotation.getTableName(User.class);
//        final String cfamilyName = HAnnotation.getColumnFamilyName(AccountData.class);
//        return hbaseTemplate.find(tableName, cfamilyName, new RowMapper<AccountData>() {
//            @Override
//            public AccountData mapRow(Result result, int rowNum) throws Exception {
//                return HMarshaller.unmarshall(AccountData.class, result);
//            }
//        });
    }

    public AccountData findAccountDataForUser(final byte[] rowKey) {
        final String tableName = HAnnotation.getTableName(User.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(AccountData.class);
        return hbaseTemplate.get(tableName, new String(rowKey), cfamilyName, new RowMapper<AccountData>() {

            @Override
            public AccountData mapRow(Result result, int i) throws Exception {
                return HMarshaller.unmarshall(AccountData.class, result);
            }
        });        
    }

    public AccountData saveAccountData(final String email, final AccountData account) {
        return accountDataRepo.save(User.toRowKey(email), account);
//        final String tableName = HAnnotation.getTableName(User.class);
//        return hbaseTemplate.execute(tableName, new TableCallback<AccountData>() {
//            @Override
//            public AccountData doInTable(HTableInterface table) throws Throwable {
//                Put p = new Put(User.toRowKey(email));
//                HMarshaller.marshall(account, p);
//                table.put(p);                
//                return account;
//            }
//        });
    }

    public List<PersonalData> findAllPersonalData() {
        final String tableName = HAnnotation.getTableName(User.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(PersonalData.class);
        return hbaseTemplate.find(tableName, cfamilyName, new RowMapper<PersonalData>() {
            @Override
            public PersonalData mapRow(Result result, int rowNum) throws Exception {
                return HMarshaller.unmarshall(PersonalData.class, result);
            }
        });
    }

    public List<PersonalData> findPersonalDataForUser(final byte[] rowKey) {
        final String tableName = HAnnotation.getTableName(User.class);
        Scan s = new Scan(new Get(rowKey));
        return hbaseTemplate.find(tableName, s, new RowMapper<PersonalData>() {
            @Override
            public PersonalData mapRow(Result result, int i) throws Exception {
                return HMarshaller.unmarshall(PersonalData.class, result);
            }
        });
    }

    public PersonalData savePersonalData(final String email, final PersonalData personalData) {
        final String tableName = HAnnotation.getTableName(User.class);
        return hbaseTemplate.execute(tableName, new TableCallback<PersonalData>() {
            @Override
            public PersonalData doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email));
                HMarshaller.marshall(personalData, p);
                table.put(p);
                return personalData;
            }
        });
    }

    public List<MedicalNotes> findMedicalNotesForUser(final byte[] rowKey) {
        final String tableName = HAnnotation.getTableName(User.class);
        Scan s = new Scan(new Get(rowKey));
        return hbaseTemplate.find(tableName, s, new RowMapper<MedicalNotes>() {
            @Override
            public MedicalNotes mapRow(Result result, int rowNum) throws Exception {
                return HMarshaller.unmarshall(MedicalNotes.class, result);
            }
        });
    }

    public MedicalNotes saveMedicalNotes(final String email, final MedicalNotes medicalNotes) {
        final String tableName = HAnnotation.getTableName(User.class);
        return hbaseTemplate.execute(tableName, new TableCallback<MedicalNotes>() {
            @Override
            public MedicalNotes doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email));
                HMarshaller.marshall(medicalNotes, p);
                table.put(p);
                return medicalNotes;
            }
        });
    }

    public List<FamilyTree> findFamilyTreeForUser(final byte[] rowKey) {
        final String tableName = HAnnotation.getTableName(User.class);
        Scan s = new Scan(new Get(rowKey));
        return hbaseTemplate.find(tableName, s, new RowMapper<FamilyTree>() {
            @Override
            public FamilyTree mapRow(Result result, int i) throws Exception {
                return HMarshaller.unmarshall(FamilyTree.class, result);
            }
        });
    }

    public FamilyTree saveFamilyTree(final String email, final FamilyTree familyTree) {
        final String tableName = HAnnotation.getTableName(User.class);
        return hbaseTemplate.execute(tableName, new TableCallback<FamilyTree>() {
            @Override
            public FamilyTree doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email));
                HMarshaller.marshall(familyTree, p);
                table.put(p);
                return familyTree;
            }
        });
    }
}
