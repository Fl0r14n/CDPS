package com.threepillarglobal.labs.cdps.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.threepillarglobal.labs.cdps.domain.CardioRisk;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.domain.User;
import com.threepillarglobal.labs.cdps.service.api.ChartService;
import com.threepillarglobal.labs.cdps.service.api.RiskFactorsService;
import com.threepillarglobal.labs.cdps.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Controller
public class UIController {

    @Autowired
    @Qualifier(value = "chartServiceMock")
    private ChartService chartService;

    @Autowired
    @Qualifier(value = "riskFactorServiceMock")
    private RiskFactorsService riskFactorsService;

    @Autowired
    @Qualifier(value = "userServiceMock")
    private UserService userService;

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
        return chartService.getSensorData(id);
    }

    @RequestMapping(value = "/getRiskData", method = RequestMethod.GET)
    public @ResponseBody
    CardioRisk getRiskData(@RequestParam String id) {
        CardioRisk cR = new CardioRisk();
        cR.calculateRiskFactor(MockDataGenerator.fetchMockUserData(2).get(0), chartService.getSensorData("1-11-2012", "3-11-2012"));
        return cR;
    }

}
