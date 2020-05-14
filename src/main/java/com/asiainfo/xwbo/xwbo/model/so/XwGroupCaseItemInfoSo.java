package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-14 11:06
 */
@Data
public class XwGroupCaseItemInfoSo {
    private Long groupId;
    private Integer itemId;
    private String areaName;
    private String title;
    private String caseType;
    private String tradeType;
    private String con;
    private String createDate;
    private String creator;
}
