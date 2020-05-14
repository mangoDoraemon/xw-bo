package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-13 17:26
 */
@Data
@Table("xw_group_product_info")
public class XwGroupProductInfoPo {
    private Long groupId;
    private Integer productId;
}
