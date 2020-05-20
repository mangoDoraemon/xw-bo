package com.asiainfo.xwbo.xwbo.service.impl;

import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.base.XwJobInfoDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwJobInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.QryJobInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwJobInfoListSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwJobInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.PageResultVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwJobCountInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwJobInfoVo;
import com.asiainfo.xwbo.xwbo.service.XwJobService;
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


    @Override
    public void syncXwJob(SyncXwJobInfoListSo syncXwJobInfoListSo) throws Exception {
        List<SyncXwJobInfoSo> syncXwJobInfoSoList = syncXwJobInfoListSo.getList();
        if(null != syncXwJobInfoSoList && syncXwJobInfoSoList.size() > 0) {
            XwJobInfoPo po = null;
            for(SyncXwJobInfoSo so : syncXwJobInfoSoList) {
                if(null == so.getJobId()) {
                    //新增
                    po = xwJobInfoSoToPo(po, so);
                    po.setCreator(so.getCreator());
                    po.setCreateTime(new Date());
                    po.setState(Constant.XW_JOB_STATE.DAICHULI);
                    commonExtDao.save(SqlBuilder.build(XwJobInfoPo.class), po);
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

    private PageResultVo qryJobAll(QryJobInfoSo qryJobInfoSo) {
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
            xwJobInfoPoList.forEach(po -> {
                xwJobInfoVoList.add(xwJobInfoPoToVo(po));
            });
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
        return xwJobInfoPoToVo(po);
    }

    @Override
    public XwJobInfoVo qryEstablishJobInfo(QryJobInfoSo qryJobInfoSo) throws Exception {
        if(StringUtils.isBlank(qryJobInfoSo.getCreator())) {
            throw new Exception("创建人不能为空");
        }
        XwJobInfoPo po = qryXwJobPo(qryJobInfoSo);
        return xwJobInfoPoToVo(po);
    }

    @Override
    public XwJobCountInfoVo qryAllJobCount(QryJobInfoSo qryJobInfoSo) throws Exception {
        if(StringUtils.isBlank(qryJobInfoSo.getHandleUserId()) || StringUtils.isBlank(qryJobInfoSo.getCreator())) {
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
        commonExtDao.update(SqlBuilder.build(XwJobInfoPo.class).eq("job_id", syncXwJobInfoSo.getJobId()), po);
    }

    @Override
    public void acceptUpdateState(SyncXwJobInfoSo syncXwJobInfoSo) throws Exception {
        XwJobInfoPo po = qryXwJobPo(syncXwJobInfoSo);
        po.setState(syncXwJobInfoSo.getState());
        po.setLastUpdator(syncXwJobInfoSo.getHandleUserId());
        po.setLastUpdateTime(new Date());
        commonExtDao.update(SqlBuilder.build(XwJobInfoPo.class).eq("job_id", syncXwJobInfoSo.getJobId()), po);
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
        po.setJobName(so.getJobName());
        return po;
    }

    XwJobInfoVo xwJobInfoPoToVo(XwJobInfoPo po) {
        XwJobInfoVo vo = new XwJobInfoVo();
        if(null == po) {
            return vo;
        }
        vo.setGroupId(po.getGroupId());
        vo.setJobId(po.getJobId());
        vo.setJobName(po.getJobName());
        vo.setState(po.getState());
        vo.setStateMessage(Constant.XW_JOB_STATE.MAPPER.get(po.getState()));
        vo.setJobName(po.getJobName());
        vo.setEndTime(new DateTime(po.getEndTime()).toString("yyyy-MM-dd"));
        Integer state = po.getState();
        if(state == Constant.XW_JOB_STATE.DAICHULI || state == Constant.XW_JOB_STATE.CHULIZHONG) {
            DateTime endTime = new DateTime(po.getEndTime());
            System.out.println(endTime);
            DateTime nowDate = new DateTime();
            int days= Days.daysBetween(nowDate, endTime).getDays();
            vo.setEndTimeMessage(""+(days+1));
        }
        return vo;
    }
}
