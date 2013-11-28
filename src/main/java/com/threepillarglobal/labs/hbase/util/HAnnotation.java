package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.annotation.HColumn;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class HAnnotation {

    public static String getTableName(Class<?> clazz) {     
        HTable hTable = clazz.getAnnotation(HTable.class);
        assert hTable != null : "HTable annotation missing";
        return hTable.name();
    }

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

    public static String getColumnFamilyName(Class<?> clazz) {
        HColumnFamily hColumnFamily = clazz.getAnnotation(HColumnFamily.class);
        assert hColumnFamily != null : "HColumnFamily annotation missing!";
        return hColumnFamily.name();
    }

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
