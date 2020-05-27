package com.asiainfo.xwbo.xwbo.service;

import com.asiainfo.xwbo.xwbo.model.so.QryJobInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwJobInfoListSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwJobInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.PageResultVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwJobCountInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwJobInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwJobStateInfoVo;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-18 14:49
 */
public interface XwJobService {

    void syncXwJob(SyncXwJobInfoListSo syncXwJobInfoListSo) throws Exception;

    PageResultVo qryAcceptJobAll(QryJobInfoSo qryJobInfoSo) throws Exception;

    PageResultVo qryEstablishJobAll(QryJobInfoSo qryJobInfoSo) throws Exception;

    XwJobInfoVo qryAcceptJobInfo(QryJobInfoSo qryJobInfoSo) throws Exception;

    XwJobInfoVo qryEstablishJobInfo(QryJobInfoSo qryJobInfoSo) throws Exception;

    XwJobCountInfoVo qryAllJobCount(QryJobInfoSo qryJobInfoSo) throws Exception;

    void acceptUpdateHandleMessage(SyncXwJobInfoSo syncXwJobInfoSo) throws Exception;

    void acceptUpdateState(SyncXwJobInfoSo syncXwJobInfoSo) throws Exception;

    List<XwJobStateInfoVo> jobStateInfo();


}
