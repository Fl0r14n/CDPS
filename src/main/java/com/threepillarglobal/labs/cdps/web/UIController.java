package com.threepillarglobal.labs.cdps.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
 
 
@Controller
@Path("/")
public class UIController {
 
	private static List<User> userList = MockDataGenerator.fetchMockUserData(10);
 
	@RequestMapping(value = "/getUid", method = RequestMethod.GET)
	public @ResponseBody
	List<User> getTags(@RequestParam String userName) {
		List<User> result = new ArrayList<User>();
		// iterate a list and filter by userName
		for (User user : userList) {
			if (user.getPersonalData().getName().contains(userName)) {						
				result.add(user);
			}
		}
 
		return result;
 
	}
 
	@RequestMapping(value = "/getChartData", method = RequestMethod.GET)
	public @ResponseBody
	List<SensorData> getChartData(@RequestParam String id) {
		return  MockDataGenerator.fetchMockSensorData("1-11-2012", "31-11-2012");		
	}
	
	
	@RequestMapping(value = "/getRiskData", method = RequestMethod.GET)
	public @ResponseBody
	CardioRisk getRiskData(@RequestParam String id) {
		CardioRisk cR = new CardioRisk();
		cR.calculateRiskFactor(MockDataGenerator.fetchMockUserData(2).get(0), MockDataGenerator.fetchMockSensorData("1-11-2012", "3-11-2012"));
		return  cR;
	}
	
	
}
