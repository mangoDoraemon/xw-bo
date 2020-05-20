package com.asiainfo.xwbo.xwbo.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-05-06 09:33
 */
@Data
@Builder
public class XwGroupInfoVo {
    private Long id;
    private String name;
    private String creditCode;
    private String address;
    private String roomNo;
    private String provId;
    private String provName;
    private String cityId;
    private String cityName;
    private String countyId;
    private String countyName;
    private String gridId;
    private String gridName;
    private String microId;
    private String microName;
    private Integer firstClass;
    private Integer secondClass;
    private String firstClassName;
    private String secondClassName;
    private String phone;
    private Integer managementState;
    private Integer handleState;
    private Integer zaiwang;
    private String managementStateMessage;
    private String handleStateMessage;
    private String zaiwangMessage;
    private String relationGroupId;
    private String lng;
    private String lat;
    private Long distance;
    private String lastHandleUser;
    private String lastHandelTime;
    private String createTime;

}
