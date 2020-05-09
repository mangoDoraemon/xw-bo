package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-05-06 16:14
 */
@Data
@Table("xw_user_info")
public class XwUserInfoPo {

    private String userId;
    private String userCode;
    private String userPassword;
    private String userName;
    private String mobilePhone;
    private String emailAddress;
    private Integer roleId;
    private String areaCode;
    private String areaId;
    private String microId;

    private String creator;
    private Date createTime;
    private String lastUpdator;
    private Date lastUpdateTime;
}
