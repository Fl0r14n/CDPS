package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
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
     *
     * @param <T> Class type
     * @param clazz The annotated class
     * @param result HBase api Result object
     * @return new POJO instance with filled fields
     * @throws Exception
     */
    public static <T> T unmarshall(Class<T> clazz, Result result) throws Exception {
        //create new instance
        final T instance = ReflectionUtil.instantiate(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            //is this field a HColumnFamily field?
            HColumnFamily hColumnFamily = field.getAnnotation(HColumnFamily.class);
            if (hColumnFamily != null) {
                field.setAccessible(true);
                //recursive call
                field.set(instance, unmarshall(field.getType(), result));
            } else {
                //is this a HColumn field
                HColumn hColumn = field.getAnnotation(HColumn.class);
                if (hColumn != null) {
                    //get the HColumnFamily class annotation
                    byte[] cFamily = getColumnFamilyName(clazz).getBytes();
                    byte[] val = result.getValue(cFamily, hColumn.name().getBytes());
                    //set field value to one of the known ones
                    ReflectionUtil.setFieldValue(field, instance, val);
                }
            }
        }
        return instance;
    }

    /**
     * Marshall the specified annotated POJO object to the specified HBase api
     * Put object
     *
     * @param <T> Class type
     * @param t The annotated POJO object
     * @param put The instance of a Put object. Do not forget to set the index
     * key to the Put object
     * @throws Exception
     */
    public static <T> void marshall(T t, Put put) throws Exception {
        //get class type
        Class<?> clazz = t.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            HColumnFamily hColumnFamily = field.getAnnotation(HColumnFamily.class);
            if (hColumnFamily != null) {
                field.setAccessible(true);                
                //recursive call
                marshall(field.get(t), put);
            } else {
                //get column family from class annotation
                HColumn hColumn = field.getAnnotation(HColumn.class);
                if (hColumn != null) {
                    byte[] cFamily = getColumnFamilyName(clazz).getBytes();
                    put.add(cFamily, hColumn.name().getBytes(), ReflectionUtil.getFieldValue(field, t));
                }
            }
        }
    }
}
