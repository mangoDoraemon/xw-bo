package com.asiainfo.xwbo.xwbo.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author jiahao jin
 * @create 2020-05-19 10:11
 */
@Data
public class XwJobInfoVo {
    private Long jobId;
    private Long groupId;
    private Integer state;
    private String stateMessage;
    private String jobName;
    private String endTime;
    private String endTimeMessage;
}
