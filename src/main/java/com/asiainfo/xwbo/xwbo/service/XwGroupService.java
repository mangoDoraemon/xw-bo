package com.asiainfo.xwbo.xwbo.service;

import com.asiainfo.xwbo.xwbo.model.vo.XwIndustryClassInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.PageResultVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwGroupInfoVo;
import com.asiainfo.xwbo.xwbo.model.so.QryPeripheryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.QryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwGroupInfoSo;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-06 10:34
 */
public interface XwGroupService {
    PageResultVo qryAll(QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo) throws Exception;

    XwGroupInfoVo qryInfo(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception;

    PageResultVo qryMember(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception;

    void sync(SyncXwGroupInfoSo syncXwGroupInfoSo) throws Exception;

    List<XwIndustryClassInfoVo> industryClass() throws Exception;
}
