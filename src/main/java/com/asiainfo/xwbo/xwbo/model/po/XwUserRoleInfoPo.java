package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-18 10:18
 */
@Data
@Table("xw_user_role_info")
public class XwUserRoleInfoPo {
    private Integer roleId;
    private Integer subRoleId;
}
