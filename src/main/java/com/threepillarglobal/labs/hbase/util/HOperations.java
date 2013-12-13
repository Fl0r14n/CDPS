package com.threepillarglobal.labs.hbase.util;

import java.io.IOException;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

/**
 * Implements usual hbase operations
 */
public abstract class HOperations {

    /**
     * Creates an HBase table based on an annotated hbase entity
     * @param clazz The annotated hbase pojo
     * @param admin An instance of HBaseAdmin
     * @throws IOException
     */
    public static void createTable(Class<?> clazz, HBaseAdmin admin) throws IOException {
        String tableName = HAnnotation.getTableName(clazz);
        if (!admin.tableExists(tableName)) {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            {
                for (String columnFamily : HAnnotation.getColumnFamilyNames(clazz)) {
                    tableDescriptor.addFamily(new HColumnDescriptor(columnFamily));
                }
            }
            admin.createTable(tableDescriptor);
        }
    }

    /**
     * Deletes an existing HBase table based on an annotated hbase entity
     * @param clazz The annotated hbase pojo
     * @param admin An instance of HBaseAdmin
     * @throws IOException 
     */
    public static void deleteTable(Class<?> clazz, HBaseAdmin admin) throws IOException {
        String tableName = HAnnotation.getTableName(clazz);
        if (admin.tableExists(tableName)) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        }
    }
}
