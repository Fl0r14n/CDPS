package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.annotation.HColumn;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class. Handless HBase annotated entities
 */
public abstract class HAnnotation {

    /**
     * Obtain HBase table Name from annotated entity
     *
     * @param clazz The annotated class
     * @return table name
     */
    public static String getTableName(Class<?> clazz) {
        HTable hTable = clazz.getAnnotation(HTable.class);
        assert hTable != null : "HTable annotation missing";
        return hTable.name();
    }

    /**
     * Obtain HBase column family names from annotated fileds or methods<br/>.
     * If no column family annotations are found it searches for column family
     * class annotation
     *
     * @param clazz The annotated class
     * @return array of column family names
     */
    public static String[] getColumnFamilyNames(Class<?> clazz) {
        List<String> buf = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            HColumnFamily hColumnFamily = field.getAnnotation(HColumnFamily.class);
            if (hColumnFamily != null) {
                buf.add(hColumnFamily.name());
            }
        }
        if (buf.isEmpty()) {
            buf.add(getColumnFamilyName(clazz));
        }
        String[] result = {};
        return buf.toArray(result);
    }

    /**
     * Obtain HBase column family name from an class annotated with
     *
     * @HColumnFamily
     *
     * @param clazz The annotated class
     * @return column family name
     */
    public static String getColumnFamilyName(Class<?> clazz) {
        HColumnFamily hColumnFamily = clazz.getAnnotation(HColumnFamily.class);
        assert hColumnFamily != null : "HColumnFamily annotation missing!";
        return hColumnFamily.name();
    }

    /**
     * Obtain HBase column names in a column family. It searches for @HColumn
     * fields
     *
     * @param clazz The annotated class
     * @return array of column names
     */
    public static String[] getColumnNames(Class<?> clazz) {
        List<String> buf = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            HColumn hColumn = field.getAnnotation(HColumn.class);
            if (hColumn != null) {
                buf.add(hColumn.name());
            }
        }
        String[] result = {};
        return buf.toArray(result);
    }
}
