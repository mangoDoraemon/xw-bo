package com.asiainfo.xwbo.xwbo.service.impl;

import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.XwAreaInfo;
import com.asiainfo.xwbo.xwbo.model.po.XwAreaInfoPo;
import com.asiainfo.xwbo.xwbo.model.vo.XwAreaInfoVo;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaInfoSo;
import com.asiainfo.xwbo.xwbo.service.XwAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
    public XwAreaInfoVo qryInfo(QryAreaInfoSo qryAreaInfoSo) throws Exception {
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

    private XwAreaInfo xwAreaInfoPoToModel(XwAreaInfoPo xwAreaInfoPo) {
        XwAreaInfo xwAreaInfo = new XwAreaInfo();
        xwAreaInfo.setArea_id(xwAreaInfoPo.getAreaId());
        xwAreaInfo.setArea_name(xwAreaInfoPo.getAreaName());
        xwAreaInfo.setArea_level(xwAreaInfoPo.getAreaLevel());
        xwAreaInfo.setArea_pid(xwAreaInfoPo.getAreaPid());
        xwAreaInfo.setArea_location(xwAreaInfoPo.getAreaLocation());
        xwAreaInfo.setCenter(xwAreaInfoPo.getCentralPoint());
        return xwAreaInfo;
    }
}