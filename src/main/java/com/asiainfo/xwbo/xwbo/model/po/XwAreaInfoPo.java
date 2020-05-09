package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-09 14:10
 */

@Data
@Table("xw_area_grid_info")
public class XwAreaInfoPo {

    private String areaId;
    private String areaName;
    private Integer areaLevel;
    private String areaPid;
    private String areaLocation;
    private String areaInfo;
    private String centralPoint;


}
