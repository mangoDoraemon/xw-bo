package com.asiainfo.xwbo.xwbo.service.impl;

import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.base.XwUserInfoDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.XwAreaInfo;
import com.asiainfo.xwbo.xwbo.model.XwUserInfo;
import com.asiainfo.xwbo.xwbo.model.po.XwAreaInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwUserInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwUserRoleInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwViewUserInfo;
import com.asiainfo.xwbo.xwbo.model.so.LoginSo;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.QrySubordinatesSo;
import com.asiainfo.xwbo.xwbo.model.so.XwUserInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.*;
import com.asiainfo.xwbo.xwbo.service.XwAreaService;
import com.asiainfo.xwbo.xwbo.service.XwUserService;
import com.asiainfo.xwbo.xwbo.system.constants.Constant;
import com.asiainfo.xwbo.xwbo.utils.AESXnwTools;
import com.asiainfo.xwbo.xwbo.utils.UserUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jiahao jin
 * @create 2020-05-11 10:57
 */
@Service
@Slf4j
@Transactional(value = "baseTransaction")
public class XwUserServiceImpl implements XwUserService {

    @Resource
    private ICommonExtDao commonExtDao;

    @Resource
    private XwAreaService xwAreaService;

    @Resource
    private XwUserInfoDao xwUserInfoDao;

    @Override
    public XwAreaInfoVo areaInfo(XwUserInfoSo xwUserInfoSo) throws Exception {


        XwUserInfo xwUserInfo = qryInfo(xwUserInfoSo);

//        String microIds = xwUserInfo.getMicroIds();
//        if(StringUtils.isBlank(microIds)) {
//            throw new Exception("该人员无权限查看");
//        }
        Integer roleId = xwUserInfo.getRoleId();
        String areaId = "";
        if(roleId.intValue() == Constant.XW_USER_ROLE.ZHIXIAORENYUAN.intValue() || roleId.intValue() == Constant.XW_USER_ROLE.WANGGEZHANG.intValue()){
            //直销人员
            areaId = xwUserInfo.getGridId();
        }else if (roleId.intValue() == Constant.XW_USER_ROLE.SUPERINTENDENT.intValue() || roleId.intValue() == Constant.XW_USER_ROLE.WANGGEDUDAO.intValue()) {
            //负责人 或者网格督导
            areaId = xwUserInfo.getAreaCode();

        }
        if(StringUtils.isBlank(areaId)) {
            throw new Exception("该人员无归属");
        }
        QryAreaInfoSo qryAreaInfoSo = new QryAreaInfoSo();
        qryAreaInfoSo.setAreaId(areaId);
        return xwAreaService.qryAreaInfo(qryAreaInfoSo);
    }

    @Override
    public XwUserInfo checkUserId(XwUserInfoSo xwUserInfoSo) throws Exception {
        String userId = AESXnwTools.getInstance().decrypt( xwUserInfoSo.getUserId() );
        if(StringUtils.isBlank(userId)) {
            throw new Exception("用户验证失败");
        }
        xwUserInfoSo.setUserId(userId);
        return qryInfo(xwUserInfoSo);
    }

    @Override
    public boolean checkToken(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)) {
            throw new Exception("无token");
        }

        return UserUtil.isOverdue(token);
    }

    @Override
    public XwMicroInfoVo microInfo(XwUserInfoSo xwUserInfoSo) throws Exception {
        XwUserInfo xwUserInfo = qryInfo(xwUserInfoSo);
        String microIds = xwUserInfo.getMicroIds();
        if(StringUtils.isBlank(microIds)) {
            throw new Exception("该人员无所属微格");
        }
        List<String> microIdList = Arrays.asList(microIds.split(","));
        QryAreaInfoSo qryAreaInfoSo = new QryAreaInfoSo();
        qryAreaInfoSo.setMircoIdList(microIdList);
        return xwAreaService.qryMicroInfo(qryAreaInfoSo);
    }

    @Override
    public XwUserInfo login(LoginSo loginSo) throws Exception {
        XwUserInfoPo xwUserInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwUserInfoPo.class).eq("user_code", loginSo.getUserCode()));
        if(null == xwUserInfoPo) {
            throw new Exception("无该人员信息");
        }
        if(!UserUtil.encryption(loginSo.getPassword()).equals(xwUserInfoPo.getUserPassword())) {
            if(loginSo.getPassword().equals(xwUserInfoPo.getUserPassword())) {

            }else {
                throw new Exception("密码错误");
            }

        }
        XwUserInfo xwUserInfo = xwUserInfoPoToModel(xwUserInfoPo);
        String token = UserUtil.sign(xwUserInfo);
        xwUserInfo.setToken(token);
        return xwUserInfo;
    }

    @Override
    public PageResultVo qrySubordinates(QrySubordinatesSo qrySubordinatesSo) throws Exception {
        PageResultVo pageResultVo = new PageResultVo();
        XwUserInfo xwUserInfo = qryInfo(qrySubordinatesSo);
        String qryAreaId = qrySubordinatesSo.getAreaId();
        Integer qryAreaLevel = qrySubordinatesSo.getAreaLevel();
        if(xwUserInfo.getRoleId() == Constant.XW_USER_ROLE.ZHIXIAORENYUAN) {
            throw new Exception("该人员无权限");
        }

        if(StringUtils.isBlank(qryAreaId) || null == qryAreaLevel) {
            if(xwUserInfo.getRoleId() == Constant.XW_USER_ROLE.WANGGEZHANG) {
                qrySubordinatesSo.setAreaId(xwUserInfo.getGridId());
                qrySubordinatesSo.setAreaLevel(Constant.XW_AREA_LEVEL.GRID);
            }else {
                qrySubordinatesSo.setAreaId(xwUserInfo.getAreaCode());
                qrySubordinatesSo.setAreaLevel(xwUserInfo.getAreaLevel());
            }
        }
        Integer qryRoleId = qrySubordinatesSo.getQryRoleId();
        List<Integer> roleIdList = new ArrayList<>();
        if(null == qryRoleId) {
//            qrySubordinatesSo.setRoleId(xwUserInfo.getRoleId());
            List<XwUserRoleInfoPo> xwUserRoleInfoPoList = commonExtDao.query(SqlBuilder.build(XwUserRoleInfoPo.class).eq("role_id", xwUserInfo.getRoleId()));
            xwUserRoleInfoPoList.forEach(po -> roleIdList.add(po.getSubRoleId()));
            qrySubordinatesSo.setRoleIdList(roleIdList);
        }else {
            roleIdList.add(qryRoleId);
            qrySubordinatesSo.setRoleIdList(roleIdList);
        }
        String pageNum = qrySubordinatesSo.getPageNum();
        String pageSize = qrySubordinatesSo.getPageSize();
        long total = 0L;
        List<XwUserInfoPo> xwUserInfoPoList = new ArrayList<>();
        if(StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
            Page pageInfo = PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
            xwUserInfoPoList = xwUserInfoDao.qrySubordinates(qrySubordinatesSo);
            total = pageInfo.getTotal();
        }else {
            xwUserInfoPoList = xwUserInfoDao.qrySubordinates(qrySubordinatesSo);
            total = xwUserInfoPoList.size();
        }
        List<XwUserInfo> xwUserInfoList = new ArrayList<>();

        xwUserInfoPoList.forEach(info -> {
            XwUserInfo subXwUserInfo = xwUserInfoPoToModel(info);

            xwUserInfoList.add(subXwUserInfo);
        });
        pageResultVo.setList(xwUserInfoList);
        pageResultVo.setTotal(total);
        return pageResultVo;
    }

    @Override
    public PageResultVo qrySubordinatesWorkDetail(QrySubordinatesSo qrySubordinatesSo) throws Exception {
        PageResultVo pageResultVo = qrySubordinates(qrySubordinatesSo);
        DecimalFormat df = new DecimalFormat("0.00%");
        pageResultVo.getList().forEach( vo -> {
            XwUserInfo  subXwUserInfo = (XwUserInfo) vo;
            Map<String, String> kpiValue = new HashMap<>();
            XwViewUserInfo xwViewUserInfo = null;
            xwViewUserInfo = commonExtDao.queryForObject(SqlBuilder.build(XwViewUserInfo.class).eq("user_id", subXwUserInfo.getUserId()));
            if(null == xwViewUserInfo) {
                kpiValue.put("message1", "0");
                kpiValue.put("message2", "0");
                kpiValue.put("message3", "0");
                kpiValue.put("message4", "0");
            }else {
                kpiValue.put("message1", String.valueOf(xwViewUserInfo.getSevenNum()==null? 0: xwViewUserInfo.getSevenNum()));
                kpiValue.put("message2", "0");
                kpiValue.put("message3", String.valueOf(xwViewUserInfo.getJobInNum()==null? 0: xwViewUserInfo.getJobInNum()));
                kpiValue.put("message4", String.valueOf(xwViewUserInfo.getRate()==null? 0:  df.format(xwViewUserInfo.getRate())));
            }

            subXwUserInfo.setKpiValue(kpiValue);
        });
        return pageResultVo;
    }

    @Override
    public List qrySubordinatesRoleList(XwUserInfoSo xwUserInfoSo) throws Exception {
        XwUserInfo xwUserInfo = qryInfo(xwUserInfoSo);
        Integer roleId = xwUserInfo.getRoleId();

        return Constant.XW_USER_ROLE.ROLE_MAPPER.get(roleId);
    }

    @Override
    public String getAes(String userId) throws Exception {
        return AESXnwTools.getInstance().encrypt( userId );
    }

    @Override
    public XwUserInfo qryInfo(XwUserInfoSo xwUserInfoSo) throws Exception{
        XwUserInfoPo xwUserInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwUserInfoPo.class).eq("user_id", xwUserInfoSo.getUserId()));
        if(null == xwUserInfoPo) {
            throw new Exception("无该人员信息");
        }
        return xwUserInfoPoToModel(xwUserInfoPo);
    }

    private XwUserInfo xwUserInfoPoToModel(XwUserInfoPo xwUserInfoPo) {
        XwUserInfo xwUserInfo = new XwUserInfo();
        xwUserInfo.setUserId(xwUserInfoPo.getUserId());
        xwUserInfo.setUserCode(xwUserInfoPo.getUserCode());
        xwUserInfo.setUserName(xwUserInfoPo.getUserName());
        xwUserInfo.setMobilePhone(xwUserInfoPo.getMobilePhone());
        xwUserInfo.setEmailAddress(xwUserInfoPo.getEmailAddress());
        xwUserInfo.setOaCode(xwUserInfoPo.getOaCode());
        xwUserInfo.setRoleId(xwUserInfoPo.getRoleId());
        xwUserInfo.setRole(Constant.XW_USER_ROLE.MAPPER.get(xwUserInfoPo.getRoleId()));
        xwUserInfo.setAreaCode(xwUserInfoPo.getAreaCode());
        xwUserInfo.setAreaLevel(xwUserInfoPo.getAreaLevel());
        xwUserInfo.setProvId(xwUserInfoPo.getProvId());
        xwUserInfo.setCityId(xwUserInfoPo.getCityId());
        xwUserInfo.setCountyId(xwUserInfoPo.getCountyId());
        xwUserInfo.setGridId(xwUserInfoPo.getGridId());
        xwUserInfo.setMicroIds(xwUserInfoPo.getMicroId());
        List<String> microIdList = new ArrayList<>();
        if(StringUtils.isNotBlank(xwUserInfoPo.getMicroId())) {
            microIdList = Arrays.asList(xwUserInfoPo.getMicroId().split(","));
        }

        List<String> areaIdList = new ArrayList<>(microIdList);
        areaIdList.add(xwUserInfoPo.getProvId());
        areaIdList.add(xwUserInfoPo.getCityId());
        areaIdList.add(xwUserInfoPo.getCountyId());
        areaIdList.add(xwUserInfoPo.getGridId());
        areaIdList.add(xwUserInfoPo.getMicroId());
        List<XwAreaInfoPo> xwAreaInfoPoList = commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class).in("area_id", areaIdList));
        Map<String, String> areaInfoMap = new HashMap<>();
        areaInfoMap = xwAreaInfoPoList.stream().collect(Collectors.toMap(XwAreaInfoPo::getAreaId, po -> po.getAreaName()));
        xwUserInfo.setProvName(areaInfoMap.getOrDefault(xwUserInfoPo.getProvId(), "浙江省"));
        xwUserInfo.setCityName(areaInfoMap.getOrDefault(xwUserInfoPo.getCityId(), "未知"));
        xwUserInfo.setCountyName(areaInfoMap.getOrDefault(xwUserInfoPo.getCountyId(), "未知"));
        xwUserInfo.setGridName(areaInfoMap.getOrDefault(xwUserInfoPo.getGridId(), "未知"));
//        xwUserInfo.setMicroName(areaInfoMap.getOrDefault(xwUserInfoPo.getMicroId(), "未知"));
        List<XwAreaInfo> microreaInfoList = new ArrayList<>();
        Map<String, String> finalAreaInfoMap = areaInfoMap;

        microIdList.stream().forEach(id -> {
            XwAreaInfo xwAreaInfo = new XwAreaInfo();
            xwAreaInfo.setAreaId(id);
            xwAreaInfo.setAreaName(finalAreaInfoMap.getOrDefault(id, "未知"));
            microreaInfoList.add(xwAreaInfo);
        });
        xwUserInfo.setMicroIdList(microreaInfoList);
        return xwUserInfo;

    }

}
