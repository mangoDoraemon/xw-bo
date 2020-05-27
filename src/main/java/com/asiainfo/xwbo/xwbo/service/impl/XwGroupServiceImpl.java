package com.asiainfo.xwbo.xwbo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.base.XwGroupInfoDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.XwUserInfo;
import com.asiainfo.xwbo.xwbo.model.po.XwAreaInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupMemberInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.*;
import com.asiainfo.xwbo.xwbo.model.vo.*;
import com.asiainfo.xwbo.xwbo.service.XwGroupService;
import com.asiainfo.xwbo.xwbo.service.XwUserService;
import com.asiainfo.xwbo.xwbo.system.XwGroupHandleStateInfoLoader;
import com.asiainfo.xwbo.xwbo.system.XwGroupManagementStateInfoLoader;
import com.asiainfo.xwbo.xwbo.system.XwIndustryClassInfoLoader;
import com.asiainfo.xwbo.xwbo.system.constants.Constant;
import com.asiainfo.xwbo.xwbo.utils.DistanceUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private XwUserService xwUserService;

    @Resource
    private ICommonExtDao commonExtDao;

    @Resource
    private XwIndustryClassInfoLoader xwIndustryClassInfoLoader;

    @Resource
    private XwGroupHandleStateInfoLoader xwGroupHandleStateInfoLoader;

    @Resource
    private XwGroupManagementStateInfoLoader xwGroupManagementStateInfoLoader;

    @Resource
    private XwGroupInfoDao xwGroupInfoDao;


    @Override
    public PageResultVo qryAll(QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo) throws Exception {
        PageResultVo pageResultVo = new PageResultVo();
        XwGroupInfoQryAllInner inner = new XwGroupInfoQryAllInner(qryPeripheryXwGroupInfoSo).invoke();
        List<XwGroupInfoPo> xwGroupInfoPoList = inner.getXwGroupInfoPoList();
        long total = inner.getTotal();
        List<XwGroupInfoVo> xwGroupInfoVoList = inner.getXwGroupInfoVoList();
        if(StringUtils.isBlank(qryPeripheryXwGroupInfoSo.getCurrentLng()) || StringUtils.isBlank(qryPeripheryXwGroupInfoSo.getCurrentLat())) {
            xwGroupInfoPoList.forEach(po -> {
                XwGroupInfoVo xwGroupInfoVo = xwGroupInfoPoToVo(po, true);
                xwGroupInfoVoList.add(xwGroupInfoVo);
            });
            pageResultVo.setList(xwGroupInfoVoList);
        }else  {
            xwGroupInfoPoList.forEach(po -> {
                XwGroupInfoVo xwGroupInfoVo = xwGroupInfoPoToVo(po, true);
                Long distance = DistanceUtil.getDistance(qryPeripheryXwGroupInfoSo.getCurrentLng(), qryPeripheryXwGroupInfoSo.getCurrentLat(), po.getLng(), po.getLat());
                xwGroupInfoVo.setDistance(distance);
                xwGroupInfoVoList.add(xwGroupInfoVo);
            });
            pageResultVo.setList(
                    xwGroupInfoVoList.stream().filter(po -> po.getDistance() != null).sorted(Comparator.comparing(XwGroupInfoVo::getDistance)).collect(Collectors.toList())
            );

        }
        pageResultVo.setTotal(total);


        return pageResultVo;

    }

    @Override
    public String qryAllPosition(QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo) throws Exception {
        long time1 = new Date().getTime();
        PageResultVo pageResultVo = new PageResultVo();
        XwGroupInfoQryAllInner inner = new XwGroupInfoQryAllInner(qryPeripheryXwGroupInfoSo).invoke();
        List<XwGroupInfoPo> xwGroupInfoPoList = inner.getXwGroupInfoPoList();
        long total = inner.getTotal();
        long time2 = new Date().getTime();
        System.out.println("time2 - time1： "+(time2 - time1));
//        List<XwGroupInfoVo> xwGroupInfoVoList = inner.getXwGroupInfoVoList();
        StringBuffer stringBuffer = new StringBuffer();
        byte[] bytes = {0x01};
        String tesult = new String(bytes);
        for(XwGroupInfoPo po : xwGroupInfoPoList) {
            if(StringUtils.isBlank(po.getLng()) || StringUtils.isBlank(po.getLat())) {
                continue;
            }
            stringBuffer.append(po.getId()).append(",");
            stringBuffer.append(po.getLng()).append(",");
            stringBuffer.append(po.getLat()).append(",");
            stringBuffer.append(po.getFirstClass() == null? "": po.getFirstClass()).append(",");
            stringBuffer.append(po.getZaiwang() == null? "":po.getZaiwang()).append(",");
            stringBuffer.append(po.getHandleState()==null?"":po.getHandleState()).append(",");
            stringBuffer.append(po.getName()==null?"":po.getName()).append(tesult);

        }
        if(stringBuffer.length() >0 ){
            return  stringBuffer.substring(0, stringBuffer.length() - 1);
        }


        return stringBuffer.toString();
    }

    @Override
    public XwGroupInfoVo qryInfo(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        Long groupId = qryXwGroupInfoSo.getGroupId();
        XwGroupInfoPo xwGroupInfoPo = qryGroupInfoPo(groupId);
        XwGroupInfoVo xwGroupInfoVo = xwGroupInfoPoToVo(xwGroupInfoPo, true);
        if(StringUtils.isNotBlank(qryXwGroupInfoSo.getCurrentLng()) && StringUtils.isNotBlank(qryXwGroupInfoSo.getCurrentLat())) {
            Long distance = DistanceUtil.getDistance(qryXwGroupInfoSo.getCurrentLng(), qryXwGroupInfoSo.getCurrentLat(), xwGroupInfoPo.getLng(), xwGroupInfoPo.getLat());
            xwGroupInfoVo.setDistance(distance);
        }

        return xwGroupInfoVo;
    }

    @Override
    public List<XwGroupInfoVo> qryInfoList(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        List<XwGroupInfoVo> xwGroupInfoVoList = new ArrayList<>();
        List<Long> groupIdList = qryXwGroupInfoSo.getGroupList();
        if(null != groupIdList && groupIdList.size() > 0) {
            List<XwGroupInfoPo> xwGroupInfoPoList = commonExtDao.query(SqlBuilder.build(XwGroupInfoPo.class).in("id", qryXwGroupInfoSo.getGroupList()));
            if(StringUtils.isBlank(qryXwGroupInfoSo.getCurrentLng()) || StringUtils.isBlank(qryXwGroupInfoSo.getCurrentLat())) {
                xwGroupInfoPoList.forEach(po -> {
                    XwGroupInfoVo xwGroupInfoVo = xwGroupInfoPoToVo(po, false);
                    xwGroupInfoVoList.add(xwGroupInfoVo);
                });
            }else  {
                xwGroupInfoPoList.forEach(po -> {
                    XwGroupInfoVo xwGroupInfoVo = xwGroupInfoPoToVo(po, false);
                    Long distance = DistanceUtil.getDistance(qryXwGroupInfoSo.getCurrentLng(), qryXwGroupInfoSo.getCurrentLat(), po.getLng(), po.getLat());
                    xwGroupInfoVo.setDistance(distance);
                    xwGroupInfoVoList.add(xwGroupInfoVo);
                });

            }
        }
        return xwGroupInfoVoList;
    }

    @Override
    public PageResultVo qryMember(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        PageResultVo pageResultVo = new PageResultVo();
        List<XwGroupMemberInfoVo> xwGroupMemberInfoVoList = new ArrayList<>();
        Long groupId = qryXwGroupInfoSo.getGroupId();
        if(null == groupId) {
            throw new Exception("未传集团信息");
        }
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
            xwGroupInfoPo.setHandleState(syncXwGroupInfoSo.getHandleState());
            xwGroupInfoPo.setCreator(syncXwGroupInfoSo.getUserId());
            xwGroupInfoPo.setCreateTime(new Date());
            groupId = commonExtDao.saveReturnKey(SqlBuilder.build(XwGroupInfoPo.class), xwGroupInfoPo);

            syncXwGroupInfoSo.setId(groupId);

        }else {
            //修改
            xwGroupInfoPo = qryGroupInfoPo(groupId);
            syncXwGroupInfoSoToPo(xwGroupInfoPo, syncXwGroupInfoSo);
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
    public void changeHandleState(UpdateHandleStateSo updateHandleStateSo) throws Exception {
        Long groupId = updateHandleStateSo.getGroupId();
        XwGroupInfoPo xwGroupInfoPo = qryGroupInfoPo(groupId);
        String userId = updateHandleStateSo.getUserId();
        Integer newState = updateHandleStateSo.getHandleState();

        //本人的可以随时修改
        //非本人只有待排摸和最后处理人为空时才能修改
        Integer oldHandleState = xwGroupInfoPo.getHandleState();
        String lastHandleUser = xwGroupInfoPo.getLastHandleUser();
        Date nowDate = new Date();
        if(Constant.XW_GROUP_HANDLE_STATE.DAIPAIMO.equals(oldHandleState) && StringUtils.isBlank(lastHandleUser)) {
            //初始状态能修改
            //更新集团信息（乐观锁）

        }else {
            //已经有人处理了（本人或其他人）
            if(StringUtils.isBlank(lastHandleUser)) {
                throw new Exception("集团ID：" + groupId + " 存在问题");
            }
            if(lastHandleUser.equals(userId)) {
                //本人
            }else {
                throw new Exception("集团ID：" + groupId + " 已被处理");
            }
        }
        xwGroupInfoPo.setLastHandleUser(userId);
        xwGroupInfoPo.setLastHandleTime(nowDate);
        xwGroupInfoPo.setLastUpdator(userId);
        xwGroupInfoPo.setLastUpdateTime(nowDate);
        xwGroupInfoPo.setNewHandleState(newState);
        System.out.println(JSONObject.toJSONString(xwGroupInfoPo));
        int updateRow = xwGroupInfoDao.updateHandleUser(xwGroupInfoPo);
        if(updateRow != 1) {
            throw new Exception("集团ID: " + groupId + " 已处理");
        }
    }

    @Override
    public XwGroupInfoPo qryGroupInfoPo(Long groupId) throws Exception {
        XwGroupInfoPo xwGroupInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwGroupInfoPo.class).eq("id", groupId));
        if(null == xwGroupInfoPo) {
            throw new Exception("无该集团信息");
        }
        return xwGroupInfoPo;
    }

    @Override
    public List<XwGroupProductInfoSo> qryProductInfo(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {

        return xwGroupDao.qryProductInfo(qryXwGroupInfoSo);
    }

    @Override
    public List<XwGroupCaseItemInfoSo> qryCaseItemInfo(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {

        return xwGroupDao.qryCaseItemInfo(qryXwGroupInfoSo);
    }

    @Override
    public List<XwIndustryClassInfoVo> industryClass() throws Exception {
        return xwIndustryClassInfoLoader.getHeaderList();
    }

    @Override
    public List<XwGroupManagementStateInfoVo> managementState() {

        return xwGroupManagementStateInfoLoader.getList();
    }

    @Override
    public List<XwGroupHandleStateInfoVo> handleState() {
        return xwGroupHandleStateInfoLoader.getList();
    }

    @Override
    public void delete(QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        Long id = qryXwGroupInfoSo.getGroupId();
        if(null == id) {
            throw new Exception("集团id不能为空");
        }
        commonExtDao.delete(SqlBuilder.build(XwGroupInfoPo.class).eq("id", id));
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
        xwGroupInfoPo.setRoomNo(syncXwGroupInfoSo.getRoomNo());
        xwGroupInfoPo.setProvId(syncXwGroupInfoSo.getProvId());
        xwGroupInfoPo.setCityId(syncXwGroupInfoSo.getCityId());
        xwGroupInfoPo.setCountyId(syncXwGroupInfoSo.getCountyId());
        xwGroupInfoPo.setGridId(syncXwGroupInfoSo.getGridId());
        xwGroupInfoPo.setMicroId(syncXwGroupInfoSo.getMicroId());
        xwGroupInfoPo.setFirstClass(syncXwGroupInfoSo.getFirstClass());
        xwGroupInfoPo.setSecondClass(syncXwGroupInfoSo.getSecondClass());
        xwGroupInfoPo.setManagementState(syncXwGroupInfoSo.getManagementState());
        xwGroupInfoPo.setCreditCode(syncXwGroupInfoSo.getCreditCode());
        xwGroupInfoPo.setRelationGroupId(syncXwGroupInfoSo.getRelationGroupId());
        xwGroupInfoPo.setPhone(syncXwGroupInfoSo.getPhone());
        xwGroupInfoPo.setLng(syncXwGroupInfoSo.getLng());
        xwGroupInfoPo.setLat(syncXwGroupInfoSo.getLat());
        if(Constant.XW_GROUP_HANDLE_STATE.YIPAIMO == syncXwGroupInfoSo.getHandleState() && StringUtils.isNotBlank(syncXwGroupInfoSo.getRelationGroupId())) {
            xwGroupInfoPo.setZaiwang(Constant.XW_GROUP_ZAIWANG_STATE.ZAIWANG);

        }else {
            xwGroupInfoPo.setZaiwang(Constant.XW_GROUP_ZAIWANG_STATE.FEIZAIWANG);
        }
        return xwGroupInfoPo;
    }


    private XwGroupInfoVo xwGroupInfoPoToVo(XwGroupInfoPo xwGroupInfoPo, boolean flag) {

        String lng = xwGroupInfoPo.getLng();
        String lat = xwGroupInfoPo.getLat();
        XwGroupInfoVo xwGroupInfoVo = XwGroupInfoVo.builder()
                .id(xwGroupInfoPo.getId())
                .name(xwGroupInfoPo.getName())
                .creditCode(xwGroupInfoPo.getCreditCode())
                .address(xwGroupInfoPo.getAddress())
                .roomNo(xwGroupInfoPo.getRoomNo())
                .provId(xwGroupInfoPo.getProvId())
                .cityId(xwGroupInfoPo.getCityId())
                .countyId(xwGroupInfoPo.getCountyId())
                .gridId(xwGroupInfoPo.getGridId())
                .microId(xwGroupInfoPo.getMicroId())
                .firstClass(xwGroupInfoPo.getFirstClass())
                .firstClassName(xwIndustryClassInfoLoader.get(xwGroupInfoPo.getFirstClass())==null? null:xwIndustryClassInfoLoader.get(xwGroupInfoPo.getFirstClass()).getName())
                .secondClass(xwGroupInfoPo.getSecondClass())
                .secondClassName(xwIndustryClassInfoLoader.get(xwGroupInfoPo.getSecondClass())==null? null: xwIndustryClassInfoLoader.get(xwGroupInfoPo.getSecondClass()).getName())
                .phone(xwGroupInfoPo.getPhone())
                .managementState(xwGroupInfoPo.getManagementState())
                .managementStateMessage(xwGroupManagementStateInfoLoader.get(xwGroupInfoPo.getManagementState()) ==null? null: xwGroupManagementStateInfoLoader.get(xwGroupInfoPo.getManagementState()).getMessage())
                .handleState(xwGroupInfoPo.getHandleState())
                .handleStateMessage(xwGroupHandleStateInfoLoader.get(xwGroupInfoPo.getHandleState()) ==null? null: xwGroupHandleStateInfoLoader.get(xwGroupInfoPo.getHandleState()).getMessage())
                .zaiwang(xwGroupInfoPo.getZaiwang())
                .zaiwangMessage(Constant.XW_GROUP_ZAIWANG_STATE.MAPPER.get(xwGroupInfoPo.getZaiwang()))
                .relationGroupId(xwGroupInfoPo.getRelationGroupId())
                .lng(lng)
                .lat(lat)
                .lastHandleUser(xwGroupInfoPo.getLastHandleUser())
                .lastHandelTime(new DateTime(xwGroupInfoPo.getLastHandleTime()).toString("yyyy-MM-dd HH:mm:ss"))
                .createTime(new DateTime(xwGroupInfoPo.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss")).build();

        if(flag) {
            List<String> areaIdList = new ArrayList<>();
            areaIdList.add(xwGroupInfoPo.getProvId());
            areaIdList.add(xwGroupInfoPo.getCityId());
            areaIdList.add(xwGroupInfoPo.getCountyId());
            areaIdList.add(xwGroupInfoPo.getGridId());
            areaIdList.add(xwGroupInfoPo.getMicroId());
            List<XwAreaInfoPo> xwAreaInfoPoList = commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class).in("area_id", areaIdList));
            Map<String, String> areaInfoMap = new HashMap<>();
            areaInfoMap = xwAreaInfoPoList.stream().collect(Collectors.toMap(XwAreaInfoPo::getAreaId, po -> po.getAreaName()));
            xwGroupInfoVo.setProvName(areaInfoMap.getOrDefault(xwGroupInfoPo.getProvId(), "浙江省"));
            xwGroupInfoVo.setCityName(areaInfoMap.getOrDefault(xwGroupInfoPo.getCityId(), "未知"));
            xwGroupInfoVo.setCountyName(areaInfoMap.getOrDefault(xwGroupInfoPo.getCountyId(), "未知"));
            xwGroupInfoVo.setGridName(areaInfoMap.getOrDefault(xwGroupInfoPo.getGridId(), "未知"));
            xwGroupInfoVo.setMicroName(areaInfoMap.getOrDefault(xwGroupInfoPo.getMicroId(), "未知"));
        }
        if(StringUtils.isNotBlank(lng) && StringUtils.isNotBlank(lat)) {
            xwGroupInfoVo.setGaodeUrl(DistanceUtil.getGaodeUrl(lng, lat));
        }
        return xwGroupInfoVo;
    }

    private class XwGroupInfoQryAllInner {
        private QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo;
        private List<XwGroupInfoPo> xwGroupInfoPoList;
        private long total;
        private List<XwGroupInfoVo> xwGroupInfoVoList;

        public XwGroupInfoQryAllInner(QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo) {
            this.qryPeripheryXwGroupInfoSo = qryPeripheryXwGroupInfoSo;
        }

        public List<XwGroupInfoPo> getXwGroupInfoPoList() {
            return xwGroupInfoPoList;
        }

        public long getTotal() {
            return total;
        }

        public List<XwGroupInfoVo> getXwGroupInfoVoList() {
            return xwGroupInfoVoList;
        }

        public XwGroupInfoQryAllInner invoke() throws Exception {
            long time5 = new Date().getTime();
            String userId = qryPeripheryXwGroupInfoSo.getUserId();
            //人员信息
            XwUserInfo xwUserInfo = xwUserService.qryInfo(qryPeripheryXwGroupInfoSo);
            Integer roleId = xwUserInfo.getRoleId();
            String pageNum = qryPeripheryXwGroupInfoSo.getPageNum();
            String pageSize = qryPeripheryXwGroupInfoSo.getPageSize();
            if(roleId.intValue() == Constant.XW_USER_ROLE.ZHIXIAORENYUAN.intValue()) {
                //直销人员 （先看传过来的microIdList是否有值 有就用这个查， 没有通过user的归属查）
                String microIds = xwUserInfo.getMicroIds();
                if(StringUtils.isBlank(microIds)) {
                    throw new Exception("该用户没有所属微格");
                }
                List<String> microIdList  = Arrays.asList(microIds.split(","));
                List<String> qryMicroIdList = qryPeripheryXwGroupInfoSo.getMicroIdList();
                if(null == qryMicroIdList || qryMicroIdList.size() == 0) {
                    //根据用户的归属微格来查
                    qryPeripheryXwGroupInfoSo.setMicroIdList(microIdList);
                }else {
                    //
                    for(String microId : microIdList) {
                        if(!microIdList.contains(microId)) {
                            throw new Exception("该用户没有查询条件中的微格归属");
                        }
                    }
                }
            }else  {

                String qryAreaId = qryPeripheryXwGroupInfoSo.getAreaId();
                Integer qryAreaLevel = qryPeripheryXwGroupInfoSo.getAreaLevel();
                if(StringUtils.isBlank(qryAreaId) || null == qryAreaLevel) {
                    //根据用户的信息来查
                    if(roleId.intValue() == Constant.XW_USER_ROLE.WANGGEZHANG.intValue()) {
                        //网格长
                        String gridId = xwUserInfo.getGridId();
                        qryPeripheryXwGroupInfoSo.setAreaId(gridId);
                        qryPeripheryXwGroupInfoSo.setAreaLevel(Constant.XW_AREA_LEVEL.GRID);
                    }else {
                        //负责人 或者网格督导
                        String areaCode = xwUserInfo.getAreaCode();
                        Integer areaLevel = xwUserInfo.getAreaLevel();
                        qryPeripheryXwGroupInfoSo.setAreaId(areaCode);
                        qryPeripheryXwGroupInfoSo.setAreaLevel(areaLevel);
                    }

                }else {
                    qryPeripheryXwGroupInfoSo.setAreaId(qryAreaId);
                    qryPeripheryXwGroupInfoSo.setAreaLevel(qryAreaLevel);
                }


            }
            long time6 = new Date().getTime();
            System.out.println("time6 - time5： "+(time6 - time5));
            xwGroupInfoVoList = new ArrayList<>();
            if(StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
                Page pageInfo = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
                xwGroupInfoPoList = xwGroupDao.qryAll(qryPeripheryXwGroupInfoSo);
                total = pageInfo.getTotal();
            }else {
                xwGroupInfoPoList = xwGroupDao.qryAll(qryPeripheryXwGroupInfoSo);
                total = xwGroupInfoPoList.size();
            }
            long time7 = new Date().getTime();
            System.out.println("time6 - time5： "+(time7 - time6));
            return this;
        }
    }
}
