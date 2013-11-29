package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.hbase.util.HMarshaller;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Getter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

/**
 * Test HMarshaller class
 */
public class HMarshallerTest {

    /**
     * Sample class with some sample fields
     */
    @Getter
    public static class SampleClass {

        private final String s = "string";
        private final boolean bool = true;
        private final Boolean boole = false;
        private final byte b = 16;
        private final Byte bt = 15;
        private final byte[] ba = {0,1,2,3,4};
        private final char ch = 'a';
        private final Character chr = 'b';
        private final short sh = 1;
        private final Short srt = 2;
        private final int i = 2;
        private final Integer  it = 3;
        private final long l = 3;
        private final Long lng = 4L;
        private final float f = 4;
        private final Float flt = 5f;
        private final double d = 5.0;
        private final Double dbl = 6.0;
        private final BigDecimal bd = new BigDecimal(6);
        private final Date dt = new Date();
        private final Timestamp ts = new Timestamp(dt.getTime());
    }

    @Test
    public void read_values_from_fields() throws Exception {
        SampleClass a = new SampleClass();
        for (Field f : SampleClass.class.getDeclaredFields()) {
            System.out.println(f.getName()+"|"+Bytes.toStringBinary(HMarshaller.getFieldValue(f, a)));
        }        
    }
    
    @Test
    public void set_values_into_fields() throws Exception {
        SampleClass a = new SampleClass();
        //do a loopback like test
        for (Field f : SampleClass.class.getDeclaredFields()) {
            //old value
            byte[] value = HMarshaller.getFieldValue(f, a);
            //set as new value
            HMarshaller.setFieldValue(f, a, value);
        }
    }
}
