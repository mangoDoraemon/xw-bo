package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-08 14:10
 */
@Data
public class QryXwGroupInfoSo extends SignSo{
    private Long groupId;
    private List<Long> groupList;
    private String currentLng;
    private String currentLat;
}
