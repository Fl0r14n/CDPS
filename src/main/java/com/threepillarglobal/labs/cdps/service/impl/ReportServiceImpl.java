package com.threepillarglobal.labs.cdps.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Service;

import com.threepillarglobal.labs.cdps.analytics.RiskAnalyser;
import com.threepillarglobal.labs.cdps.analytics.RiskIndexUtils;
import com.threepillarglobal.labs.cdps.dao.repository.SensorDataRepository;
import com.threepillarglobal.labs.cdps.domain.DemographicReport;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.service.api.ReportService;
import com.threepillarglobal.labs.hbase.util.HMarshaller;

@Service(value = "reportServiceImpl")
public class ReportServiceImpl implements ReportService {

	@Autowired
	private SensorDataRepository sensorRepo;
	
	
	@Autowired
	private HbaseTemplate hBaseTemplate;
	 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<DemographicReport> display() {
		return hBaseTemplate.find("summary_user", new Scan(), new RowMapper() {

			@Override
			public Object mapRow(Result result, int rowNum) throws Exception {
				return HMarshaller.unmarshall(DemographicReport.class, result);
			}
			
		});
		
			
	}

	 
}
