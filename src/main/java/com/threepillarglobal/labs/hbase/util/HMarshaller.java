package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import static com.threepillarglobal.labs.hbase.util.HAnnotation.getColumnFamilyName;
import java.lang.reflect.Field;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;

/**
 * Handless marshalling to and from HBase api
 */
public abstract class HMarshaller {

    /**
     * Unmarshall a Result to a POJO object
     * @param <T> Class type
     * @param clazz The annotated class
     * @param result HBase api Result object
     * @return new POJO instance with filled fields
     * @throws Exception 
     */
    public static <T> T unmarshall(Class<T> clazz, Result result) throws Exception {
        //get column family
        byte[] cFamily = getColumnFamilyName(clazz).getBytes();
        //create new instance
        final T instance = ReflectionUtil.instantiate(clazz);
        //populate with values
        for (Field field : clazz.getDeclaredFields()) {
            HColumn hColumn = field.getAnnotation(HColumn.class);
            if (hColumn != null) {
                byte[] val = result.getValue(cFamily, hColumn.name().getBytes());
                ReflectionUtil.setFieldValue(field, instance, val);
            }
        }
        return instance;
    }

    /**
     * Marshall the specified annotated POJO object to the specified HBase api Put object
     * @param <T> Class type
     * @param obj The annotated POJO object
     * @param put The instance of a Put object. Do not forget to set the index key to the Put object
     * @throws Exception 
     */
    public static <T> void marshall(T obj, Put put) throws Exception {
        Class<?> clazz = obj.getClass();
        //get column family
        byte[] cFamily = getColumnFamilyName(clazz).getBytes();
        //get all hcolums and read value
        for (Field field : clazz.getDeclaredFields()) {
            HColumn hColumn = field.getAnnotation(HColumn.class);
            if (hColumn != null) {                
                put.add(cFamily, hColumn.name().getBytes(), ReflectionUtil.getFieldValue(field, obj));
            }
        }
    }
}
