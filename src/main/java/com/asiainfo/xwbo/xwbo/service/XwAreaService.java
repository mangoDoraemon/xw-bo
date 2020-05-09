package com.asiainfo.xwbo.xwbo.service;

import com.asiainfo.xwbo.xwbo.model.vo.XwAreaInfoVo;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaInfoSo;

/**
 * @author jiahao jin
 * @create 2020-05-09 12:04
 */
public interface XwAreaService {
    XwAreaInfoVo qryInfo(QryAreaInfoSo qryAreaInfoSo) throws Exception;
}
