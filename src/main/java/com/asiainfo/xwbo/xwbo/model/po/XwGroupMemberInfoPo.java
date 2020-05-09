package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-04-30 16:06
 */
@Data
@Table("xw_group_member_info")
public class XwGroupMemberInfoPo {
    private Long groupId;
    private String userName;
    private String userPhone;

    private String creator;
    private Date createTime;
    private String lastUpdator;
    private Date lastUpdateTime;
}
