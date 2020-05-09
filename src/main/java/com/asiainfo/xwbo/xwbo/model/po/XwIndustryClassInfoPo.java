package com.asiainfo.xwbo.xwbo.model.po;

import com.asiainfo.xwbo.xwbo.dao.sqlBuild.Table;
import lombok.Data;

import java.util.Objects;

/**
 * @author jiahao jin
 * @create 2020-05-08 17:17
 */
@Table("xw_industry_class_info")
@Data
public class XwIndustryClassInfoPo {
    private Long id;
    private String name;
    private Long pid;
    private Integer level;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XwIndustryClassInfoPo that = (XwIndustryClassInfoPo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(pid, that.pid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pid);
    }
}
