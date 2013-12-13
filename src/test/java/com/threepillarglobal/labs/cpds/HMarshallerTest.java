package com.threepillarglobal.labs.cpds;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import lombok.Getter;
import org.junit.Test;

//TODO
public class HMarshallerTest {

    @HTable(name = "table")
    @Getter
    public static class Table {

        private String key;
        @HColumnFamily(name = "cfamily1")
        private CFamily1 cFamily1;
        @HColumnFamily(name = "cfamily2")
        private CFamily2 cFamily2;

        @Getter
        @HColumnFamily(name = "cfamily1")
        public static class CFamily1 {

            @HColumn(name = "name")
            String name = "Sample2";
        }

        @Getter
        @HColumnFamily(name = "cfamily2")
        public static class CFamily2 {

            @HColumn(name = "name")
            String name = "Sample2";
        }
    }

    @Test
    public void marshall_a_column_family_object() throws Exception {
//        HMarshaller.marshall(this, null);
    }

    @Test
    public void unmarshall_a_column_family_object() throws Exception {
//        HMarshaller.unmarshall(Table.CFamily1.class, null);
    }

    @Test
    public void marshall_a_table_object_with_all_cfamilies() throws Exception {
//        HMarshaller.marshall(this, null);
    }

    @Test
    public void unmarshall_a_table_object_with_all_cfamilies() throws Exception {
//        HMarshaller.unmarshall(Table.class, null);
    }
}
