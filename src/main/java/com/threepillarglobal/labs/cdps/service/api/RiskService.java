package com.threepillarglobal.labs.cdps.service.api;

import java.util.Date;

import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.User;

public interface RiskService {

	CardioRisk getCardioRisk(String uid, Date from, Date to);
	
}
