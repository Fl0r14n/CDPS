package com.threepillarglobal.labs.cdps.domain;

import com.threepillarglobal.labs.hbase.annotation.HColumn;
import com.threepillarglobal.labs.hbase.annotation.HColumnFamily;
import com.threepillarglobal.labs.hbase.annotation.HTable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@HTable(name = "summary_user")
@HColumnFamily(name = "details")
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class DemographicReport {
        
	@HColumn
    private final String loc_id;
	
    @HColumn
    private final Integer total;
    
}
