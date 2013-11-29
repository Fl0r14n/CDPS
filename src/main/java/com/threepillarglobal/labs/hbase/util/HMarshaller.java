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

public class HMarshaller {

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

    public static <T> byte[] getFieldValue(Field fld, T t) throws Exception {
        fld.setAccessible(true);
        Class<?> fieldType = fld.getType();
        switch (fieldType.getSimpleName()) {
            case "String": {
                return Bytes.toBytes((String) fld.get(t));
            }
            case "boolean": {
                return Bytes.toBytes(fld.getBoolean(t));
            }
            case "Boolean": {
                return Bytes.toBytes((Boolean) fld.get(t));
            }
            case "byte": {
                return Bytes.toBytes(fld.getByte(t));
            }
            case "Byte": {
                return Bytes.toBytes((Byte) fld.get(t)); //do not cast to short
            }
            case "byte[]": {
                return (byte[]) fld.get(t);
            }
            case "char": {
                return Bytes.toBytes(fld.getChar(t));
            }
            case "Character": {
                return Bytes.toBytes((Character) fld.get(t)); //do not cast to int
            }
            case "short": {
                return Bytes.toBytes(fld.getShort(t));
            }
            case "Short": {
                return Bytes.toBytes((Short) fld.get(t));
            }
            case "int": {
                return Bytes.toBytes(fld.getInt(t));
            }
            case "Integer": {
                return Bytes.toBytes((Integer) fld.get(t));
            }
            case "long": {
                return Bytes.toBytes(fld.getLong(t));
            }
            case "Long": {
                return Bytes.toBytes((Long) fld.get(t));
            }
            case "float": {
                return Bytes.toBytes(fld.getFloat(t));
            }
            case "Float": {
                return Bytes.toBytes((Float) fld.get(t));
            }
            case "double": {
                return Bytes.toBytes(fld.getDouble(t));
            }
            case "Double": {
                return Bytes.toBytes((Double) fld.get(t));
            }
            case "BigDecimal": {
                return Bytes.toBytes((BigDecimal) fld.get(t));
            }
            case "Date": {
                return Bytes.toBytes(((Date) fld.get(t)).getTime());
            }
            case "Timestamp": {
                return Bytes.toBytes(((Date) fld.get(t)).getTime());
            }
        }
        return null;
    }

    public static <T> void setFieldValue(Field fld, T t, byte[] value) throws Exception {
        fld.setAccessible(true);
        Class<?> fieldType = fld.getType();        
        switch (fieldType.getSimpleName()) {
            case "String": {
                fld.set(t, Bytes.toString(value));
                return;
            }
            case "boolean": {
                fld.setBoolean(t, Bytes.toBoolean(value));
                return;
            }
            case "Boolean": {
                fld.set(t, Bytes.toBoolean(value));
                return;
            }
            case "byte": {
                byte val = value.length>0?(byte)(value[0]&0xFF):0;
                fld.setByte(t, val);
                return;
            }
            case "Byte": {
                byte val = value.length>0?(byte)(value[0]&0xFF):0;
                fld.set(t, new Byte(val));
                return;
            }
            case "byte[]": {
                fld.set(t, value);
                return;
            }
            case "char": {
                fld.setChar(t, new Character((char) Bytes.toInt(value)));
                return;
            }
            case "Character": {
                fld.set(t, new Character((char) Bytes.toInt(value)));
                return;
            }
            case "short": {
                fld.setShort(t, Bytes.toShort(value));
                return;
            }
            case "Short": {
                fld.set(t, new Short(Bytes.toShort(value)));
                return;
            }
            case "int": {
                fld.setInt(t, Bytes.toInt(value));
                return;
            }
            case "Integer": {
                fld.set(t, new Integer(Bytes.toInt(value)));
                return;
            }
            case "long": {
                fld.setLong(t, Bytes.toLong(value));
                return;
            }
            case "Long": {
                fld.set(t, new Long(Bytes.toLong(value)));
                return;
            }
            case "float": {
                fld.setFloat(t, Bytes.toFloat(value));
                return;
            }
            case "Float": {
                fld.set(t, new Float(Bytes.toFloat(value)));
                return;
            }
            case "double": {
                fld.setDouble(t, Bytes.toDouble(value));
                return;
            }
            case "Double": {
                fld.set(t, new Double(Bytes.toDouble(value)));
                return;
            }
            case "BigDecimal": {
                fld.set(t, Bytes.toBigDecimal(value));
                return;
            }
            case "Date": {
                fld.set(t, new Date(Bytes.toLong(value)));
                return;
            }
            case "Timestamp": {
                fld.set(t, new Timestamp(Bytes.toLong(value)));
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
