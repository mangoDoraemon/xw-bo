package com.asiainfo.xwbo.xwbo.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-29 14:28
 */
public class DateUtil {
    public static List<String> days(String startTime, String endTime) {
        DateTime start = new DateTime(startTime);
        DateTime end = new DateTime(endTime);
        List<String> result = new ArrayList<>();
        while((!start.isEqual(end)) && start.isBefore(end)) {
            result.add(start.toString("yyyy-MM-dd"));
            start = start.plusDays(1);
        }
        result.add(start.toString("yyyy-MM-dd"));
        return result;

    }

    public static void main(String[] args) {
        System.out.println(days("2020-01-01", "2020-01-03"));
    }

    public static String getFormt(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
