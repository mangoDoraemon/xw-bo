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
    private String provId;
    private String cityId;
    private String countyId;
    private String areaId;
    private String gridId;
    private String microId;
    private Integer firstClass;
    private Integer secondClass;
    private String phone;
    private String managementState;
    private String handleState;
    private String relationGroupId;
    private String lng;
    private String lat;
    private Double distance;
    private String lastHandleUser;
    private Date lastHandelTime;

}
