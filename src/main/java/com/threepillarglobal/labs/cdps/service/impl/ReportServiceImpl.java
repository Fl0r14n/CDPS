package com.threepillarglobal.labs.cdps.service.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Service;

import com.threepillarglobal.labs.cdps.domain.DemographicReport;
import com.threepillarglobal.labs.cdps.service.api.ReportService;
import com.threepillarglobal.labs.hbase.util.HMarshaller;

@Service(value = "reportServiceImpl")
public class ReportServiceImpl implements ReportService {

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
