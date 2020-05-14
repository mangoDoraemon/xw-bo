package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-09 12:00
 */
@Data
public class QryAreaInfoSo {
    private String areaId;
    private String areaLevel;
    private List<String> mircoIdList;
}
