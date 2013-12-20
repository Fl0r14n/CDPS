package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.hbase.util.ReflectionUtil;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test HMarshaller class
 */
public class ReflectionUtilTest {

    /**
     * Sample class with some sample fields
     */
    @Getter
    public static class SampleClass {

        public static enum ENUM {

            LOW,
            MEDIUM,
            HIGH;
        }

        private final String s = "string";
        private final boolean bool = true;
        private final Boolean boole = false;
        //TODO
        //private final byte b = 16;
        //private final Byte bt = 15;
        private final byte[] ba = {0, 1, 2, 3, 4};
        private final char ch = 'a';
        private final Character chr = 'b';
        private final short sh = 1;
        private final Short srt = 2;
        private final int i = 2;
        private final Integer it = 3;
        private final long l = 3;
        private final Long lng = 4L;
        private final float f = 4;
        private final Float flt = 5f;
        private final double d = 5.0;
        private final Double dbl = 6.0;
        private final BigDecimal bd = new BigDecimal(6);
        private final Date dt = new Date();
        private final Timestamp ts = new Timestamp(dt.getTime());
        private final ENUM e = ENUM.MEDIUM;
        private final Object o = new Object();
        private final Sample1 sample1 = new Sample1();

        @Data
        public static class Sample1 {

            public Sample1() {
                list = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    list.add(i);
                }
            }

            private String str = "Hello";

            private List<Integer> list;

            @Data
            public static class Sample2 {

                private final String string = "World!";
            }
        }
    }

    @Test
    public void set_values_into_fields() throws Exception {
        SampleClass a = new SampleClass();
        //do a loopback like test
        for (Field field : SampleClass.class.getDeclaredFields()) {
            //old value
            byte[] expected = ReflectionUtil.getFieldValue(field, a);
            //set as new value
            ReflectionUtil.setFieldValue(field, a, expected);
            byte[] actual = ReflectionUtil.getFieldValue(field, a);

            System.out.println(field.getName() + "|" + Bytes.toStringBinary(actual));
            Assert.assertArrayEquals(expected, actual);
        }
    }
}
