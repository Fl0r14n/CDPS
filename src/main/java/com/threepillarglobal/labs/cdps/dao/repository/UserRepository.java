package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.User.AccountData;
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
        return hbaseTemplate.find(User.TABLE, AccountData.CFAMILY, new RowMapper<AccountData>() {
            @Override
            public AccountData mapRow(Result result, int rowNum) throws Exception {

                return new AccountData(Bytes.toString(result.getValue(AccountData.BCFAMILY, AccountData.BSECRETKEY)),
                        Bytes.toBoolean(result.getValue(AccountData.BCFAMILY, AccountData.BACTIVE)),
                        Bytes.toString(result.getValue(AccountData.BCFAMILY, AccountData.BPHONE)));
            }
        });
    }

    public AccountData saveAccountData(final String email, final AccountData account) {
        return hbaseTemplate.execute(User.TABLE, new TableCallback<AccountData>() {
            @Override
            public AccountData doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(User.toRowKey(email).getDigest());
                {
                    p.add(AccountData.BCFAMILY, AccountData.BSECRETKEY, Bytes.toBytes(account.getSecretKey()));
                    p.add(AccountData.BCFAMILY, AccountData.BACTIVE, Bytes.toBytes(account.getActive()));
                    p.add(AccountData.BCFAMILY, AccountData.BPHONE, Bytes.toBytes(account.getPhone()));
                }
                table.put(p);
                return account;
            }
        });
    }
}
