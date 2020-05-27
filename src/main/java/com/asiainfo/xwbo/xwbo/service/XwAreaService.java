package com.asiainfo.xwbo.xwbo.service;

import com.asiainfo.xwbo.xwbo.model.XwAreaInfo;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.XwAreaInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwMicroInfoVo;

import java.util.List;
import java.util.Map;

/**
 * @author jiahao jin
 * @create 2020-05-09 12:04
 */
public interface XwAreaService {
    XwAreaInfoVo qryAreaInfo(QryAreaInfoSo qryAreaInfoSo) throws Exception;

    Map<String, Double> getXwAreaRate(QryAreaInfoSo qryAreaInfoSo);

    List<XwAreaInfo> qryCascadeAreaInfo(QryAreaInfoSo qryAreaInfoSo) throws Exception;

    XwMicroInfoVo qryMicroInfo(QryAreaInfoSo qryAreaInfoSo) throws Exception;
}
