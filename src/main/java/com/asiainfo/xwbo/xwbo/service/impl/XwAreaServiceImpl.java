package com.asiainfo.xwbo.xwbo.service.impl;

import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.XwAreaInfo;
import com.asiainfo.xwbo.xwbo.model.po.XwAreaInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.XwAreaInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwIndustryClassInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwMicroInfoVo;
import com.asiainfo.xwbo.xwbo.service.XwAreaService;
import com.asiainfo.xwbo.xwbo.system.constants.Constant;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.stream.Collectors;

/**
 * @author jiahao jin
 * @create 2020-05-09 12:04
 */
@Service
@Slf4j
@Transactional(value = "baseTransaction")
public class XwAreaServiceImpl implements XwAreaService {

    @Resource
    private ICommonExtDao commonExtDao;

    @Override
    public XwAreaInfoVo qryAreaInfo(QryAreaInfoSo qryAreaInfoSo) throws Exception {
        XwAreaInfoVo xwAreaInfoVo = new XwAreaInfoVo();
        XwAreaInfoPo  parentXwAreaInfoPo = commonExtDao.queryForObject(SqlBuilder.build(XwAreaInfoPo.class).eq("area_id", qryAreaInfoSo.getAreaId()));
        if(null == parentXwAreaInfoPo) {
            throw new Exception("无该地区信息");
        }
        String path = parentXwAreaInfoPo.getAreaName();
        xwAreaInfoVo.setPath(path);
        XwAreaInfo parentXwAreaInfo = xwAreaInfoPoToModel(parentXwAreaInfoPo);

        List<XwAreaInfo> childXeAreaInfoList = new ArrayList<>();
        List<XwAreaInfoPo> childXwAreaInfoPoList = commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class).eq("area_pid", qryAreaInfoSo.getAreaId()));
        if(null != childXwAreaInfoPoList && childXwAreaInfoPoList.size() > 0) {
            childXeAreaInfoList = childXwAreaInfoPoList.stream().map(po -> xwAreaInfoPoToModel(po)).collect(Collectors.toList());

        }
        parentXwAreaInfo.setChild(childXeAreaInfoList);
        xwAreaInfoVo.setMap(parentXwAreaInfo);
        return xwAreaInfoVo;
    }

    @Override
    public List<XwAreaInfo> qryCascadeAreaInfo(QryAreaInfoSo qryAreaInfoSo) throws Exception {
        Map<String, XwAreaInfo> keyMap = new HashMap<>();
        List<XwAreaInfo> xwAreaInfoVoList = new ArrayList<>();
        List<Integer> arealevelList = new ArrayList<>();
        arealevelList.add(Constant.XW_AREA_LEVEL.PROV);
        arealevelList.add(Constant.XW_AREA_LEVEL.CITY);
        arealevelList.add(Constant.XW_AREA_LEVEL.COUNTY);
        arealevelList.add(Constant.XW_AREA_LEVEL.GRID);
        arealevelList.add(Constant.XW_AREA_LEVEL.MICRO);
        List<XwAreaInfoPo>  xwAreaInfoPoList = commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class)
                .in("area_level", arealevelList)
                .setOrderBy("area_level"));
        if(null != xwAreaInfoPoList && xwAreaInfoPoList.size() > 0) {
            for(XwAreaInfoPo po : xwAreaInfoPoList) {
                String areaId = po.getAreaId();
                XwAreaInfo vo = new XwAreaInfo();
                vo.setAreaId(areaId);
                vo.setAreaName(po.getAreaName());
                vo.setAreaLevel(po.getAreaLevel());
                keyMap.put(areaId, vo);
                if(qryAreaInfoSo.getAreaId().equals(areaId)) {
                    xwAreaInfoVoList.add(vo);
                }else {
                    String pid = po.getAreaPid();
                    XwAreaInfo pVo = keyMap.get(pid);
                    if(null != pVo) {
                        if(null == pVo.getChild()) {
                            pVo.setChild(new ArrayList<>());

                        }
                        List<XwAreaInfo> child = pVo.getChild();
                        child.add(vo);
                    }
                }
            }
        }

        return xwAreaInfoVoList;
    }

    @Override
    public XwMicroInfoVo qryMicroInfo(QryAreaInfoSo qryAreaInfoSo) throws Exception {
        XwMicroInfoVo xwMicroInfoVo = new XwMicroInfoVo();
        List<XwAreaInfoPo>  parentXwAreaInfoPoList = commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class).in("area_id", qryAreaInfoSo.getMircoIdList()));
        List<XwAreaInfo> areaInfoList = parentXwAreaInfoPoList.stream().map(po -> xwAreaInfoPoToModel(po)).collect(Collectors.toList());
        xwMicroInfoVo.setXwMicroInfoList(areaInfoList);
        return xwMicroInfoVo;
    }

    private XwAreaInfo xwAreaInfoPoToModel(XwAreaInfoPo xwAreaInfoPo) {
        XwAreaInfo xwAreaInfo = new XwAreaInfo();
        xwAreaInfo.setAreaId(xwAreaInfoPo.getAreaId());
        xwAreaInfo.setAreaName(xwAreaInfoPo.getAreaName());
        xwAreaInfo.setAreaLevel(xwAreaInfoPo.getAreaLevel());
        xwAreaInfo.setAreaPid(xwAreaInfoPo.getAreaPid());
        xwAreaInfo.setAreaLocation(xwAreaInfoPo.getAreaLocation());
        xwAreaInfo.setCenter(xwAreaInfoPo.getCentralPoint());
        return xwAreaInfo;
    }
}