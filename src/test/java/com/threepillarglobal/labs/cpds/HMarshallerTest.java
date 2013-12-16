package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

//TODO
public class HMarshallerTest {

    @HTable(name = "table")
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Table {

        private static byte[] row = "row".getBytes();

        @HColumnFamily(name = "cfamily1")
        private CFamily1 cFamily1;
        @HColumnFamily(name = "cfamily2")
        private CFamily2 cFamily2;

        @HColumnFamily(name = "cfamily1")
        @Getter
        @EqualsAndHashCode
        @ToString
        public static class CFamily1 {

            @HColumn(name = "col01")
            String col01 = "value0";
            @HColumn(name = "col02")
            String col02 = "value1";

        }

        @HColumnFamily(name = "cfamily2")
        @Getter
        @EqualsAndHashCode
        @ToString
        public static class CFamily2 {

            @HColumn(name = "col11")
            String col11 = "value2";
            @HColumn(name = "col12")
            String col12 = "value3";
        }
    }

    private Result constructResult() {
        List<KeyValue> kvs = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Class<?>> cFamily : HAnnotation.getColumnFamilies(Table.class).entrySet()) {
            byte[] familyName = cFamily.getKey().getBytes();
            for (Map.Entry<String, Class<?>> column : HAnnotation.getColumns(cFamily.getValue()).entrySet()) {
                byte[] columnName = column.getKey().getBytes();
                kvs.add(new KeyValue(Table.row, familyName, columnName, ("value" + i).getBytes()));
                i++;
            }
        }
        return new Result(kvs);
    }
    private final Result res = constructResult();

    @Test
    public void marshall_a_column_family_object() throws Exception {
        Table.CFamily1 obj = new Table.CFamily1();
        Put put = new Put(Table.row);
        HMarshaller.marshall(obj, put);
        
        Assert.assertTrue(put.has("cfamily1".getBytes(), "col01".getBytes(), "value0".getBytes()));
        Assert.assertTrue(put.has("cfamily1".getBytes(), "col02".getBytes(), "value1".getBytes()));
    }

    @Test
    public void unmarshall_a_column_family_object() throws Exception {
        Table.CFamily1 expected = new Table.CFamily1();
        Table.CFamily1 actual = HMarshaller.unmarshall(Table.CFamily1.class, res);
        
        Assert.assertTrue(expected.col01.equals(actual.col01));
        Assert.assertTrue(expected.col02.equals(actual.col02));
    }

    @Test
    @Ignore
    public void marshall_a_table_object_with_all_cfamilies() throws Exception {
        Put put = new Put(Table.row);
        Table table = new Table();
        HMarshaller.marshall(table, put);
    }

    @Test
    @Ignore
    public void unmarshall_a_table_object_with_all_cfamilies() throws Exception {
        System.out.println(res);
        Table table = HMarshaller.unmarshall(Table.class, res);
    }
}
