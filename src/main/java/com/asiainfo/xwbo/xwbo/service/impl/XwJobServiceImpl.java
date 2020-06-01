package com.asiainfo.xwbo.xwbo.service.impl;

import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.base.XwGroupInfoDao;
import com.asiainfo.xwbo.xwbo.dao.base.XwJobInfoDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.XwUserInfo;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwJobInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.*;
import com.asiainfo.xwbo.xwbo.model.vo.*;
import com.asiainfo.xwbo.xwbo.service.XwGroupService;
import com.asiainfo.xwbo.xwbo.service.XwJobService;
import com.asiainfo.xwbo.xwbo.service.XwUserService;
import com.asiainfo.xwbo.xwbo.system.XwJobStateInfoLoader;
import com.asiainfo.xwbo.xwbo.system.constants.Constant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-18 14:49
 */
@Service
@Slf4j
@Transactional(value = "baseTransaction")
public class XwJobServiceImpl implements XwJobService {

    @Resource
    private ICommonExtDao commonExtDao;

    @Resource
    private XwJobInfoDao xwJobInfoDao;

    @Resource
    private XwJobStateInfoLoader xwJobStateInfoLoader;

    @Resource
    private XwGroupService xwGroupService;

    @Resource
    private XwUserService xwUserService;

    @Resource
    private XwGroupInfoDao xwGroupInfoDao;


    @Override
    public void syncXwJob(SyncXwJobInfoListSo syncXwJobInfoListSo) throws Exception {
        List<SyncXwJobInfoSo> syncXwJobInfoSoList = syncXwJobInfoListSo.getList();
        if(null != syncXwJobInfoSoList && syncXwJobInfoSoList.size() > 0) {
            XwJobInfoPo po = null;
            for(SyncXwJobInfoSo so : syncXwJobInfoSoList) {
                if(null == so.getJobId()) {
                    //新增
                    //判断集团信息
                    Long groupId = so.getGroupId();
                    XwGroupInfoPo xwGroupInfoPo = xwGroupService.qryGroupInfoPo(groupId);
                    Integer state = xwGroupInfoPo.getHandleState();
                    String handleUserId = xwGroupInfoPo.getLastHandleUser();
                    if(state != Constant.XW_GROUP_HANDLE_STATE.DAIPAIMO || StringUtils.isNotBlank(handleUserId)) {
                        throw new Exception("集团ID: " + groupId + " 已处理");
                    }
                    po = xwJobInfoSoToPo(po, so);
                    po.setCreator(so.getCreator());
                    po.setCreateTime(new Date());
                    po.setIsTimeout(Constant.XW_JOB_TIMEOUT.FALSE);
                    po.setState(Constant.XW_JOB_STATE.DAICHULI);
                    //更新集团信息（乐观锁）
                    Date nowDate = new Date();
                    xwGroupInfoPo.setLastHandleUser(so.getHandleUserId());
                    xwGroupInfoPo.setLastHandleTime(nowDate);
                    xwGroupInfoPo.setLastUpdator(so.getCreator());
                    xwGroupInfoPo.setLastUpdateTime(nowDate);
                    xwGroupInfoPo.setNewHandleState(Constant.XW_GROUP_HANDLE_STATE.DAICHULIRENPAIMO);
                    int updateRow = xwGroupInfoDao.updateHandleUser(xwGroupInfoPo);
                    if(updateRow == 1) {
                        commonExtDao.save(SqlBuilder.build(XwJobInfoPo.class), po);
                    }else {
                        throw new Exception("集团ID: " + groupId + " 已处理");
                    }

                }else {
//                    //修改
//                    Long jobId = so.getJobId();
//                    po = qryXwJobPo(jobId);
//                    po = xwJobInfoSoToPo(po, so);
//                    po.setLastUpdator(so.getCreator());
//                    po.setLastUpdateTime(new Date());
//                    commonExtDao.update(SqlBuilder.build(XwJobInfoPo.class).eq("job_id", po.getJobId()), po);
                }
            }
        }

    }

    @Override
    public PageResultVo qryAcceptJobAll(QryJobInfoSo qryJobInfoSo) throws Exception {
        if(StringUtils.isBlank(qryJobInfoSo.getHandleUserId())) {
            throw new Exception("处理人不能为空");
        }
        return qryJobAll(qryJobInfoSo);
    }

    @Override
    public PageResultVo qryEstablishJobAll(QryJobInfoSo qryJobInfoSo) throws Exception {
        if(StringUtils.isBlank(qryJobInfoSo.getCreator())) {
            throw new Exception("创建人不能为空");
        }
        return qryJobAll(qryJobInfoSo);
    }

    private PageResultVo qryJobAll(QryJobInfoSo qryJobInfoSo) throws Exception {
        PageResultVo pageResultVo = new PageResultVo();
        long total = 0L;
        String pageSize = qryJobInfoSo.getPageSize();
        String pageNum = qryJobInfoSo.getPageNum();
        List<XwJobInfoVo> xwJobInfoVoList = new ArrayList<>();
        List<XwJobInfoPo> xwJobInfoPoList = null;
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)) {
            xwJobInfoPoList = xwJobInfoDao.qryJobAll(qryJobInfoSo);
            total = xwJobInfoPoList.size();
        }else {
            Page pageInfo = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
            xwJobInfoPoList = xwJobInfoDao.qryJobAll(qryJobInfoSo);
            total = pageInfo.getTotal();
        }
        if(null != xwJobInfoPoList && xwJobInfoPoList.size() > 0) {
            for(XwJobInfoPo po : xwJobInfoPoList) {
                xwJobInfoVoList.add(xwJobInfoPoToVo(po, qryJobInfoSo));
            }
        }
        pageResultVo.setList(xwJobInfoVoList);
        pageResultVo.setTotal(total);
        return pageResultVo;

    }

    @Override
    public XwJobInfoVo qryAcceptJobInfo(QryJobInfoSo qryJobInfoSo) throws Exception {
        if(StringUtils.isBlank(qryJobInfoSo.getHandleUserId())) {
            throw new Exception("处理人不能为空");
        }
        XwJobInfoPo po = qryXwJobPo(qryJobInfoSo);
        return xwJobInfoPoToVo(po, qryJobInfoSo);
    }

    @Override
    public XwJobInfoVo qryEstablishJobInfo(QryJobInfoSo qryJobInfoSo) throws Exception {
        if(StringUtils.isBlank(qryJobInfoSo.getCreator())) {
            throw new Exception("创建人不能为空");
        }
        XwJobInfoPo po = qryXwJobPo(qryJobInfoSo);
        return xwJobInfoPoToVo(po, qryJobInfoSo);
    }

    @Override
    public XwJobCountInfoVo qryAllJobCount(QryJobInfoSo qryJobInfoSo) throws Exception {
        if(StringUtils.isBlank(qryJobInfoSo.getHandleUserId()) && StringUtils.isBlank(qryJobInfoSo.getCreator())) {
            throw new Exception("创建人和处理人不能同时为空");
        }
        return xwJobInfoDao.qryAllJobCount(qryJobInfoSo);
    }

    @Override
    public void acceptUpdateHandleMessage(SyncXwJobInfoSo syncXwJobInfoSo) throws Exception {
        XwJobInfoPo po = qryXwJobPo(syncXwJobInfoSo);
        po.setMessage(syncXwJobInfoSo.getMessage());
        po.setLastUpdator(syncXwJobInfoSo.getHandleUserId());
        po.setLastUpdateTime(new Date());
        commonExtDao.update(SqlBuilder.build(XwJobInfoPo.class).eq("job_id", syncXwJobInfoSo.getJobId()).eq("handle_user_id", syncXwJobInfoSo.getHandleUserId()), po);
    }

    @Override
    public void acceptUpdateState(SyncXwJobInfoSo syncXwJobInfoSo) throws Exception {
        XwJobInfoPo po = qryXwJobPo(syncXwJobInfoSo);
        po.setState(syncXwJobInfoSo.getState());
        po.setLastUpdator(syncXwJobInfoSo.getHandleUserId());
        po.setLastUpdateTime(new Date());
        commonExtDao.update(SqlBuilder.build(XwJobInfoPo.class).eq("job_id", syncXwJobInfoSo.getJobId()).eq("handle_user_id", syncXwJobInfoSo.getHandleUserId()), po);
        UpdateHandleStateSo updateHandleStateSo = new UpdateHandleStateSo();
        if(Constant.XW_JOB_STATE.CHULIZHONG == syncXwJobInfoSo.getState().intValue()) {
            updateHandleStateSo.setUserId(syncXwJobInfoSo.getHandleUserId());
            updateHandleStateSo.setHandleState(Constant.XW_GROUP_HANDLE_STATE.PAIMOZHONG);
        }
        if(Constant.XW_JOB_STATE.YIWANCHENG == syncXwJobInfoSo.getState().intValue()) {
            updateHandleStateSo.setUserId(syncXwJobInfoSo.getHandleUserId());
            updateHandleStateSo.setHandleState(Constant.XW_GROUP_HANDLE_STATE.YIPAIMO);
        }

        xwGroupService.changeHandleState(updateHandleStateSo);
    }

    @Override
    public List<XwJobStateInfoVo> jobStateInfo() {
        return xwJobStateInfoLoader.getList();
    }

//    @Override
//    public void updateHandleMessage(SyncXwJobInfoSo syncXwJobInfoSo) throws Exception {
//        QryJobInfoSo qryJobInfoSo = new QryJobInfoSo();
//        qryJobInfoSo.setJobId(syncXwJobInfoSo.getJobId());
//        qryJobInfoSo.setHandleUserId(syncXwJobInfoSo.getHandleUserId());
//        XwJobInfoPo po = qryXwJobPo(qryJobInfoSo);
//        po.setMessage(syncXwJobInfoSo.getMessage());
//        po.setLastUpdator(syncXwJobInfoSo.getUserId());
//        po.setLastUpdateTime(new Date());
//        commonExtDao.update(SqlBuilder.build(XwJobInfoPo.class).eq("job_id", jobId), po);
//
//    }
//
//    @Override
//    public void updateState(SyncXwJobInfoSo syncXwJobInfoSo) throws Exception {
//        QryJobInfoSo qryJobInfoSo = new QryJobInfoSo();
//        qryJobInfoSo.setJobId(syncXwJobInfoSo.getJobId());
//        qryJobInfoSo.setHandleUserId(syncXwJobInfoSo.getHandleUserId());
//        XwJobInfoPo po = qryXwJobPo(qryJobInfoSo);
//        po.setState(syncXwJobInfoSo.getState());
//        po.setLastUpdator(syncXwJobInfoSo.getUserId());
//        po.setLastUpdateTime(new Date());
//        commonExtDao.update(SqlBuilder.build(XwJobInfoPo.class).eq("job_id", jobId), po);
//    }

    private XwJobInfoPo qryXwJobPo(SyncXwJobInfoSo syncXwJobInfoSo) throws Exception {
        QryJobInfoSo qryJobInfoSo = new QryJobInfoSo();
        qryJobInfoSo.setHandleUserId(syncXwJobInfoSo.getHandleUserId());
        qryJobInfoSo.setCreator(syncXwJobInfoSo.getCreator());
        qryJobInfoSo.setJobId(syncXwJobInfoSo.getJobId());
        return qryXwJobPo(qryJobInfoSo);

    }


    private XwJobInfoPo qryXwJobPo(QryJobInfoSo qryJobInfoSo) throws Exception {
//        XwJobInfoPo xwJobInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwJobInfoPo.class).eq("job_id", jobId));
        XwJobInfoPo xwJobInfoPo = xwJobInfoDao.qryJobInfo(qryJobInfoSo);
        if(null == xwJobInfoPo) {
            throw new Exception("无该工单");
        }
        return xwJobInfoPo;
    }

    XwJobInfoPo xwJobInfoSoToPo(XwJobInfoPo po, SyncXwJobInfoSo so) {

        if(null == po) {
            po = new XwJobInfoPo();
        }
        po.setGroupId(so.getGroupId());
        po.setEndTime(so.getEndTime());
        po.setHandleUserId(so.getHandleUserId());
        po.setHandleRequire(so.getHandleRequire());
        return po;
    }

    XwJobInfoVo xwJobInfoPoToVo(XwJobInfoPo po, QryJobInfoSo qryJobInfoSo) throws Exception {
        XwJobInfoVo vo = new XwJobInfoVo();
        Long groupId = po.getGroupId();
        vo.setJobId(po.getJobId());
        QryXwGroupInfoSo qryXwGroupInfoSo = new QryXwGroupInfoSo();
        qryXwGroupInfoSo.setGroupId(groupId);
        XwGroupInfoVo xwGroupInfoVo = null;
        try {
            qryXwGroupInfoSo.setCurrentLng(qryJobInfoSo.getCurrentLng());
            qryXwGroupInfoSo.setCurrentLat(qryJobInfoSo.getCurrentLat());
            xwGroupInfoVo = xwGroupService.qryInfo(qryXwGroupInfoSo);

        }catch (Exception e) {

        }
        vo.setXwGroupInfoVo(xwGroupInfoVo);
        XwUserInfoSo xwUserInfoSo = new XwUserInfoSo();
        String handleUserId = po.getHandleUserId();
        xwUserInfoSo.setUserId(handleUserId);
        XwUserInfo handleUserInfo = xwUserService.qryInfo(xwUserInfoSo);

        vo.setHandlueUserId(handleUserId);
        vo.setHandleUserName(handleUserInfo.getUserName());
        vo.setState(po.getState());
        vo.setStateMessage(Constant.XW_JOB_STATE.MAPPER.get(po.getState()));
        vo.setCreateTime(new DateTime(po.getCreateTime()).toString("yyyy-MM-dd  HH:mm:ss"));
        vo.setEndTime(new DateTime(po.getEndTime()).toString("yyyy-MM-dd"));
        vo.setIsTimeout(po.getIsTimeout());
        vo.setTimeoutMessage(Constant.XW_JOB_TIMEOUT.MAPPER.get(po.getIsTimeout()));
        Integer state = po.getState();
        if(state == Constant.XW_JOB_STATE.DAICHULI || state == Constant.XW_JOB_STATE.CHULIZHONG) {
            DateTime endTime = new DateTime(po.getEndTime());
            DateTime nowDate = new DateTime();
            int days= Days.daysBetween(nowDate.withTimeAtStartOfDay(), endTime.withTimeAtStartOfDay()).getDays();
            vo.setEndTimeMessage(""+(days));
        }
        return vo;
    }
}
