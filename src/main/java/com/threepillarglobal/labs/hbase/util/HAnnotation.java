package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.annotation.HColumn;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Util class. Handless HBase annotated entities
 */
public class HAnnotation {

    /**
     * Obtain HBase table Name from annotated entity
     * @param clazz The annotated class
     * @return table name
     */
    public static String getTableName(Class<?> clazz) {     
        HTable hTable = clazz.getAnnotation(HTable.class);
        assert hTable != null : "HTable annotation missing";
        return hTable.name();
    }

    /**
     * Obtain HBase column family names from annotated entity with @HTable<br/>. 
     * In case <b>columnFamilies</b> propertiy is missing it searches for @HColumnFamily annotation
     * @param clazz The annotated class
     * @return array of column family names
     */
    public static String[] getColumnFamilyNames(Class<?> clazz) {
        HTable hTable = clazz.getAnnotation(HTable.class);
        assert hTable != null : "HTable annotation missing";
        String[] columnFamilies = hTable.columnFamilies();
        if (columnFamilies.length>0) {
            return columnFamilies;
        } else {
            return new String[]{getColumnFamilyName(clazz)};
        }
    }

    /**
     * Obtain HBase column family name from an class annotated with @HColumnFamily
     * @param clazz The annotated class
     * @return column family name
     */
    public static String getColumnFamilyName(Class<?> clazz) {
        HColumnFamily hColumnFamily = clazz.getAnnotation(HColumnFamily.class);
        assert hColumnFamily != null : "HColumnFamily annotation missing!";
        return hColumnFamily.name();
    }

    /**
     * Obtain HBase column names in a column family. It searches for @HColumn fields
     * @param clazz The annotated class
     * @return array of column names
     */
    public static String[] getColumnNames(Class<?> clazz) {
        List<String> buf = new ArrayList<>();
        for (Annotation a : clazz.getAnnotations()) {
            if (a instanceof HColumn) {
                buf.add(((HColumn) a).name());
            }
        }
        String[] result = {};
        return buf.toArray(result);
    }
}
