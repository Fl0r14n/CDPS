package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import java.io.IOException;
import javax.annotation.Resource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:integrationTests-context.xml")
/**
 * TO WORK: 
 * 1) allow your local user to access hdfs /user dir. So ssh into hdfs node and do
 * sudo -u hdfs hadoop fs -chmod 777 /user
 * 2) create your home directory in hdfs
 * sudo -u hdfs hadoop fs -mkdir /user/[name]
 * sudo -u hdfs hadoop fs -chown [name]:supergroup /user/[name]
 * 
 * //TODO see why it fails copying jars neededed to run mapreduce job
 */
public class MapReduceIT {

    private static final Logger L = LoggerFactory.getLogger(MapReduceIT.class);

    @Resource(name = "hbaseConfiguration")
    private Configuration config;

    @Ignore
    @Test
    public void run_mapreduce_job() throws IOException, InterruptedException, ClassNotFoundException {
        Job job = new Job(config, MapReduceIT.class.getName());                
        job.setJarByClass(MapReduceIT.class);        
        Scan scan = new Scan();
        {
            scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
            scan.setCacheBlocks(false);  // don't set to true for MR jobs
            scan.addColumn("family".getBytes(), "qualifier".getBytes());
        }        
        TableMapReduceUtil.initTableMapperJob(HAnnotation.getTableName(User.class), scan, UserByLocationMapper.class, ImmutableBytesWritable.class, Put.class, job);
        TableMapReduceUtil.initTableReducerJob("targetTable", UserByLocationReducer.class, job);
        job.setNumReduceTasks(0);
        if (!job.waitForCompletion(true)) {
            throw new IOException("error with job!");
        }
    }

    //key out, value out
    static class UserByLocationMapper extends TableMapper<ImmutableBytesWritable, Put> {

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            System.out.println("Mapper key:="+Bytes.toString(key.get()));
        }
    }

    //key in, value in, key out
    static class UserByLocationReducer extends TableReducer<ImmutableBytesWritable, Put, ImmutableBytesWritable> {

        @Override
        protected void reduce(ImmutableBytesWritable key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
            //TODO
        }
    }
}
