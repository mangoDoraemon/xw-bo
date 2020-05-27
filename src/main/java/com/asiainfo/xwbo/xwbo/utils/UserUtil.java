package com.asiainfo.xwbo.xwbo.utils;

import com.asiainfo.xwbo.xwbo.model.XwUserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-05-12 09:37
 */
public class UserUtil {

    private static final String TOKEN_SECRET = "JO6HN3NGIU25G2FIG8V7VD6CK9B6T2Z5";
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000;

    public static String encryption(String password) {
//        return MD5Util.encrypt(password);
        return password;
    }

    public static String sign(XwUserInfo xwUserInfo) {

        Date nowDate = new Date();
        System.out.println("开始时间： "+nowDate.getTime());
        Date expireDate = new Date(nowDate.getTime() + EXPIRE_TIME);
        System.out.println("到期时间： "+expireDate.getTime());
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(xwUserInfo.getUserId() + "")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();

    }

    public static Claims verify(String token){
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            return Jwts.parser()
                    .setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){

            return null;
        }
    }

    /**
     *
     * @param token
     * @return
     * @throws Exception
     * @message false 不过期
     */
    public static boolean isOverdue(String token) throws Exception {
        Claims claims = verify(token);
        if(null == claims) {
            return true;
        }
        return claims.getExpiration().before(new Date());
    }

    public static void main(String[] args) throws Exception {
//        XwUserInfo xwUserInfo = new XwUserInfo();
//        xwUserInfo.setUserId("234");
//        String a = sign(xwUserInfo);
//        System.out.println(a);
//        System.out.println(verify(a));
        System.out.println(isOverdue("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMzQiLCJpYXQiOjE1ODkyNjQwOTAsImV4cCI6MTU4OTI2NDExMH0.e-D7z-2YAykWiCAKmIRQIVB8TLQCXGEjD4KASIQnmhfuIYQmDclmowS22ODK7wTayFsn1YRHCrKCpdo1f1YMww"));
    }
}
//1589263436
//1589263436208

//1589264336
//1589264336208