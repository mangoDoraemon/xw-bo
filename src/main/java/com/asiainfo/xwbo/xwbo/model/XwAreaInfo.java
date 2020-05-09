package com.asiainfo.xwbo.xwbo.model;

import lombok.Data;

import java.util.List;

@Data
public class XwAreaInfo {
    private String area_name;
    private String area_id;
    private Integer area_level;
    private String area_pid;
    private String center;
    private String area_location;
    private List<XwAreaInfo> child;
}
