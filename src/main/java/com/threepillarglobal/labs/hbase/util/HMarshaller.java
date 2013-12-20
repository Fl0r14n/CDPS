package com.threepillarglobal.labs.hbase.util;

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
    private static <T> T unmarshall(Class<T> clazz, Result result, String cFamilyName) throws Exception {
        //create new instance
        final T instance = ReflectionUtil.instantiate(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            //is this field a HColumnFamily field?
            String fieldTypeName = HAnnotation.getColumnFamilyName(field);
            if (fieldTypeName != null) {
                field.setAccessible(true);
                //recursive call
                field.set(instance, unmarshall(field.getType(), result, fieldTypeName));
            } else {
                //is this a HColumn field?
                fieldTypeName = HAnnotation.getColumnName(field);
                if (fieldTypeName != null) {
                    String cf = cFamilyName;
                    if (cf == null) {
                        cf = HAnnotation.getColumnFamilyName(clazz);
                    }
                    if (cf != null) {
                        //only if a valid column family is found
                        byte[] val = result.getValue(cf.getBytes(), fieldTypeName.getBytes());
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
    private static <T> void marshall(T t, Put put, String cFamilyName) throws Exception {
        //get class type
        Class<?> clazz = t.getClass();
        String fieldTypeName;
        for (Field field : clazz.getDeclaredFields()) {
            //is this field a HColumnFamily field?
            fieldTypeName = HAnnotation.getColumnFamilyName(field);            
            if (fieldTypeName != null) {
                field.setAccessible(true);
                //recursive call
                marshall(field.get(t), put, fieldTypeName);
            } else {
                //is this a HColumn field?
                fieldTypeName = HAnnotation.getColumnName(field);
                if (fieldTypeName != null) {
                    String cf = cFamilyName;
                    if (cf == null) {
                        cf = HAnnotation.getColumnFamilyName(clazz);
                    }
                    if (cf != null) {
                        //only if a valid column family is found
                        put.add(cf.getBytes(), fieldTypeName.getBytes(), ReflectionUtil.getFieldValue(field, t));
                    }
                }
            }
        }
    }
}
