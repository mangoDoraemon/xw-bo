package com.asiainfo.xwbo.xwbo.model.so;


import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-14 12:10
 */
@Data
public class UpdateHandleStateSo extends QryXwGroupInfoSo {
    private String userId;
    private Integer handleState;
}
