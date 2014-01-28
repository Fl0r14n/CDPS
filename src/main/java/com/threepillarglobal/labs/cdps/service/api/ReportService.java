package com.threepillarglobal.labs.cdps.service.api;

import java.util.Date;
import java.util.List;

import org.apache.hadoop.hbase.filter.FilterBase;

import com.threepillarglobal.labs.cdps.domain.DemographicReport;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;

public interface ReportService {

	List<DemographicReport> display();
	
	
}
