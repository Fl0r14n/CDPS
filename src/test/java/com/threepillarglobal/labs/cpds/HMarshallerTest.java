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
import org.junit.Test;

public class HMarshallerTest {

    @HTable(name = "table")
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Table {

        private static byte[] row = "row".getBytes();

        @HColumnFamily(name = "cf1")
        private CFamily1 cFamily1 = new CFamily1();
        @HColumnFamily(name = "cf2")
        private CFamily2 cFamily2 = new CFamily2();

        @HColumnFamily(name = "cf1")
        @Getter
        @EqualsAndHashCode
        @ToString
        public static class CFamily1 {

            @HColumn
            private String col01 = "value0";
            @HColumn
            private String col02 = "value1";

        }

        @HColumnFamily(name = "cf2")
        @Getter
        @EqualsAndHashCode
        @ToString
        public static class CFamily2 {

            @HColumn
            private String col11 = "value0";
            @HColumn
            private String col12 = "value1";
        }
    }

    @HTable(name = "table")
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Table1 {

        private static byte[] row = "row".getBytes();

        @HColumnFamily
        private CFamily cf1 = new Table1.CFamily();
        @HColumnFamily
        private CFamily cf2 = new Table1.CFamily();

        @Getter
        @EqualsAndHashCode
        @ToString
        public static class CFamily {

            @HColumn
            private String col01 = "value0";
            @HColumn(name = "col02")
            private String col02 = "value1";
        }

    }

    private Result constructResult(Class<?> clazz, byte[] row) {
        List<KeyValue> kvs = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Class<?>> cFamily : HAnnotation.getColumnFamilies(clazz).entrySet()) {
            byte[] familyName = cFamily.getKey().getBytes();
            for (Map.Entry<String, Class<?>> column : HAnnotation.getColumns(cFamily.getValue()).entrySet()) {
                byte[] columnName = column.getKey().getBytes();
                kvs.add(new KeyValue(row, familyName, columnName, ("value" + i).getBytes()));
                i++;
            }
            i = 0;
        }
        return new Result(kvs);
    }

    @Test
    public void marshall_a_column_family_object() throws Exception {
        Table.CFamily1 obj = new Table.CFamily1();
        Put put = new Put(Table.row);
        HMarshaller.marshall(obj, put);
        Assert.assertTrue(put.has("cf1".getBytes(), "col01".getBytes(), "value0".getBytes()));
        Assert.assertTrue(put.has("cf1".getBytes(), "col02".getBytes(), "value1".getBytes()));
    }

    @Test
    public void unmarshall_a_column_family_object() throws Exception {
        Table.CFamily1 expected = new Table.CFamily1();
        Result res = constructResult(Table.class, Table.row);
        Table.CFamily1 actual = HMarshaller.unmarshall(Table.CFamily1.class, res);

        Assert.assertTrue(expected.getCol01().equals(actual.getCol01()));
        Assert.assertTrue(expected.getCol02().equals(actual.getCol02()));
    }

    @Test
    public void marshall_a_table_object_with_all_cfamilies() throws Exception {
        Put put = new Put(Table.row);
        Table table = new Table();
        HMarshaller.marshall(table, put);
        Assert.assertTrue(put.has("cf1".getBytes(), "col01".getBytes(), "value0".getBytes()));
        Assert.assertTrue(put.has("cf1".getBytes(), "col02".getBytes(), "value1".getBytes()));
        Assert.assertTrue(put.has("cf2".getBytes(), "col11".getBytes(), "value0".getBytes()));
        Assert.assertTrue(put.has("cf2".getBytes(), "col12".getBytes(), "value1".getBytes()));
    }

    @Test
    public void unmarshall_a_table_object_with_all_cfamilies() throws Exception {
        Table expected = new Table();
        Result res = constructResult(Table.class, Table.row);
        Table actual = HMarshaller.unmarshall(Table.class, res);

        Assert.assertTrue(expected.getCFamily1().getCol01().equals(actual.getCFamily1().getCol01()));
        Assert.assertTrue(expected.getCFamily1().getCol02().equals(actual.getCFamily1().getCol02()));
        Assert.assertTrue(expected.getCFamily2().getCol11().equals(actual.getCFamily2().getCol11()));
        Assert.assertTrue(expected.getCFamily2().getCol12().equals(actual.getCFamily2().getCol12()));
    }

    @Test
    public void unmarshall_a_table_with_multiple_column_families_of_same_class() throws Exception {
        Table1 expected = new Table1();
        Result res = constructResult(Table1.class, Table1.row);
        Table1 actual = HMarshaller.unmarshall(Table1.class, res);
        Assert.assertTrue(expected.getCf1().getCol01().equals(actual.getCf1().getCol01()));
        Assert.assertTrue(expected.getCf1().getCol02().equals(actual.getCf1().getCol02()));
        Assert.assertTrue(expected.getCf2().getCol01().equals(actual.getCf2().getCol01()));
        Assert.assertTrue(expected.getCf2().getCol02().equals(actual.getCf2().getCol02()));
    }

    @Test
    public void marshall_a_table_with_multiple_column_families_of_same_class() throws Exception {
        Put put = new Put(Table1.row);
        Table1 table = new Table1();
        HMarshaller.marshall(table, put);
        Assert.assertTrue(put.has("cf1".getBytes(), "col01".getBytes(), "value0".getBytes()));
        Assert.assertTrue(put.has("cf1".getBytes(), "col02".getBytes(), "value1".getBytes()));
        Assert.assertTrue(put.has("cf2".getBytes(), "col01".getBytes(), "value0".getBytes()));
        Assert.assertTrue(put.has("cf2".getBytes(), "col02".getBytes(), "value1".getBytes()));
    }
}
