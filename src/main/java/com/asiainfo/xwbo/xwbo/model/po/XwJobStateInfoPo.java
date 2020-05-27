package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-20 15:45
 */
@Table("xw_job_state_info")
@Data
public class XwJobStateInfoPo {
    private Integer id;
    private String message;
}
