package com.asiainfo.xwbo.xwbo.utils;

import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * @author jiahao jin
 * @create 2020-05-06 15:54
 */
public class DistanceUtil {
    public static Long getDistance(String lngFrom, String latFrom, String lngTo, String latTo) {
        if(StringUtils.isBlank(lngTo) || StringUtils.isBlank(latTo)) return null;
        GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(latFrom), Double.parseDouble(lngFrom));
        GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(latTo), Double.parseDouble(lngTo));

        return Math.round(new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance());
    }

    public static String getGaodeUrl(String lng, String lat) {
        double X_PI = Math.PI * 3000.0 / 180.0;
        double x = Double.parseDouble(lng) - 0.0065;
        double y = Double.parseDouble(lat) - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return "http://uri.amap.com/marker?position="+gg_lng+","+gg_lat+"&name=&src=yellowpage&coordinate=gaode&callnative=1";

    }
    public static void main(String[] args) {
        System.out.println(Math.random()*0.01);
        System.out.println(getDistance("120.10487", "30.326928", String.valueOf(120.10487+Math.random()*0.001), String.valueOf(30.326928+Math.random()*0.001)));
        System.out.println(0.1+0.2);
    }
}
