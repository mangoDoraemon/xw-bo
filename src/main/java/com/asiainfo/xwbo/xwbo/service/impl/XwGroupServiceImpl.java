package com.asiainfo.xwbo.xwbo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.base.XwGroupInfoDao;
import com.asiainfo.xwbo.xwbo.dao.base.XwUserInfoDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.po.XwUserInfoPo;
import com.asiainfo.xwbo.xwbo.model.vo.XwIndustryClassInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.PageResultVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwGroupInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwGroupMemberInfoVo;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupMemberInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.QryPeripheryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.QryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.XwGroupMemberInfoSo;
import com.asiainfo.xwbo.xwbo.service.XwGroupService;
import com.asiainfo.xwbo.xwbo.system.XwIndustryClassInfoLoader;
import com.asiainfo.xwbo.xwbo.system.constants.Constant;
import com.asiainfo.xwbo.xwbo.utils.DistanceUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jiahao jin
 * @create 2020-05-06 10:34
 */
@Service
@Slf4j
@Transactional(value = "baseTransaction")
public class XwGroupServiceImpl implements XwGroupService {

    @Resource
    private XwGroupInfoDao xwGroupDao;

    @Resource
    private XwUserInfoDao xwUserInfoDao;

    @Resource
    private ICommonExtDao commonExtDao;

    @Resource
    private XwIndustryClassInfoLoader xwIndustryClassInfoLoader;

    @Override
    public PageResultVo qryAll(QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo) throws Exception {
        PageResultVo pageResultVo = new PageResultVo();
        String userId = qryPeripheryXwGroupInfoSo.getUserId();

        //人员信息
        XwUserInfoPo xwUserInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwUserInfoPo.class).eq("user_id", userId));
        if(null == xwUserInfoPo) {
            throw new Exception("无该人员信息");
        }
        Integer roleId = xwUserInfoPo.getRoleId();
        long total;
        String pageNum = qryPeripheryXwGroupInfoSo.getPageNum();
        String pageSize = qryPeripheryXwGroupInfoSo.getPageSize();
        List<XwGroupInfoPo> xwGroupInfoPoList;
        if(roleId.intValue() == Constant.XW_USER_ROLE_ID.ZHIXIAORENYUAN.intValue()) {
            //直销人员
            String microIds = xwUserInfoPo.getMicroId();
            if(StringUtils.isBlank(microIds)) {
                throw new Exception("该用户没有所属微格");
            }
            List<String> microIdList  = xwUserInfoDao.qryUserAreaInfo(userId);
            qryPeripheryXwGroupInfoSo.setMicroIdList(microIdList);
        }else if (roleId.intValue() == Constant.XW_USER_ROLE_ID.ZHIXIAORENYUAN.intValue()) {
            //负责人
            String areaCode = xwUserInfoPo.getAreaCode();
        }else if (roleId.intValue() == Constant.XW_USER_ROLE_ID.WANGGEZHANG.intValue()) {
            //网格长
            String gridId = xwUserInfoPo.getAreaId();
        }else if (roleId.intValue() == Constant.XW_USER_ROLE_ID.WANGGEDUTAO.intValue()) {
            //负责人
            String areaCode = xwUserInfoPo.getAreaCode();
        }

        List<XwGroupInfoVo> xwGroupInfoVoList = new ArrayList<>();
        if(StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
            Page pageInfo = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
            xwGroupInfoPoList = xwGroupDao.qryAll(qryPeripheryXwGroupInfoSo);
            total = pageInfo.getTotal();
        }else {
            xwGroupInfoPoList = xwGroupDao.qryAll(qryPeripheryXwGroupInfoSo);
            total = xwGroupInfoPoList.size();
        }
        xwGroupInfoPoList.forEach(po -> {
            XwGroupInfoVo xwGroupInfoVo = xwGroupInfoPoToVo(po);
            double distance = DistanceUtil.getDistance(qryPeripheryXwGroupInfoSo.getCurrentLng(), qryPeripheryXwGroupInfoSo.getCurrentLat(), po.getLng(), po.getLat());
            xwGroupInfoVo.setDistance(distance);
            xwGroupInfoVoList.add(xwGroupInfoVo);
        });

        pageResultVo.setList(
            xwGroupInfoVoList.stream().sorted(Comparator.comparing(XwGroupInfoVo::getDistance)).collect(Collectors.toList())
        );
        pageResultVo.setTotal(total);
        return pageResultVo;



//        List<String> microIdList  = xwUserInfoDao.qryUserAreaInfo(userId);
//        System.out.println("microIdList1: "+ microIdList);
//        if(null == microIdList || microIdList.size() == 0) {
//            throw new Exception("该用户没有所属微格");
//        }
//
//        qryPeripheryXwGroupInfoSo.setMicroIdList(microIdList);
//        String pageNum = qryPeripheryXwGroupInfoSo.getPageNum();
//        String pageSize = qryPeripheryXwGroupInfoSo.getPageSize();
//        List<XwGroupInfoPo> xwGroupInfoPoList;
//        if(StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
//            Page pageInfo = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
//            xwGroupInfoPoList = xwGroupDao.qryAll(qryPeripheryXwGroupInfoSo);
//            total = pageInfo.getTotal();
//        }else {
//            xwGroupInfoPoList = xwGroupDao.qryAll(qryPeripheryXwGroupInfoSo);
//            total = xwGroupInfoPoList.size();
//        }
//        System.out.println(total);
//        List<XwGroupInfoVo> xwGroupInfoVoList = new ArrayList<>();
//        xwGroupInfoPoList.forEach(po -> {
//            XwGroupInfoVo xwGroupInfoVo = xwGroupInfoPoToVo(po);
//            double distance = DistanceUtil.getDistance(qryPeripheryXwGroupInfoSo.getCurrentLng(), qryPeripheryXwGroupInfoSo.getCurrentLat(), po.getLng(), po.getLat());
//            xwGroupInfoVo.setDistance(distance);
//            xwGroupInfoVoList.add(xwGroupInfoVo);
//        });
//
//        pageResultVo.setList(
//            xwGroupInfoVoList.stream().sorted(Comparator.comparing(XwGroupInfoVo::getDistance)).collect(Collectors.toList())
//        );
//        pageResultVo.setTotal(total);
//        return pageResultVo;
        return null;
    }

    @Override
    public XwGroupInfoVo qryInfo(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        String groupId = qryXwGroupInfoSo.getGroupId();
        XwGroupInfoPo xwGroupInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwGroupInfoPo.class).eq("id", groupId));
        XwGroupInfoVo xwGroupInfoVo = xwGroupInfoPoToVo(xwGroupInfoPo);
        return xwGroupInfoVo;
    }

    @Override
    public PageResultVo qryMember(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        PageResultVo pageResultVo = new PageResultVo();
        List<XwGroupMemberInfoVo> xwGroupMemberInfoVoList = new ArrayList<>();
        String groupId = qryXwGroupInfoSo.getGroupId();
        List<XwGroupMemberInfoPo> xwGroupMemberInfoPoList = commonExtDao.query(SqlBuilder.build(XwGroupMemberInfoPo.class).eq("group_id", groupId));

        xwGroupMemberInfoPoList.forEach(po -> {
            XwGroupMemberInfoVo xwGroupMemberInfoVo = xwGroupMemberInfoPoToVo(po);
            xwGroupMemberInfoVoList.add(xwGroupMemberInfoVo);
        });
        pageResultVo.setList(xwGroupMemberInfoVoList);
        pageResultVo.setTotal(xwGroupMemberInfoVoList.size());
        return pageResultVo;
    }

    @Override
    public void sync(SyncXwGroupInfoSo syncXwGroupInfoSo) throws Exception {
        Long groupId = syncXwGroupInfoSo.getId();
        String userId = syncXwGroupInfoSo.getUserId();
        XwGroupInfoPo xwGroupInfoPo = null;
        if(null == groupId) {
            //新增
            //获取id

            xwGroupInfoPo = syncXwGroupInfoSoToPo(xwGroupInfoPo, syncXwGroupInfoSo);
            groupId = commonExtDao.saveReturnKey(SqlBuilder.build(XwGroupInfoPo.class), xwGroupInfoPo);

            syncXwGroupInfoSo.setId(groupId);

        }else {
            //修改
            xwGroupInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwGroupInfoPo.class).eq("id", groupId));
            if(null == xwGroupInfoPo) {
                throw new Exception("无该集团信息");
            }
            System.out.println("修改前"+JSONObject.toJSONString(xwGroupInfoPo));
            syncXwGroupInfoSoToPo(xwGroupInfoPo, syncXwGroupInfoSo);
            System.out.println("修改后"+JSONObject.toJSONString(xwGroupInfoPo));
            xwGroupInfoPo.setLastUpdator(userId);
            xwGroupInfoPo.setLastUpdateTime(new Date());
            commonExtDao.update(SqlBuilder.build(XwGroupInfoPo.class).eq("id", groupId), xwGroupInfoPo);

            //删除成员
            commonExtDao.delete(SqlBuilder.build(XwGroupMemberInfoPo.class).eq("group_id", groupId));

        }
        List<XwGroupMemberInfoPo> xwGroupMemberInfoPoList = new ArrayList<>();
        List<XwGroupMemberInfoSo> xwGroupMemberInfoSoList = syncXwGroupInfoSo.getXwGroupMemberList();
        if(null != xwGroupMemberInfoSoList && xwGroupMemberInfoSoList.size() > 0) {
            xwGroupMemberInfoSoList.forEach(xwGroupMemberInfoSo -> {
                XwGroupMemberInfoPo xwGroupMemberInfoPo = xwGroupMemberInfoSoToPo(xwGroupMemberInfoSo, syncXwGroupInfoSo);
                xwGroupMemberInfoPoList.add(xwGroupMemberInfoPo);
            });
        }

        if(xwGroupMemberInfoPoList.size() > 0) {
            commonExtDao.save(SqlBuilder.build(XwGroupMemberInfoPo.class), xwGroupMemberInfoPoList);
        }
    }

    @Override
    public List<XwIndustryClassInfoVo> industryClass() throws Exception {
        return xwIndustryClassInfoLoader.getHeaderList();
    }



    private XwGroupMemberInfoPo xwGroupMemberInfoSoToPo(XwGroupMemberInfoSo xwGroupMemberInfoSo, SyncXwGroupInfoSo syncXwGroupInfoSo) {
        XwGroupMemberInfoPo xwGroupMemberInfoPo = new XwGroupMemberInfoPo();
        xwGroupMemberInfoPo.setUserName(xwGroupMemberInfoSo.getUserName());
        xwGroupMemberInfoPo.setUserPhone(xwGroupMemberInfoSo.getUserPhone());
        xwGroupMemberInfoPo.setCreator(syncXwGroupInfoSo.getUserId());
        xwGroupMemberInfoPo.setGroupId(syncXwGroupInfoSo.getId());
        xwGroupMemberInfoPo.setCreateTime(new Date());
        return xwGroupMemberInfoPo;
    }

    private XwGroupMemberInfoVo xwGroupMemberInfoPoToVo(XwGroupMemberInfoPo xwGroupMemberInfoPo) {
        XwGroupMemberInfoVo xwGroupMemberInfoVo = new XwGroupMemberInfoVo();
        xwGroupMemberInfoVo.setUserName(xwGroupMemberInfoPo.getUserName());
        xwGroupMemberInfoVo.setUserPhone(xwGroupMemberInfoPo.getUserPhone());
        return xwGroupMemberInfoVo;
    }

    private XwGroupInfoPo syncXwGroupInfoSoToPo(XwGroupInfoPo xwGroupInfoPo, SyncXwGroupInfoSo syncXwGroupInfoSo) {

        if(null == xwGroupInfoPo) {
            xwGroupInfoPo = new XwGroupInfoPo();
        }
        xwGroupInfoPo.setName(syncXwGroupInfoSo.getName());
        xwGroupInfoPo.setAddress(syncXwGroupInfoSo.getAddress());
        xwGroupInfoPo.setProvId(syncXwGroupInfoSo.getProvId());
        xwGroupInfoPo.setCityId(syncXwGroupInfoSo.getCityId());
        xwGroupInfoPo.setCountyId(syncXwGroupInfoSo.getCountyId());
        xwGroupInfoPo.setAreaId(syncXwGroupInfoSo.getAreaId());
        xwGroupInfoPo.setGridId(syncXwGroupInfoSo.getGridId());
        xwGroupInfoPo.setMicroId(syncXwGroupInfoSo.getMicroId());
        xwGroupInfoPo.setFirstClass(syncXwGroupInfoSo.getFisrtClass());
        xwGroupInfoPo.setSecondClass(syncXwGroupInfoSo.getSecondClass());
        xwGroupInfoPo.setManagementState(syncXwGroupInfoSo.getManagementState());
        xwGroupInfoPo.setCreditCode(syncXwGroupInfoSo.getCreditCode());
        xwGroupInfoPo.setHandleState(syncXwGroupInfoSo.getHandleState());
        xwGroupInfoPo.setRelationGroupId(syncXwGroupInfoSo.getRelationGroupId());
        xwGroupInfoPo.setPhone(syncXwGroupInfoSo.getPhone());
        xwGroupInfoPo.setLng(syncXwGroupInfoSo.getLng());
        xwGroupInfoPo.setLat(syncXwGroupInfoSo.getLat());
        xwGroupInfoPo.setCreator(syncXwGroupInfoSo.getUserId());
        xwGroupInfoPo.setCreateTime(new Date());
        return xwGroupInfoPo;
    }


    private XwGroupInfoVo xwGroupInfoPoToVo(XwGroupInfoPo xwGroupInfoPo) {

        XwGroupInfoVo xwGroupInfoVo = XwGroupInfoVo.builder()
                .id(xwGroupInfoPo.getId())
                .name(xwGroupInfoPo.getName())
                .creditCode(xwGroupInfoPo.getCreditCode())
                .address(xwGroupInfoPo.getAddress())
                .provId(xwGroupInfoPo.getProvId())
                .cityId(xwGroupInfoPo.getCityId())
                .countyId(xwGroupInfoPo.getCountyId())
                .areaId(xwGroupInfoPo.getAreaId())
                .gridId(xwGroupInfoPo.getGridId())
                .microId(xwGroupInfoPo.getMicroId())
                .firstClass(xwGroupInfoPo.getFirstClass())
                .secondClass(xwGroupInfoPo.getSecondClass())
                .phone(xwGroupInfoPo.getPhone())
                .managementState(Constant.XW_GROUP_MANAGEMENT_STATE.MAPPER.get(xwGroupInfoPo.getManagementState()))
                .handleState(Constant.XW_GROUP_HANDLE_STATE.MAPPER.get(xwGroupInfoPo.getHandleState()))
                .relationGroupId(xwGroupInfoPo.getRelationGroupId())
                .lng(xwGroupInfoPo.getLng())
                .lat(xwGroupInfoPo.getLat())
                .lastHandleUser(xwGroupInfoPo.getLastHandleUser())
                .lastHandelTime(xwGroupInfoPo.getLastHandelTime()).build();
        return xwGroupInfoVo;
    }
}
