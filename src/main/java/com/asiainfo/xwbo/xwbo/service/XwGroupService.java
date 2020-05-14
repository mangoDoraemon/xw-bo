package com.asiainfo.xwbo.xwbo.service;

import com.asiainfo.xwbo.xwbo.model.so.*;
import com.asiainfo.xwbo.xwbo.model.vo.*;

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

    void changeHandleState(UpdateHandelStateSo updateHandelStateSo) throws Exception;

    List<XwGroupProductInfoSo> qryProductInfo(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception;

    List<XwGroupCaseItemInfoSo> qryCaseItemInfo(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception;

    List<XwIndustryClassInfoVo> industryClass() throws Exception;

    List<XwGroupManagementStateInfoVo> managementState();

    List<XwGroupHandleStateInfoVo> handleState();
}
