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
     * Get declared column families from fields.
     *
     * @param clazz The annotated class
     * @return a map of column family name and associated class
     */
    public static Map<String, Class<?>> getColumnFamilies(Class<?> clazz) {
        Map<String, Class<?>> result = new LinkedHashMap<>(5);
        HColumnFamily hColumnFamily;
        //HColumnFamily at field level
        for (Field field : clazz.getDeclaredFields()) {
            hColumnFamily = field.getAnnotation(HColumnFamily.class);
            if (hColumnFamily != null) {
                String name = "".equals(hColumnFamily.name()) ? field.getName() : hColumnFamily.name();
                result.put(name, field.getType());
            }
        }
        return result;
    }

    /**
     * Get column family annotation at class level
     *
     * @param clazz The supposed annotated @HColumnFamily class.
     * @return Map.Entity entry with column name as key and class type as value
     * or null
     */
    public static Map.Entry<String, Class<?>> getColumnFamily(Class<?> clazz) {
        HColumnFamily hColumnFamily = clazz.getAnnotation(HColumnFamily.class);
        if (hColumnFamily != null) {
            Map<String, Class<?>> result = new HashMap<>(1);
            String name = "".equals(hColumnFamily.name()) ? clazz.getSimpleName() : hColumnFamily.name();
            result.put(name, clazz);
            for (Map.Entry<String, Class<?>> entry : result.entrySet()) {
                return entry;
            }
        }
        return null;
    }

    /**
     * Get all declared columns from annotated entity.
     *
     * @param clazz The annotated class with @HColumn
     * @return a map of columns with name and associated class
     */
    public static Map<String, Class<?>> getColumns(Class<?> clazz) {
        Map<String, Class<?>> result = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            HColumn hColumn = field.getAnnotation(HColumn.class);
            if (hColumn != null) {
                String name = "".equals(hColumn.name()) ? field.getName() : hColumn.name();
                result.put(name, field.getType());
            }
        }
        return result;
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
        for (Field field : clazz.getDeclaredFields()) {
            HColumnFamily hColumnFamily = field.getAnnotation(HColumnFamily.class);
            if (hColumnFamily != null) {
                String name = "".equals(hColumnFamily.name()) ? field.getName() : hColumnFamily.name();
                buf.add(name);
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
                String name = "".equals(hColumn.name()) ? field.getName() : hColumn.name();
                buf.add(name);
            }
        }
        String[] result = {};
        return buf.toArray(result);
    }
}
