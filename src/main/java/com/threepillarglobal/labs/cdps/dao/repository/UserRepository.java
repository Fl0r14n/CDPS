package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.AccountData;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import java.util.List;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
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
        final String cfamilyName= HAnnotation.getColumnFamilyName(AccountData.class);        
        return hbaseTemplate.find(tableName, cfamilyName, new RowMapper<AccountData>() {
            @Override
            public AccountData mapRow(Result result, int rowNum) throws Exception {
                
                return new AccountData(Bytes.toString(result.getValue(cfamilyName.getBytes(), "secretKey".getBytes())),
                        Bytes.toBoolean(result.getValue(cfamilyName.getBytes(), "active".getBytes())),
                        Bytes.toString(result.getValue(cfamilyName.getBytes(), "phone".getBytes())));
            }
        });
    }

    public AccountData saveAccountData(final String email, final AccountData account) {
        final String tableName = HAnnotation.getTableName(User.class);
        final String cfamilyName= HAnnotation.getColumnFamilyName(AccountData.class);        
        return hbaseTemplate.execute(tableName, new TableCallback<AccountData>() {
            @Override
            public AccountData doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email).getDigest());
                {
                    p.add(cfamilyName.getBytes(), "secretKey".getBytes(), Bytes.toBytes(account.getSecretKey()));
                    p.add(cfamilyName.getBytes(), "active".getBytes(), Bytes.toBytes(account.getActive()));
                    p.add(cfamilyName.getBytes(), "phone".getBytes(), Bytes.toBytes(account.getPhone()));
                }
                table.put(p);
                return account;
            }
        });
    }
}
