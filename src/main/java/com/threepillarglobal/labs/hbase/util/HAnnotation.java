package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.annotation.HColumn;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Util class. Handless HBase annotated entities
 */
public abstract class HAnnotation {

    /**
     * Obtain HBase table name from annotated entity
     *
     * @param tableClass The annotated class
     * @return table name
     */
    public static String getTableName(Class<?> tableClass) {
        HTable hTable = tableClass.getAnnotation(HTable.class);
        assert hTable != null : "HTable annotation missing";
        return hTable.name();
    }

    /**
     * Get declared column families from annotated HBase entity
     *
     * @param tableClass the HBase annotated class with @HTable
     * @return a map of column family name and associated class
     */
    public static Map<String, Class<?>> getColumnFamilies(Class<?> tableClass) {
        HTable hTable = tableClass.getAnnotation(HTable.class);
        assert hTable != null : "HTable annotation missing";
        Map<String, Class<?>> result = new LinkedHashMap<>();
        //HColumnFamily at class level
        HColumnFamily hColumnFamily = tableClass.getAnnotation(HColumnFamily.class);
        if (hColumnFamily != null) {
            result.put(hColumnFamily.name(), tableClass);
        } else {
            //HColumnFamily at field level
            for (Field field : tableClass.getDeclaredFields()) {
                hColumnFamily = field.getAnnotation(HColumnFamily.class);
                if (hColumnFamily != null) {
                    result.put(hColumnFamily.name(), field.getType());
                }
            }
        }
        return result;
    }

    /**
     * Get all declared columns from annotated HBase entity.
     *
     * @param cFamilyClass the column family class or single column family table
     * annotated with HColumnFamily
     * @return a map of columns with name and associated class
     */
    public static Map<String, Class<?>> getColumns(Class<?> cFamilyClass) {
        Map<String, Class<?>> result = new LinkedHashMap<>();
        for (Field field : cFamilyClass.getDeclaredFields()) {
            HColumn hColumn = field.getAnnotation(HColumn.class);
            if (hColumn != null) {
                result.put(hColumn.name(), field.getType());
            }
        }
        return result;
    }

    /**
     * Obtain HBase column family names from annotated fileds or methods<br/>.
     * If also searches for column family class annotation
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
        //add also individual family name if found at class level
        String cFamilyName = getColumnFamilyName(clazz);
        if (cFamilyName != null) {
            buf.add(cFamilyName);
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
        if (hColumnFamily == null) {
            return null;
        }
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
