package com.asiainfo.xwbo.xwbo.model;

import lombok.Data;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-11 18:02
 */
@Data
public class XwUserInfo {
    private String userId;
    private String userCode;
    private String userName;
    private String mobilePhone;
    private String emailAddress;
    private String oaCode;
    private Integer roleId;
    private String role;
    private String areaCode;
    private Integer areaLevel;
    private String provId;
    private String provName;
    private String cityId;
    private String cityName;
    private String countyId;
    private String countyName;
    private String gridId;
    private String gridName;
    private String microIds;
    private List<XwAreaInfo> microIdList;
    private String token;
}
