package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-05-14 09:49
 */
@Data
@Table("xw_case_item_info")
public class XwCaseItemInfoPo {
    private Integer id;
    private String areaName;
    private String title;
    private String caseType;
    private String tradeType;
    private String con;
    private Date createDate;
    private String creator;
}
