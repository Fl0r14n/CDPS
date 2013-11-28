package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import static com.threepillarglobal.labs.hbase.util.HAnnotation.getColumnFamilyName;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class HMarshaller {

    //TODO Test
    public static <T> T unmarshall(Class<T> clazz, Result result) throws InstantiationException, IllegalAccessException {
        //get column family
        byte[] cFamily = getColumnFamilyName(clazz).getBytes();

        //construct new instance        
        T t = clazz.newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            HColumn hColumn = field.getAnnotation(HColumn.class);
            if (hColumn != null) {
                field.setAccessible(true);

                byte[] val = result.getValue(cFamily, hColumn.name().getBytes());
                field.set(t, val);

//                Class fieldType = field.getType();                
//                switch (fieldType.getSimpleName()) {
//                    case "String": {                        
//                        Bytes.toString(val);
//                        break;
//                    }
//                    case "Boolean": {
//                        Bytes.toBoolean(val);
//                        break;
//                    }
//                    case "Short": {
//                        Bytes.toShort(val);
//                        break;
//                    }
//                    case "Integer": {
//                        Bytes.toInt(val);                        
//                        break;
//                    }
//                    case "Long": {
//                        Bytes.toLong(val);
//                        break;
//                    }
//                    case "Float": {
//                        Bytes.toFloat(val);
//                        break;
//                    }
//                    case "Double": {
//                        Bytes.toDouble(val);
//                        break;
//                    }
//                    case "BigDecimal": {
//                        Bytes.toBigDecimal(val);
//                        break;
//                    }
//                    case "Date": {
//                        new Date(Bytes.toLong(val));
//                        break;
//                    }
//                    case "Timestamp": {
//                        new Timestamp(Bytes.toLong(val));
//                        break;
//                    }               
//                    default: {
//                        field.marshall(t, val);
//                    }
//                }
            }
        }

        return t;
    }

    public static <T> void marshall(T obj, Put put) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        //get column family
        byte[] cFamily = getColumnFamilyName(clazz).getBytes();
        //get all hcolums and read value
        for (Field field : clazz.getDeclaredFields()) {
            HColumn hColumn = field.getAnnotation(HColumn.class);
            if (hColumn != null) {
                put.add(cFamily, hColumn.name().getBytes(), getFieldValue(obj, field));
            }
        }
    }

    public static <T> byte[] getFieldValue(T t, Field fld) throws IllegalArgumentException, IllegalAccessException {
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
                return Bytes.toBytes(((Date)fld.get(t)).getTime());
            }
            case "Timestamp": {
                return Bytes.toBytes(((Date)fld.get(t)).getTime());
            }
        }
        return null;
    }

}
