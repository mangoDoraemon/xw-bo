package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-20 10:32
 */
@Data
@Table("xw_view_user_info")
public class XwViewUserInfo {
    private String userId;
    private Integer sevenNum;
    private Integer jobInNum;
    private Double rate;
}
