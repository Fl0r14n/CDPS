package com.threepillarglobal.labs.cdps.analytics;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import java.io.IOException;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

public class RiskReducer extends TableReducer<IntWritable, IntWritable, IntWritable> {
    
    private final String riskColumnName = "inheritedRisk";

    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        try {
            int sumRiskFactor = 0;
            int valuesCount = 0;
            // loop through different sales vales and add it to sum
            for (IntWritable partialRiskFactor : values) {
                Integer intRiskFactor = new Integer(partialRiskFactor.toString());
                sumRiskFactor += intRiskFactor;
                valuesCount += 1;
            }
            int riskFactor = Math.round(sumRiskFactor / valuesCount);
            int riskRank = riskFactor % User.MedicalNotes.INHERITED_RISK.values().length;
            User.MedicalNotes.INHERITED_RISK riskValue = User.MedicalNotes.INHERITED_RISK.values()[riskRank];
            // create hbase put with rowkey as date
            Put insHBase = new Put(key.getBytes());
            // insert sum value to hbase 
            insHBase.add(Bytes.toBytes(HAnnotation.getColumnFamilyName(User.MedicalNotes.class)), Bytes.toBytes(riskColumnName), Bytes.toBytes(riskValue.toString()));
            // write data to Hbase table
            context.write(null, insHBase);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}