package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Transient;
import lombok.Data;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-04-30 15:57
 */

@Data
@Table("xw_group_info")
public class XwGroupInfoPo {
    private Long id;
    private String name;
    private String creditCode;
    private String address;
    private String roomNo;
    private String provId;
    private String cityId;
    private String countyId;
    private String gridId;
    private String microId;
    private Integer firstClass;
    private Integer secondClass;
    private String phone;
    private Integer managementState;
    private Integer handleState;
    private Integer zaiwang;
    private String relationGroupId;
    private String lng;
    private String lat;

    private String lastHandleUser;
    private Date lastHandleTime;

    private String creator;
    private Date createTime;
    private String lastUpdator;
    private Date lastUpdateTime;

    @Transient
    private Integer newHandleState;
}
