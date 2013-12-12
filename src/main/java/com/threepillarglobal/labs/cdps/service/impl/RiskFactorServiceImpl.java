package com.threepillarglobal.labs.cdps.service.impl;

import java.util.Date;

import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.service.api.RiskService;

import org.springframework.stereotype.Service;

@Service(value = "riskFactorServiceImpl")
public class RiskFactorServiceImpl implements RiskService {

	@Override
	public CardioRisk getCardioRisk(String uid, Date from, Date to) {
		 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
    
}
