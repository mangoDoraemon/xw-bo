package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-14 09:49
 */
@Data
@Table("xw_group_case_item_info")
public class XwGroupCaseItemInfoPo {
    private Long groupId;
    private Integer itemId;

}
