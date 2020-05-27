package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

/**
 * @author jiahao jin
 * @create 2020-05-26 14:31
 */
@Data
@Table("xw_weg_mopai_rate")
public class XwWegMopaiRatePo {
    private String microId;
    private Double rate;

}
