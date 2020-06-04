package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Transient;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-26 14:31
 */

@Table("xw_weg_mopai_rate")
public class XwWegMopaiRatePo {
    private String areaId;
    private Double rate;
    @Transient
    private int sort;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
