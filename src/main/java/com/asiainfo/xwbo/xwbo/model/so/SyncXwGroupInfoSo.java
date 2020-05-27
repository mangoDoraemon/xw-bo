package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-06 14:48
 */
@Data
public class SyncXwGroupInfoSo extends SignSo{
    private Long id;
    private String name;
    private String creditCode;
    private String address;
    private String roomNo;
    private String provId;
    private String cityId;
    private String countyId;
    private String areaId;
    private String gridId;
    private String microId;
    private Integer firstClass;
    private Integer secondClass;
    private String phone;
    private Integer managementState;
    private Integer handleState;
    private String relationGroupId;
    private String lng;
    private String lat;
    private String lastHandleUser;
    private Date lastHandelTime;
    private String userId;
    private List<XwGroupMemberInfoSo> xwGroupMemberList;
}
