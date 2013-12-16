package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.junit.Ignore;
import org.junit.Test;

//TODO
public class HMarshallerTest {

    @HTable(name = "table")
    @Getter
    public static class Table {

        private String key;
        @HColumnFamily(name = "cfamily1")
        private CFamily1 cFamily1;
        @HColumnFamily(name = "cfamily2")
        private CFamily2 cFamily2;

        @Getter
        @HColumnFamily(name = "cfamily1")
        public static class CFamily1 {

            @HColumn(name = "name")
            String name = "Sample1";
        }

        @Getter
        @HColumnFamily(name = "cfamily2")
        public static class CFamily2 {

            @HColumn(name = "name")
            String name = "Sample2";
        }
    }

    @Test
    public void marshall_a_column_family_object() throws Exception {
        Put put = new Put(new byte[]{0x01});
        Table.CFamily1 cf = new Table.CFamily1();
        HMarshaller.marshall(cf, put);
    }

    @Test
    public void unmarshall_a_column_family_object() throws Exception {
        List<KeyValue> kvs = new ArrayList<>();
        {
            kvs.add(new KeyValue("row".getBytes(), "family".getBytes(), "qualifier1".getBytes()));
            kvs.add(new KeyValue("row".getBytes(), "family".getBytes(), "qualifier2".getBytes()));
            kvs.add(new KeyValue("row".getBytes(), "family".getBytes(), "qualifier3".getBytes()));
        }
        HMarshaller.unmarshall(Table.CFamily1.class, new Result(kvs));
    }

    @Test
    @Ignore
    public void marshall_a_table_object_with_all_cfamilies() throws Exception {
        Put put = new Put("row".getBytes());
        Table table = new Table();
        HMarshaller.marshall(table, put);
    }

    @Test
    @Ignore
    public void unmarshall_a_table_object_with_all_cfamilies() throws Exception {
        List<KeyValue> kvs = new ArrayList<>();
        {
            kvs.add(new KeyValue("row".getBytes(), "cfamily1".getBytes(), "Sample1".getBytes()));
            kvs.add(new KeyValue("row".getBytes(), "cfamily2".getBytes(), "Sample2".getBytes()));
        }
        HMarshaller.unmarshall(Table.class, new Result(kvs));
    }
}
