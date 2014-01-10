package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.*;
import com.threepillarglobal.labs.hbase.repository.HRepository;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    public UserRepository(HbaseTemplate hbaseTemplate) {
        userRepo = new HRepository<User>(User.class, hbaseTemplate) {
        };
        accountDataRepo = new HRepository<AccountData>(User.class, hbaseTemplate) {
        };
        personalDataRepo = new HRepository<PersonalData>(User.class, hbaseTemplate) {
        };
        familyTreeRepo = new HRepository<FamilyTree>(User.class, hbaseTemplate) {
        };
        medicalNotesRepository = new HRepository<MedicalNotes>(User.class, hbaseTemplate) {
        };
    }
    private final HRepository<User> userRepo;
    private final HRepository<AccountData> accountDataRepo;
    private final HRepository<PersonalData> personalDataRepo;
    private final HRepository<FamilyTree> familyTreeRepo;
    private final HRepository<MedicalNotes> medicalNotesRepository;

    public void saveUsers(Map<byte[], User> users) {
        userRepo.save(users);
    }
    
    public List<User> findAllUser() {
        return userRepo.findAll();
    }
    
    public User findUser(String email) {
        return userRepo.findOne(User.toRowKey(email));
    }
    
    public List<AccountData> findAllAccountData() {
        return accountDataRepo.findAll();
    }

    public AccountData findAccountDataForUser(final byte[] rowKey) {
        return accountDataRepo.findOne(rowKey);
    }

    public AccountData saveAccountData(final String email, final AccountData account) {
        return accountDataRepo.save(User.toRowKey(email), account);
    }

    public List<PersonalData> findAllPersonalData() {
        return personalDataRepo.findAll();
    }

    public PersonalData findPersonalDataForUser(final byte[] rowKey) {
        return personalDataRepo.findOne(rowKey);
    }

    public PersonalData savePersonalData(final String email, final PersonalData personalData) {
        return personalDataRepo.save(User.toRowKey(email), personalData);
    }

    public MedicalNotes findMedicalNotesForUser(final byte[] rowKey) {
        return medicalNotesRepository.findOne(rowKey);
    }
    public MedicalNotes saveMedicalNotes(final String email, final MedicalNotes medicalNotes) {
        return medicalNotesRepository.save(User.toRowKey(email), medicalNotes);
    }

    public FamilyTree findFamilyTreeForUser(final byte[] rowKey) {
        return familyTreeRepo.findOne(rowKey);
    }
    
    public FamilyTree saveFamilyTree(final String email, final FamilyTree familyTree) {
        return familyTreeRepo.save(User.toRowKey(email), familyTree);
    }
}
