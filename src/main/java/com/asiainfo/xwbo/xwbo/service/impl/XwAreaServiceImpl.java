package com.asiainfo.xwbo.xwbo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.XwAreaInfo;
import com.asiainfo.xwbo.xwbo.model.po.XwAreaInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwViewGroupNumPo;
import com.asiainfo.xwbo.xwbo.model.po.XwWegMopaiRatePo;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaHandleInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.QryAreaHandleInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwAreaInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwMicroInfoVo;
import com.asiainfo.xwbo.xwbo.service.XwAreaService;
import com.asiainfo.xwbo.xwbo.system.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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
        Set<String> areaIdSet = new HashSet<>();
        if(qryAreaInfoSo.getAreaLevel().intValue() <= Constant.XW_AREA_LEVEL.COUNTY.intValue()) {
            List<XwAreaInfoPo> childXwAreaInfoPoList = commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class).eq("area_pid", qryAreaInfoSo.getAreaId()));
            if(null != childXwAreaInfoPoList && childXwAreaInfoPoList.size() > 0) {
                for(XwAreaInfoPo xwAreaInfoPo : childXwAreaInfoPoList) {
                    areaIdSet.add(xwAreaInfoPo.getAreaId());
                    childXeAreaInfoList.add(xwAreaInfoPoToModel(xwAreaInfoPo));
                }

            }
        }else if(Constant.XW_AREA_LEVEL.GRID.equals(qryAreaInfoSo.getAreaLevel())) {
            //微格信息
            List<XwAreaInfoPo> child72XwAreaInfoPoList =
                    commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class).eq("area_pid", qryAreaInfoSo.getAreaId()).eq("area_level", Constant.XW_AREA_LEVEL.MICRO));
            if(null != child72XwAreaInfoPoList && child72XwAreaInfoPoList.size() > 0) {
                for(XwAreaInfoPo po72 : child72XwAreaInfoPoList) {
                    //根据微格查所有小区 area_level = 7
                    areaIdSet.add(po72.getAreaId());
                    List<XwAreaInfoPo> child7XwAreaInfoPoList =
                            commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class).eq("area_pid72", po72.getAreaId()));
                    if(null != child7XwAreaInfoPoList && child7XwAreaInfoPoList.size() > 0) {
                        for(XwAreaInfoPo po7 : child7XwAreaInfoPoList) {
                            XwAreaInfo xwAreaInfo7 = xwAreaInfoPoToModel(po7);
                            xwAreaInfo7.setAreaId(po7.getAreaId());
                            xwAreaInfo7.setMicroId(po72.getAreaId());
                            xwAreaInfo7.setAreaPid(qryAreaInfoSo.getAreaId());
                            childXeAreaInfoList.add(xwAreaInfo7);
                        }
                    }
                }
            }
        }else if(Constant.XW_AREA_LEVEL.MICRO.equals(qryAreaInfoSo.getAreaLevel())) {
            List<XwAreaInfoPo> child7XwAreaInfoPoList =
                    commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class).eq("area_pid72", qryAreaInfoSo.getAreaId()));
            if(null != child7XwAreaInfoPoList && child7XwAreaInfoPoList.size() > 0) {
                for(XwAreaInfoPo po7 : child7XwAreaInfoPoList) {
                    XwAreaInfo xwAreaInfo7 = xwAreaInfoPoToModel(po7);
                    xwAreaInfo7.setAreaPid(po7.getAreaId());
                    xwAreaInfo7.setMicroId(qryAreaInfoSo.getAreaId());
                    childXeAreaInfoList.add(xwAreaInfo7);
                }
            }
        }
        Map<String, XwWegMopaiRatePo> areaRateMap = getXwAreaRate(areaIdSet);
        for(XwAreaInfo childXwAreaInfo : childXeAreaInfoList) {
            XwWegMopaiRatePo xwWegMopaiRatePo = areaRateMap.get(childXwAreaInfo.getAreaId());
            if(xwWegMopaiRatePo==null){
                childXwAreaInfo.setRate(0D);
                childXwAreaInfo.setSort(11);
            }else{
                childXwAreaInfo.setRate(xwWegMopaiRatePo.getRate()==null? 0D:xwWegMopaiRatePo.getRate());
                childXwAreaInfo.setSort(xwWegMopaiRatePo.getSort());
            }
//            System.out.println(xwWegMopaiRatePo);
//            childXwAreaInfo.setRate(xwWegMopaiRatePo==null? 0D:xwWegMopaiRatePo.getRate());
//            childXwAreaInfo.setSort(xwWegMopaiRatePo==null? 11:xwWegMopaiRatePo.getSort());


    }
        parentXwAreaInfo.setChild(childXeAreaInfoList);
        xwAreaInfoVo.setMap(parentXwAreaInfo);
        return xwAreaInfoVo;
    }

    @Override
    public Map<String, Double> getXwAreaRate(QryAreaInfoSo qryAreaInfoSo) {
        List<String>  areaIdList = qryAreaInfoSo.getAreaIdList();
        Map<String, Double> map = new HashMap<>();
        List<XwWegMopaiRatePo> xwWegMopaiRatePoList = commonExtDao.query(SqlBuilder.build(XwWegMopaiRatePo.class).in("area_id", areaIdList));
        if(null != xwWegMopaiRatePoList && xwWegMopaiRatePoList.size() > 0) {
            map = xwWegMopaiRatePoList.stream().collect(Collectors.toMap(XwWegMopaiRatePo :: getAreaId, po -> po.getRate()));
        }
        return map;
    }

    public Map<String, XwWegMopaiRatePo> getXwAreaRate(Set areaIdSet) {
        Map<String, XwWegMopaiRatePo> map = new HashMap<>();
        List<XwWegMopaiRatePo> xwWegMopaiRatePoList = commonExtDao.query(SqlBuilder.build(XwWegMopaiRatePo.class).in("area_id", areaIdSet).setOrder("rate", "desc"));
        if(null != xwWegMopaiRatePoList && xwWegMopaiRatePoList.size() > 0) {
//            map = xwWegMopaiRatePoList.stream().collect(Collectors.toMap(XwWegMopaiRatePo :: getAreaId, po -> po.getRate()));\
            if(null != xwWegMopaiRatePoList && xwWegMopaiRatePoList.size() > 0) {
                if(xwWegMopaiRatePoList.size() == 1) {
                    xwWegMopaiRatePoList.get(0).setSort(1);
                }else if(xwWegMopaiRatePoList.size() == 2) {
                    xwWegMopaiRatePoList.get(0).setSort(1);
                    xwWegMopaiRatePoList.get(1).setSort(11);
                }else {
                    xwWegMopaiRatePoList.get(0).setSort(1);
                    xwWegMopaiRatePoList.get(xwWegMopaiRatePoList.size() - 1).setSort(11);
                    int sub = 11/(xwWegMopaiRatePoList.size() - 1);
                    for(int i = 1; i < xwWegMopaiRatePoList.size()-1; i++) {
                        xwWegMopaiRatePoList.get(i).setSort(xwWegMopaiRatePoList.get(i-1).getSort() + sub);
                    }
                }

                map = xwWegMopaiRatePoList.stream().collect(Collectors.toMap(XwWegMopaiRatePo :: getAreaId, po -> po));
            }
        }
        return map;
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

    @Override
    public void updateCenter() throws Exception {
        List<XwAreaInfoPo> parentXwAreaInfoPoList = commonExtDao.query(SqlBuilder.build(XwAreaInfoPo.class));
        for(XwAreaInfoPo po : parentXwAreaInfoPoList) {
            if(StringUtils.isNotBlank(po.getAreaLocation())) {
                String[] arr = po.getAreaLocation().split(";");
                int total = arr.length;
                double X = 0, Y = 0, Z = 0;
                for(int i=0;i<arr.length;i++){
                    double lat, lon, x, y, z;
                    lon = Double.parseDouble(arr[i].split(",")[0]) * Math.PI / 180;
                    lat = Double.parseDouble(arr[i].split(",")[1]) * Math.PI / 180;
                    x = Math.cos(lat) * Math.cos(lon);
                    y = Math.cos(lat) * Math.sin(lon);
                    z = Math.sin(lat);
                    X += x;
                    Y += y;
                    Z += z;
                }

                X = X / total;
                Y = Y / total;
                Z = Z / total;
                double Lon = Math.atan2(Y, X);
                double Hyp = Math.sqrt(X * X + Y * Y);
                double Lat = Math.atan2(Z, Hyp);
                po.setCentralPoint(""+(Lon * 180 / Math.PI)+","+(Lat * 180 / Math.PI));
                commonExtDao.update(SqlBuilder.build(XwAreaInfoPo.class).eq("area_id", po.getAreaId()), po);
            }
        }
    }

    @Override
    public XwViewGroupNumPo qryAreaHandleInfo(QryAreaHandleInfoSo qryAreaHandleInfoSo) throws Exception {
        XwViewGroupNumPo po = commonExtDao.queryForObject(SqlBuilder.build(XwViewGroupNumPo.class).eq("area_id", qryAreaHandleInfoSo.getAreaId()));
        return po;
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