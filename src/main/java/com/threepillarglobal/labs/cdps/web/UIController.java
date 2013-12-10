package com.threepillarglobal.labs.cdps.web;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
 
 
@Controller
@Path("/")
public class UIController {
 

	
	//private static List<SensorData> sdList = MockDataGenerator.fetchMockSensorData();
	private static List<User> data = MockDataGenerator.fetchMockUserData();
 
	
 
	@RequestMapping(value = "/getUid", method = RequestMethod.GET)
	public @ResponseBody
	List<User> getTags(@RequestParam String tagName) {
 
		List<User> result = new ArrayList<User>();
		 
		// iterate a list and filter by tagName
		for (User tag : data) {
			if (tag.getId().contains(tagName)) {
				result.add(tag);
			}
		}
 
		return result;
 
	}
 
	@RequestMapping(value = "/getChartData", method = RequestMethod.GET)
	public @ResponseBody
	String getChartData(@RequestParam String id) {
		List<SensorData> sdList  = MockDataGenerator.fetchMockSensorData();
		JSONArray resJsonArray =  new JSONArray();
		for(SensorData sD: sdList){
			try {
				resJsonArray.put(new JSONObject(sD.toJSONString()));
			} catch (JSONException e) {}
		}
		return resJsonArray.toString();
	}
	
	
	
}
