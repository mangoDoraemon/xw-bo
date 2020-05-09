package com.asiainfo.xwbo.xwbo.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-08 17:57
 */
@Data
public class XwIndustryClassInfoVo {
    private Long id;
    private String name;
    private List<XwIndustryClassInfoVo> child;
}
