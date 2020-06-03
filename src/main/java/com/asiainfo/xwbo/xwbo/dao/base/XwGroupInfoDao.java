package com.asiainfo.xwbo.xwbo.dao.base;

import com.asiainfo.xwbo.xwbo.model.po.XwGroupInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author jiahao jin
 * @create 2020-05-06 09:30
 */
@Mapper
@Repository
public interface XwGroupInfoDao {

    List<XwGroupInfoPo> qryAll(QryPeripheryXwGroupInfoSo queryXwGroupInfoSo);

    List<XwGroupProductInfoSo> qryProductInfo(QryXwGroupInfoSo qryXwGroupInfoSo);

    List<XwGroupCaseItemInfoSo> qryCaseItemInfo(QryXwGroupInfoSo qryXwGroupInfoSo) ;

    int updateHandleUser(XwGroupInfoPo xwGroupInfoPo);

    List<XwGroupInfoPo> qryUserHandleInfo(QryUserHandleInfoSo qryUserHandleInfoSo);

    void callProduct(Map callParams);
    void callHyal(Map callParams);
}
