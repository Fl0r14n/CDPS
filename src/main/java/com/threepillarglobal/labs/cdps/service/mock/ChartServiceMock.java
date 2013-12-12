package com.threepillarglobal.labs.cdps.service.mock;

import com.threepillarglobal.labs.cdps.domain.HourlyData;
import com.threepillarglobal.labs.cdps.domain.SensorData;
import com.threepillarglobal.labs.cdps.service.api.ChartService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;

@Service(value = "chartServiceMock")
public class ChartServiceMock implements ChartService {

    @Override
    public List<SensorData> getSensorData(String id) {
        return getSensorData("1-11-2012", "31-11-2012");
    }

    @SuppressWarnings("finally")
    @Override
    public List<SensorData> getSensorData(String sDate, String eDate) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        List<SensorData> generateSensorData = new ArrayList<>();
        try {
            generateSensorData = generateSensorData(0, 1, dateFormat.parse(sDate), dateFormat.parse(eDate));
            return generateSensorData;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return generateSensorData;
        }
    }

    private List<SensorData> generateSensorData(int rangeFrom, int rangeTo, Date startDate, Date endDate) {
        String[] activityType = {"Eating", "Sleeping", "Exercising"};
        List<SensorData> sdList = new ArrayList<>();
        int activityDuration;
        Random rand = new Random();
        int avgHR, diastolicBP, systolicBP, calories;
        String currentActivityType = null;
        int daysInInterval = getDaysInInterval(startDate, endDate);
        for (int i = 0; i < daysInInterval; i++) {
            for (int j = rangeFrom; j <= rangeTo; j++) {
                SensorData sd;

                List<HourlyData> dailyData = new ArrayList<>();
                Date eventDate = DateUtils.addDays(startDate, i);

                for (int k = 0; k < 24; k++) {
                    HourlyData hh;
                    activityDuration = rand.nextInt(61);
                    currentActivityType = activityType[rand.nextInt(3)];
                    avgHR = rand.nextInt(200);
                    diastolicBP = 50 + rand.nextInt(100);
                    systolicBP = 100 + rand.nextInt(200);
                    calories = rand.nextInt(3000);
                    hh = new HourlyData(currentActivityType, activityDuration, avgHR, systolicBP, diastolicBP, calories);
                    dailyData.add(hh);
                }

                sd = new SensorData(eventDate, dailyData.get(0), dailyData.get(1), dailyData.get(2),
                        dailyData.get(3), dailyData.get(4), dailyData.get(5),
                        dailyData.get(6), dailyData.get(7), dailyData.get(8),
                        dailyData.get(9), dailyData.get(10), dailyData.get(11),
                        dailyData.get(12), dailyData.get(13), dailyData.get(14),
                        dailyData.get(15), dailyData.get(16), dailyData.get(17),
                        dailyData.get(18), dailyData.get(19), dailyData.get(20),
                        dailyData.get(21), dailyData.get(22), dailyData.get(23)
                );
                sdList.add(sd);
            }
        }

        return sdList;
    }

    private int getDaysInInterval(Date startDate, Date endDate) {
        //get the number of days in the specified interval
        final long miliSecondsInADay = 1000 * 60 * 60 * 24;
        return Math.round((endDate.getTime() - startDate.getTime()) / miliSecondsInADay);
    }
}
