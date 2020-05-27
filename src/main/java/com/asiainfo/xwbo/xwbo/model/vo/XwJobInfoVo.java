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
    private XwGroupInfoVo xwGroupInfoVo;
    private Integer state;
    private Integer isTimeout;
    private String stateMessage;
    private String timeoutMessage;
    private String endTime;
    private String endTimeMessage;
    private String handlueUserId;
    private String handleUserName;
    private String createTime;
}
