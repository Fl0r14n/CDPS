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

        private final String _String = "Hello world!";
        private final boolean _boolean = true;
        private final Boolean _Boolean = false;
        private final byte _byte = 7;
        private final Byte _Byte = 10;
        private final byte[] _byte_array = {0, 1, 2, 3, 4};
        private final char _char = 'a';
        private final Character _Character = 'b';
        private final short _short = 1;
        private final Short _Short = 2;
        private final int _int = 2;
        private final Integer _Integer = 3;
        private final long _long = 3;
        private final Long _Long = 4L;
        private final float _float = 4;
        private final Float _Float = 5f;
        private final double _double = 5.0;
        private final Double _Double = 6.0;
        private final BigDecimal _BigDecimal = new BigDecimal(6);
        private final Date _Date = new Date();
        private final Timestamp _Timestamp = new Timestamp(_Date.getTime());
        private final ENUM _ENUM = ENUM.MEDIUM;
        private final Object _Object = new Object();
        private final Sample1 _Sample1 = new Sample1();
        private String emptyString;
        private final String nullString = null;

        @Data
        public static class Sample1 {

            public Sample1() {
                list = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    list.add(i);
                }
            }

            private String str = "Hello";
            
            private Sample2 sample2 = new Sample2();

            private List<Integer> list;

            @Data
            public static class Sample2 {

                private String string = "World!";
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

            System.out.println(field.getName() + " expected: " + Bytes.toStringBinary(expected) + " actual: "+Bytes.toStringBinary(actual));
            Assert.assertArrayEquals(expected, actual);
        }
    }
}
