package com.asiainfo.xwbo.xwbo.model;

import lombok.Data;

import java.util.List;

@Data
public class XwAreaInfo {
    private String areaName;
    private String areaId;
    private Integer areaLevel;
    private String areaPid;
    private String center;
    private String areaLocation;
    private List<XwAreaInfo> child;
}
