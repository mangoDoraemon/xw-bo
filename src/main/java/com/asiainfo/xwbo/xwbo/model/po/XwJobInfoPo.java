package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

import java.util.Date;


/**
 * @author jiahao jin
 * @create 2020-05-18 14:34
 */
@Data
@Table("xw_job_info")
public class XwJobInfoPo {
    private Long jobId;
    private Long groupId;
    private Integer handleRequire;
    private String handleUserId;
    private Integer state;
    private String message;
    private String creator;
    private Date createTime;
    private Date endTime;
    private String lastUpdator;
    private Date lastUpdateTime;
    private String jobName;

}
