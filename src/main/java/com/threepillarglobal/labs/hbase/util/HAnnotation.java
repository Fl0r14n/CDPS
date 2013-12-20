package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.annotation.HColumn;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
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
     * @param clazz The annotated class with @HTable
     * @return table name
     */
    public static String getTableName(Class<?> clazz) {
        HTable hTable = clazz.getAnnotation(HTable.class);
        //this is a must
        assert hTable != null : "HTable annotation missing on " + clazz.getName();
        return "".equals(hTable.name()) ? clazz.getSimpleName() : hTable.name();
    }

    /**
     * Obtain HBase column family name from an class annotated with
     * HColumnFamily
     *
     * @param clazz The annotated class
     * @return column family name or null
     */
    public static String getColumnFamilyName(Class<?> clazz) {
        HColumnFamily hColumnFamily = clazz.getAnnotation(HColumnFamily.class);
        if (hColumnFamily != null) {
            return "".equals(hColumnFamily.name()) ? clazz.getSimpleName() : hColumnFamily.name();
        }
        return null;
    }

    /**
     * Obtain HBase column family name form an annotated field with
     * HColumnFamily
     *
     * @param field The annotated field
     * @return column family name or null
     */
    public static String getColumnFamilyName(Field field) {
        HColumnFamily hColumnFamily = field.getAnnotation(HColumnFamily.class);
        if (hColumnFamily != null) {
            return "".equals(hColumnFamily.name()) ? field.getName() : hColumnFamily.name();
        }
        return null;
    }

    /**
     * Obtain HBase column name from an annotated field with HColumn
     *
     * @param field The annotated field
     * @return column name or null
     */
    public static String getColumnName(Field field) {
        HColumn hColumn = field.getAnnotation(HColumn.class);
        if (hColumn != null) {
            return "".equals(hColumn.name()) ? field.getName() : hColumn.name();
        }
        return null;
    }

    /**
     * Obtain HBase column family names from annotated fileds<br/>. If also
     * searches for column family class annotation
     *
     * @param clazz The annotated class
     * @return array of column family names
     */
    public static String[] getColumnFamilyNames(Class<?> clazz) {
        List<String> buf = new ArrayList<>();
        String cFamilyName;
        for (Field field : clazz.getDeclaredFields()) {
            cFamilyName = getColumnFamilyName(field);
            if (cFamilyName == null) { //might be annotated at field class level
                cFamilyName = getColumnFamilyName(field.getType());
            }
            if (cFamilyName != null) {
                buf.add(cFamilyName);
            }
        }
        //add also individual family name if found at class level
        cFamilyName = getColumnFamilyName(clazz);
        if (cFamilyName != null) {
            buf.add(cFamilyName);
        }
        String[] result = {};
        return buf.toArray(result);
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
        String hColumnName;
        for (Field field : clazz.getDeclaredFields()) {
            hColumnName = getColumnName(field);
            if (hColumnName != null) {
                buf.add(hColumnName);
            }
        }
        String[] result = {};
        return buf.toArray(result);
    }

    /**
     * Get declared column families from fields.
     *
     * @param clazz The annotated class
     * @return a map of column family name and associated class
     */
    public static Map<String, Class<?>> getColumnFamilies(Class<?> clazz) {
        Map<String, Class<?>> result = new LinkedHashMap<>(5);
        //HColumnFamily at field level
        String cFamilyName;
        for (Field field : clazz.getDeclaredFields()) {
            cFamilyName = getColumnFamilyName(field);
            if (cFamilyName == null) {
                cFamilyName = getColumnFamilyName(field.getType());
            }
            if (cFamilyName != null) {
                result.put(cFamilyName, field.getType());
            }
        }
        return result;
    }

    /**
     * Get all declared columns from annotated entity.
     *
     * @param clazz The annotated class with @HColumn
     * @return a map of columns with name and associated class
     */
    public static Map<String, Class<?>> getColumns(Class<?> clazz) {
        Map<String, Class<?>> result = new LinkedHashMap<>();
        String hColumnName;
        for (Field field : clazz.getDeclaredFields()) {
            hColumnName = getColumnName(field);
            if (hColumnName != null) {
                result.put(hColumnName, field.getType());
            }
        }
        return result;
    }
}
