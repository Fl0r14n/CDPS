package com.threepillarglobal.labs.cdps.analytics;

import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import java.io.IOException;
import java.math.BigDecimal;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MD5Hash;
import org.apache.hadoop.io.Text;

public class RiskMapper extends TableMapper<Text, IntWritable> {
    
    private final String riskFactorColumnName = "riskFactor";

    @Override
    public void map(ImmutableBytesWritable rowKey, Result columns, Context context)
            throws InterruptedException, IOException {

        try {
            String wholeKey = new String(rowKey.get());
            String hashUserID = wholeKey.substring(0, MD5Hash.MD5_LEN);
            // get sales column in byte format first and then convert it to string (as it is stored as string from hbase shell)
            byte[] bRiskFactor = columns.getValue(Bytes.toBytes(HAnnotation.getColumnFamilyName(LivingData.class)), Bytes.toBytes(riskFactorColumnName));
            String sRiskFactor = new String(bRiskFactor);
            BigDecimal riskFactor = new BigDecimal(sRiskFactor);
            // emit date and sales values
            context.write(new Text(hashUserID), new IntWritable(riskFactor.intValueExact()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}