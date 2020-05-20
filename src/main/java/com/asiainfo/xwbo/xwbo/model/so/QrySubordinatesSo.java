package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-16 15:13
 */
@Data
public class QrySubordinatesSo extends XwUserInfoSo{
    private String areaId;
    private Integer areaLevel;
    private Integer qryRoleId;
    private String pageNum;
    private String pageSize;
    private List<Integer> roleIdList;
    private String qryName;
    private String qryCode;
}
