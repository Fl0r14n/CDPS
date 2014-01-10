package com.threepillarglobal.labs.cdps.dao.repository;

import java.util.List;

import com.threepillarglobal.labs.cdps.domain.Location;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.domain.Location.*;
import com.threepillarglobal.labs.hbase.repository.HRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {

    @Autowired
    public LocationRepository(HbaseTemplate hbaseTemplate) {
        locationDetailsRepo = new HRepository<LocationDetails>(Location.class, hbaseTemplate) {
        };
        residentsRepo = new HRepository<Residents>(Location.class, hbaseTemplate) {
        };
    }
    private final HRepository<LocationDetails> locationDetailsRepo;
    private final HRepository<Residents> residentsRepo;

    public LocationDetails saveLocation(final String countryName, final String countyName, final String cityName, final LocationDetails locationDetails) {
        return locationDetailsRepo.save(Location.toRowKey(countryName, countyName, cityName), locationDetails);
    }

    public Residents saveLocationResidents(final String countryName, final String countyName, final String cityName, final Residents locationResidents) {
        return residentsRepo.save(Location.toRowKey(countryName, countyName, cityName), locationResidents);
    }
    
    public List<Location.LocationDetails> findAllLocations() {
        return locationDetailsRepo.findAll();
    }
    
}
