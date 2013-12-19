package com.threepillarglobal.labs.hbase.repository;

import com.threepillarglobal.labs.hbase.util.HAnnotation;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import com.threepillarglobal.labs.hbase.util.HOperations;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;

public abstract class HRepository<T extends Object> {

    private static final Logger L = LoggerFactory.getLogger(HRepository.class);

    //here we try to get the class type of T at runtime
    @SuppressWarnings("unchecked")
    private HRepository() {
        this.tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    private final Class<T> tClass;

    /**
     * Constructor
     *
     * @param tableClass The class annotated with @HTable. Might be same class
     * or other
     * @param hbaseTemplate The HBaseTemplate object because spring does not
     * autowire in abstract classes
     */
    public HRepository(Class<?> tableClass, HbaseTemplate hbaseTemplate) {
        this();
        assert hbaseTemplate != null : "HbaseTemplate must not be null!";
        this.hbaseTemplate = hbaseTemplate;
        tableName = HAnnotation.getTableName(tableClass);
    }
    private String tableName;
    private HbaseTemplate hbaseTemplate;

    /**
     * Create or override an existing object at defined rowID
     *
     * @param <S> the generic type
     * @param row row ID
     * @param s the object to be persisted
     * @return same object if persisted
     */
    public <S extends T> S save(final byte[] row, final S s) {
        return hbaseTemplate.execute(tableName, new TableCallback<S>() {

            @Override
            public S doInTable(HTableInterface hti) throws Throwable {
                Put p = new Put(row);
                HMarshaller.marshall(s, p);
                hti.put(p);
                return s;
            }
        });
    }

    /**
     * Create or override a list of objects debined by their rowID
     *
     * @param <S> the generic type
     * @param map Map consisting of the rowID and the object
     * @return an Iterable of persisted objects
     */
    public <S extends T> Iterable<S> save(Map<byte[], S> map) {
        final List<Put> puts = new ArrayList<>();
        final List<S> objectsWritten = new ArrayList<>();
        for (Map.Entry<byte[], S> entry : map.entrySet()) {
            try {
                S s = entry.getValue();
                Put put = new Put(entry.getKey());
                HMarshaller.marshall(s, put);
                puts.add(put);
                objectsWritten.add(s);
            } catch (Exception e) {
                L.error(HRepository.class.getMethods()[0].toGenericString(), e);
            }
        }
        if (puts.size() > 0) {
            return hbaseTemplate.execute(tableName, new TableCallback<Iterable<S>>() {

                @Override
                public Iterable<S> doInTable(HTableInterface hti) throws Throwable {
                    hti.put(puts);
                    return objectsWritten;
                }
            });
        }
        return objectsWritten;
    }

    /**
     * Get an annotated entity from HBase
     *
     * @param row the row key
     * @return the found object
     */
    public T findOne(byte[] row) {
        final String cfamilyName = HAnnotation.getColumnFamilyName(tClass);
        if (cfamilyName != null) {
            return hbaseTemplate.get(tableName, new String(row), cfamilyName, new RowMapperImpl());
        } else {
            List<T> tList = hbaseTemplate.find(tableName, new Scan(new Get(row)), new RowMapperImpl());
            return tList.get(0);
        }
    }

    /**
     * Get all persisted entities in the HBase table. It does a full table scan
     *
     * @return all persisted objects
     */
    public List<T> findAll() {
        final String cfamilyName = HAnnotation.getColumnFamilyName(tClass);
        if (cfamilyName != null) {
            return hbaseTemplate.find(tableName, cfamilyName, new RowMapperImpl());
        } else {
            return hbaseTemplate.find(tableName, new Scan(), new RowMapperImpl());
        }
    }

    /**
     * Get a list of entities by their row key
     *
     * @param rows The iterable object consisting of the row keys
     * @return a list of the found entities
     */
    public List<T> findAll(Iterable<byte[]> rows) {
        List<T> result = new ArrayList<>();
        for (Iterator<byte[]> it = rows.iterator(); it.hasNext();) {
            T t = findOne(it.next());
            result.add(t);
        }
        return result;
    }

    /**
     * Get a range of entities
     *
     * @param startRow The start row key
     * @param stopRow The end row key
     * @return a list of the found entities
     */
    public List<T> findAll(byte[] startRow, byte[] stopRow) {
        System.out.println(Bytes.toString(startRow) + " " + Bytes.toString(stopRow));
        return hbaseTemplate.find(tableName, new Scan(/*startRow, stopRow*/), new RowMapper<T>() {

                    @Override
                    public T mapRow(Result result, int i) throws Exception {
                        return HMarshaller.unmarshall(tClass, result);
                    }
                });
    }

    /**
     * Get a list of entities using a filter for row scanning
     *
     * @param startRow The start row key
     * @param filter The scan filter
     * @return a list of found entities
     */
    public List<T> findAll(byte[] startRow, Filter filter) {
        return hbaseTemplate.find(tableName, new Scan(startRow, filter), new RowMapper<T>() {

            @Override
            public T mapRow(Result result, int i) throws Exception {
                return HMarshaller.unmarshall(tClass, result);
            }
        });
    }

    /**
     * Delete a row in the HBase table
     *
     * @param row the row key
     */
    public void delete(final byte[] row) {
        hbaseTemplate.execute(tableName, new TableCallback<Void>() {

            @Override
            public Void doInTable(HTableInterface hti) throws Throwable {
                hti.delete(new Delete(row));
                return null;
            }
        });
    }

    /**
     * Delete a list of rows
     *
     * @param itrbl The iterable object consisting of row keys
     */
    public void delete(final Iterable<byte[]> itrbl) {
        hbaseTemplate.execute(tableName, new TableCallback<Void>() {

            @Override
            public Void doInTable(HTableInterface hti) throws Throwable {
                for (Iterator<byte[]> it = itrbl.iterator(); it.hasNext();) {
                    hti.delete(new Delete(it.next()));
                }
                return null;
            }
        });
    }

    /**
     * Drops the table and recreates it
     */
    public void deleteAll() {
        try {
            HBaseAdmin admin = new HBaseAdmin(hbaseTemplate.getConfiguration());
            HOperations.deleteTable(tClass, admin);
            HOperations.createTable(tClass, admin);
        } catch (IOException ioe) {
            L.error(HRepository.class.getMethods()[0].toGenericString(), ioe);
        }
    }

    private class RowMapperImpl implements RowMapper<T> {

        public RowMapperImpl() {
        }

        @Override
        public T mapRow(Result result, int i) throws Exception {
            return HMarshaller.unmarshall(tClass, result);
        }
    }
}
