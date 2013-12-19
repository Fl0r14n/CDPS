package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import java.util.Map;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

public class HAnnotationTest {

    private static final String TABLE = "table";
    private static final String CFAMILY1 = "cf1";
    private static final String CFAMILY2 = "cf2";
    private static final String COLUMN1 = "col1";
    private static final String COLUMN2 = "col2";

    @HTable(name = TABLE)
    @HColumnFamily(name = CFAMILY1)
    @Getter
    public static class Sample1 {

        @HColumn(name = COLUMN1)
        private String col1;
        @HColumn(name = COLUMN2)
        private String col2;
    }

    @HTable(name = TABLE)
    @Getter
    public static class Sample2 {

        @HColumnFamily(name = CFAMILY1)
        private Object cFamily1;
        @HColumnFamily(name = CFAMILY2)
        private Object cFamily2;
    }

    @Test
    public void get_table_name() {
        String result = HAnnotation.getTableName(Sample1.class);
        Assert.assertEquals(TABLE, result);
    }

    @Test
    public void get_column_names() {
        String[] expected = {COLUMN1, COLUMN2};
        String[] result = HAnnotation.getColumnNames(Sample1.class);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void get_family_name_from_class_annotaion() {
        String result = HAnnotation.getColumnFamilyName(Sample1.class);
        Assert.assertEquals(CFAMILY1, result);
    }

    @Test
    public void get_family_names_from_fields() {
        String[] expected = {CFAMILY1, CFAMILY2};
        String[] result = HAnnotation.getColumnFamilyNames(Sample2.class);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void get_column_family_class_from_table() {
        Map.Entry<String, Class<?>> result = HAnnotation.getColumnFamily(Sample1.class);
        Assert.assertTrue(Sample1.class.getName().equals(result.getValue().getName()));
    }
    
    @Test
    public void get_column_family_classes_from_table_fields() {
        Map<String, Class<?>> result = HAnnotation.getColumnFamilies(Sample2.class);
        Assert.assertEquals(Object.class.getName(), result.get(CFAMILY2).getName());
    }
    
    public void get_cloumns() {
        Map<String, Class<?>> result = HAnnotation.getColumns(Sample1.class);
        Assert.assertTrue(result.get(COLUMN1).getName().equals(result.get(COLUMN1).getName()));
    }
}
