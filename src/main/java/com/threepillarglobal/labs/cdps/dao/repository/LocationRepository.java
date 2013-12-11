package com.threepillarglobal.labs.cdps.dao.repository;

import com.threepillarglobal.labs.cdps.domain.Location;
import com.threepillarglobal.labs.hbase.util.HAnnotation;
import com.threepillarglobal.labs.hbase.util.HMarshaller;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {
    
    @Autowired
    private HbaseTemplate hbaseTemplate;

    public Location.LocationDetails saveLocation(final String countryName, final String countyName, final String cityName, final Location.LocationDetails locationDetails ) {
        final String tableName = HAnnotation.getTableName(Location.LocationDetails.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(Location.LocationDetails.class);
        return hbaseTemplate.execute(tableName, new TableCallback<Location.LocationDetails>() {
            @Override
            public Location.LocationDetails doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(Location.toRowKey(countryName, countyName, cityName));
                HMarshaller.marshall(locationDetails, p);
                table.put(p);
                return locationDetails;
            }
        });

    }

    public Location.Residents saveLocationResidents(final String countryName, final String countyName, final String cityName, final Location.Residents locationResidents)
    {
        final String tableName = HAnnotation.getTableName(Location.Residents.class);
        final String cfamilyName = HAnnotation.getColumnFamilyName(Location.Residents.class);
        return hbaseTemplate.execute(tableName, new TableCallback<Location.Residents>() {
            @Override
            public Location.Residents doInTable(HTableInterface table) throws Throwable {
                Put p = new Put(Location.toRowKey(countryName, countyName, cityName));
                HMarshaller.marshall(locationResidents, p);
                table.put(p);
                return locationResidents;
            }
        });

    }
}
