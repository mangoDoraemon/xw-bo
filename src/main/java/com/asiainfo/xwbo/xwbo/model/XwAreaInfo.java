package com.asiainfo.xwbo.xwbo.model;

import lombok.Data;

import java.util.List;


public class XwAreaInfo {
    private String areaName;
    private String areaId;
    private String microId;
    private Integer areaLevel;
    private String areaPid;
    private String center;
    private String areaLocation;
    private List<XwAreaInfo> child;
    private Double rate;
    private Integer sort;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getMicroId() {
        return microId;
    }

    public void setMicroId(String microId) {
        this.microId = microId;
    }

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getAreaPid() {
        return areaPid;
    }

    public void setAreaPid(String areaPid) {
        this.areaPid = areaPid;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getAreaLocation() {
        return areaLocation;
    }

    public void setAreaLocation(String areaLocation) {
        this.areaLocation = areaLocation;
    }

    public List<XwAreaInfo> getChild() {
        return child;
    }

    public void setChild(List<XwAreaInfo> child) {
        this.child = child;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
