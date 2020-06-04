package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-06-01 14:20
 */
@Table("xw_view_group_num")
@Data
public class XwViewGroupNumPo {
    private String areaId;
    private Integer weekNewNum;
    private Integer weekDaimopai;
    private Integer weekMopaizhong;
    private Integer weekYimopai;
    private Integer weekZaiwang;
    private Integer allNum;
    private Integer allDaimopai;
    private Integer allMopaizhong;
    private Integer allYimopai;
    private Integer allZaiwang;

}
