package com.asiainfo.xwbo.xwbo.dao.base;

import com.asiainfo.xwbo.xwbo.model.po.XwJobInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.QryJobInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.XwJobCountInfoVo;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-19 10:14
 */
public interface XwJobInfoDao {

    List<XwJobInfoPo> qryJobAll(QryJobInfoSo qryJobInfoSo);

    XwJobInfoPo qryJobInfo(QryJobInfoSo qryJobInfoSo);

    XwJobCountInfoVo qryAllJobCount(QryJobInfoSo qryJobInfoSo);
}
