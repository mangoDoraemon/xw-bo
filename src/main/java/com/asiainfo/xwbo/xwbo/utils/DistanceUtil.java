package com.asiainfo.xwbo.xwbo.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * @author jiahao jin
 * @create 2020-05-06 15:54
 */
public class DistanceUtil {
    public static double getDistance(String lngFrom, String latFrom, String lngTo, String latTo) {
        GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(latFrom), Double.parseDouble(lngFrom));
        GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(latTo), Double.parseDouble(lngTo));

        return Math.round(new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance());
    }

    public static void main(String[] args) {
        System.out.println(Math.random()*0.01);
        System.out.println(getDistance("120.10487", "30.326928", String.valueOf(120.10487+Math.random()*0.001), String.valueOf(30.326928+Math.random()*0.001)));
    }
}
