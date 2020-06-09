package com.asiainfo.xwbo.xwbo.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author: huangfuliang
 * @date: 2019/7/20.
 */
public class MD5Util {

    private static Logger log = LoggerFactory.getLogger(MD5Util.class);

    //盐，用于混交md5
    public static String encrypt(String dataStr) {
        try {
           return DigestUtils.md5Hex(dataStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encryptBySlat(String dataStr, String newSlat) {
        dataStr = dataStr + newSlat;
        log.info(dataStr);
        try {
            return DigestUtils.md5Hex(dataStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(encrypt("22436870"));
//        String userId = "zhangweixing1";
//        String mobile = "13867059550";
//        long timestamp = System.currentTimeMillis();
//        String sign = MD5Util.encryptBySlat("5711574929642289", "571!@#");
//        System.out.println(timestamp + "=====" + sign);
//List<String> kpiIdList = new ArrayList();
//        kpiIdList.add("HJ001");
//        kpiIdList.add("SS001");
//        kpiIdList.add("HJ002");
//        kpiIdList = kpiIdList.stream().filter(str -> str.startsWith("SS")).collect(Collectors.toList());
//        System.out.println(kpiIdList);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        DateTime endTime = new DateTime();
//        DateTime startDate = new DateTime();
//        int days= Days.daysBetween(startDate, endTime).getDays();
//        byte[] bytes = {0x01};
//        String tesult = new String(bytes);
//        System.out.println(Arrays.toString("   ".split(tesult)));

    }



}
