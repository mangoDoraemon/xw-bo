package com.asiainfo.xwbo.xwbo.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-18 09:49
 */
@Data
public class XwUserRoleInfoVo {

    private Integer id;
    private String name;

    public XwUserRoleInfoVo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
