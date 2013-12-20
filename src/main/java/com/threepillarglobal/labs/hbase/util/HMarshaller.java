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
        return unmarshall(clazz, result, null);
    }

    //for recursive call
    private static <T> T unmarshall(Class<T> clazz, Result result, byte[] cFamily) throws Exception {
        //create new instance
        final T instance = ReflectionUtil.instantiate(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            //is this field a HColumnFamily field?
            HColumnFamily hColumnFamily = field.getAnnotation(HColumnFamily.class);
            if (hColumnFamily != null) {
                field.setAccessible(true);
                String hColumnFamilyName = "".equals(hColumnFamily.name()) ? field.getName() : hColumnFamily.name();
                //recursive call
                field.set(instance, unmarshall(field.getType(), result, hColumnFamilyName.getBytes()));
            } else {
                //is this a HColumn field
                HColumn hColumn = field.getAnnotation(HColumn.class);
                if (hColumn != null) {
                    byte[] cf = cFamily;
                    if (cf == null) {
                        cf = getColumnFamilyName(clazz).getBytes();
                    }
                    if (cf != null) { //only if a valid column family is found
                        String hColumnName = "".equals(hColumn.name()) ? field.getName() : hColumn.name();
                        byte[] val = result.getValue(cf, hColumnName.getBytes());
                        //set field value to one of the known ones
                        ReflectionUtil.setFieldValue(field, instance, val);
                    }
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
        marshall(t, put, null);
    }

    //for recursive call
    private static <T> void marshall(T t, Put put, byte[] cFamily) throws Exception {
        //get class type
        Class<?> clazz = t.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            HColumnFamily hColumnFamily = field.getAnnotation(HColumnFamily.class);
            if (hColumnFamily != null) {
                field.setAccessible(true);
                String hColumnFamilyName = "".equals(hColumnFamily.name()) ? field.getName() : hColumnFamily.name();
                //recursive call
                marshall(field.get(t), put, hColumnFamilyName.getBytes());
            } else {
                //get column family from class annotation
                HColumn hColumn = field.getAnnotation(HColumn.class);
                if (hColumn != null) {
                    byte[] cf = cFamily;
                    if (cf == null) {
                        cf = getColumnFamilyName(clazz).getBytes();
                    }
                    if (cf != null) { //only if a valid column family is found
                        String hColumnName = "".equals(hColumn.name()) ? field.getName() : hColumn.name();
                        put.add(cf, hColumnName.getBytes(), ReflectionUtil.getFieldValue(field, t));
                    }
                }
            }
        }
    }
}
