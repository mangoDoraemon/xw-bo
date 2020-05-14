package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-06 09:33
 */
@Data
public class QryPeripheryXwGroupInfoSo extends XwUserInfoSo {
    private Integer handleState;
    private Integer managementState;
    private Integer firstClass;
    private Integer secondClass;
    private List<String> microIdList;
    private String currentLng;
    private String currentLat;
    private String leftBottomLng;
    private String leftBottomLat;
    private String rightTopLng;
    private String rightTopLat;
    private String pageNum;
    private String pageSize;
    private String keywords;
    private String areaId;
    private Integer areaLevel;
    private Date beginCreateDate;
    private Date endCreateDate;

}
