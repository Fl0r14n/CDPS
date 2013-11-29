package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.hbase.util.HMarshaller;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

public class HMarshallerTest {

    public static class A {

        String s = "string";
        boolean bool = true;
        Boolean boole = false;
        byte b = 16;
        Byte bt = 15;
        byte[] ba = {0,1,2,3,4};
        char ch = 'a';
        Character chr = 'b';
        short sh = 1;
        Short srt = 2;
        int i = 2;
        Integer  it = 3;
        long l = 3;
        Long lng = 4L;
        float f = 4;
        Float flt = 5f;
        double d = 5.0;
        Double dbl = 6.0;
        BigDecimal bd = new BigDecimal(6);
        Date dt = new Date();
        Timestamp ts = new Timestamp(dt.getTime());
    }

    @Test
    public void read_field_values_from_class() throws Exception {
        A a = new A();
        for (Field f : A.class.getDeclaredFields()) {
            System.out.println(f.getName()+"|"+Bytes.toStringBinary(HMarshaller.getFieldValue(f, a)));
        }
        //TODO do some asserts
    }
    
    @Test
    public void set_field_values_in_class() throws Exception {
        A a = new A();
        for (Field f : A.class.getDeclaredFields()) {
            byte[] oldValue = HMarshaller.getFieldValue(f, a);
            HMarshaller.setFieldValue(f, a, oldValue);
        }
        //TODO do some asserts
    }
}
