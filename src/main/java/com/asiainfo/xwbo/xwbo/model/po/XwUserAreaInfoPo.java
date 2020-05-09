package com.asiainfo.xwbo.xwbo.model.po;

import lombok.Data;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-05-06 17:14
 */
@Data
public class XwUserAreaInfoPo {

    private String userId;
    private String microId;

    private String creator;
    private Date createTime;
    private String lastUpdator;
    private Date lastUpdateTime;
}
