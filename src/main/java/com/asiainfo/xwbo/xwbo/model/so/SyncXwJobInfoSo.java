package com.asiainfo.xwbo.xwbo.model.so;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author jiahao jin
 * @create 2020-05-18 14:38
 */
@Data

public class SyncXwJobInfoSo extends SignSo{

    private Long jobId;
    private Long groupId;
    private Integer handleRequire;
    private String handleUserId;
    private String message;
    private String creator;
    private Date endTime;
    private Integer state;

}
