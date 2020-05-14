package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-13 17:14
 */
@Data
@Table("xw_product_info")
public class XwProductInfoPo {
    private Integer id;
    private String productName;
    private String productType;
    private String productDesc;
}
