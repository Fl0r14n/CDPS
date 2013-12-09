package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import static com.threepillarglobal.labs.hbase.util.HAnnotation.getColumnFamilyName;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ClassUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Handless marshalling to and from HBase api
 */
public class HMarshaller {

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
        final T instance = instantiate(clazz);
        //populate with values
        for (Field field : clazz.getDeclaredFields()) {
            HColumn hColumn = field.getAnnotation(HColumn.class);
            if (hColumn != null) {
                byte[] val = result.getValue(cFamily, hColumn.name().getBytes());
                setFieldValue(field, instance, val);
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
                put.add(cFamily, hColumn.name().getBytes(), getFieldValue(field, obj));
            }
        }
    }

    /**
     * Obtain a byte[] representation of the value inside a field
     * @param <T> Class type
     * @param field The Field object
     * @param t The object instance
     * @return field value as byte[]
     * @throws Exception 
     */
    public static <T> byte[] getFieldValue(Field field, T t) throws Exception {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();
        switch (fieldType.getSimpleName()) {
            case "String": {
                return Bytes.toBytes((String) field.get(t));
            }
            case "boolean": {
                return Bytes.toBytes(field.getBoolean(t));
            }
            case "Boolean": {
                return Bytes.toBytes((Boolean) field.get(t));
            }
            case "byte": {
                return Bytes.toBytes(field.getByte(t));
            }
            case "Byte": {
                return Bytes.toBytes((Byte) field.get(t)); //do not cast to short
            }
            case "byte[]": {
                return (byte[]) field.get(t);
            }
            case "char": {
                return Bytes.toBytes(field.getChar(t));
            }
            case "Character": {
                return Bytes.toBytes((Character) field.get(t)); //do not cast to int
            }
            case "short": {
                return Bytes.toBytes(field.getShort(t));
            }
            case "Short": {
                return Bytes.toBytes((Short) field.get(t));
            }
            case "int": {
                return Bytes.toBytes(field.getInt(t));
            }
            case "Integer": {
                return Bytes.toBytes((Integer) field.get(t));
            }
            case "long": {
                return Bytes.toBytes(field.getLong(t));
            }
            case "Long": {
                return Bytes.toBytes((Long) field.get(t));
            }
            case "float": {
                return Bytes.toBytes(field.getFloat(t));
            }
            case "Float": {
                return Bytes.toBytes((Float) field.get(t));
            }
            case "double": {
                return Bytes.toBytes(field.getDouble(t));
            }
            case "Double": {
                return Bytes.toBytes((Double) field.get(t));
            }
            case "BigDecimal": {
                return Bytes.toBytes((BigDecimal) field.get(t));
            }
            case "Date": {
                return Bytes.toBytes(((Date) field.get(t)).getTime());
            }
            case "Timestamp": {
                return Bytes.toBytes(((Date) field.get(t)).getTime());
            }
        }
        return null;
    }

    /**
     * Set a Field in an object given the byte[] representation of it's value
     * @param <T> Class type
     * @param field The Field object
     * @param t The object instance
     * @param value field value to be set as byte[]
     * @throws Exception 
     */
    public static <T> void setFieldValue(Field field, T t, byte[] value) throws Exception {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();        
        switch (fieldType.getSimpleName()) {
            case "String": {
                field.set(t, Bytes.toString(value));
                return;
            }
            case "boolean": {
                field.setBoolean(t, Bytes.toBoolean(value));
                return;
            }
            case "Boolean": {
                field.set(t, Bytes.toBoolean(value));
                return;
            }
            case "byte": {
                byte val = value.length>0?(byte)(value[0]&0xFF):0;
                field.setByte(t, val);
                return;
            }
            case "Byte": {
                byte val = value.length>0?(byte)(value[0]&0xFF):0;
                field.set(t, new Byte(val));
                return;
            }
            case "byte[]": {
                field.set(t, value);
                return;
            }
            case "char": {
                field.setChar(t, new Character((char) Bytes.toInt(value)));
                return;
            }
            case "Character": {
                field.set(t, new Character((char) Bytes.toInt(value)));
                return;
            }
            case "short": {
                field.setShort(t, Bytes.toShort(value));
                return;
            }
            case "Short": {
                field.set(t, new Short(Bytes.toShort(value)));
                return;
            }
            case "int": {
                field.setInt(t, Bytes.toInt(value));
                return;
            }
            case "Integer": {
                field.set(t, new Integer(Bytes.toInt(value)));
                return;
            }
            case "long": {
                field.setLong(t, Bytes.toLong(value));
                return;
            }
            case "Long": {
                field.set(t, new Long(Bytes.toLong(value)));
                return;
            }
            case "float": {
                field.setFloat(t, Bytes.toFloat(value));
                return;
            }
            case "Float": {
                field.set(t, new Float(Bytes.toFloat(value)));
                return;
            }
            case "double": {
                field.setDouble(t, Bytes.toDouble(value));
                return;
            }
            case "Double": {
                field.set(t, new Double(Bytes.toDouble(value)));
                return;
            }
            case "BigDecimal": {
                field.set(t, Bytes.toBigDecimal(value));
                return;
            }
            case "Date": {
                field.set(t, new Date(Bytes.toLong(value)));
                return;
            }
            case "Timestamp": {
                field.set(t, new Timestamp(Bytes.toLong(value)));
            }
        }
    }

    private static <T> T instantiate(Class<T> cls) throws Exception {
        // Create instance of the given class                
        @SuppressWarnings("unchecked")
        final Constructor<T> constr = (Constructor<T>) cls.getConstructors()[0];
        final List<Object> params = new ArrayList<>();
        for (Class<?> pType : constr.getParameterTypes()) {
            params.add((pType.isPrimitive()) ? ClassUtils.primitiveToWrapper(pType).newInstance() : null);
        }
        return constr.newInstance(params.toArray());
    }

    private static <T> T instantiate(Class<T> cls, Map<String, ? extends Object> args) throws Exception {
        final T instance = instantiate(cls);

        // Set separate fields
        for (Map.Entry<String, ? extends Object> arg : args.entrySet()) {
            Field f = cls.getDeclaredField(arg.getKey());
            f.setAccessible(true);
            f.set(instance, arg.getValue());
        }
        return instance;
    }
}