package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.cdps.analytics.RiskMapper;
import com.threepillarglobal.labs.cdps.analytics.RiskReducer;
import com.threepillarglobal.labs.cdps.domain.LivingData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import javax.annotation.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:integrationTests-context.xml")
public class HBaseMapReduceIT {

    @Resource(name = "hbaseConfiguration")
    private Configuration config;

    @Test
    public void testMapReduceJob() throws Exception {
        HBaseMapReduceIT mrd = new HBaseMapReduceIT();

        // define scan and define column families to scan
        Scan scan = new Scan();
        scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
        scan.setCacheBlocks(false);
        scan.addFamily(Bytes.toBytes(HAnnotation.getColumnFamilyName(LivingData.class)));

        Job job = new Job(config, "RiskAgregator");

        job.setJarByClass(HBaseMapReduceIT.class);
        // define input hbase table
        TableMapReduceUtil.initTableMapperJob(
                HAnnotation.getTableName(LivingData.class),
                scan,
                RiskMapper.class,
                Text.class,
                IntWritable.class,
                job);
        // define output table
        TableMapReduceUtil.initTableReducerJob(
                HAnnotation.getTableName(User.class),
                RiskReducer.class,
                job);

        job.waitForCompletion(true);
    }
}
