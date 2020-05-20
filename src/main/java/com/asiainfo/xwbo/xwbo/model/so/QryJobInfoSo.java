package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-19 10:05
 */
@Data
public class QryJobInfoSo extends XwUserInfoSo {
    private Long jobId;

    private String creator;
    private String handleUserId;

    private Integer state;
    private String pageNum;
    private String pageSize;
    private String keywords;
}
