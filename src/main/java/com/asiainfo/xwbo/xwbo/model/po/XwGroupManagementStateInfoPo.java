package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-08 17:44
 */
@Table("xw_group_management_state_info")
@Data
public class XwGroupManagementStateInfoPo {
    private Long id;
    private String message;
}
