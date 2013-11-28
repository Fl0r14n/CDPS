package com.threepillarglobal.labs.hbase.util;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import static com.threepillarglobal.labs.hbase.util.HAnnotation.getColumnFamilyName;
import java.lang.reflect.Field;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;

public class HMarshaller {

    //TODO Test
    public static <T> T get(Class<T> clazz, Result result) throws InstantiationException, IllegalAccessException {
        //get column family
        byte[] cFamily = getColumnFamilyName(clazz).getBytes();

        //construct new instance
        T t = clazz.newInstance();

        for (Field field : clazz.getFields()) {
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
//                        field.set(t, val);
//                    }
//                }
            }
        }

        return t;
    }

    public static <T> void set(T obj, HTableInterface table) {
        Class<?> clazz = obj.getClass();
        //TODO
    }
}
