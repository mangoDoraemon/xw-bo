package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-05-27 14:18
 */
@Data
@Table("xw_user_handle_info")
public class XwUserHandleInfoPo {
    private String userId;
    private String url;
    private String params;
    private String result;
    private String type;
    private Date createTime;
    private String cityId;
    private String feature;
}
