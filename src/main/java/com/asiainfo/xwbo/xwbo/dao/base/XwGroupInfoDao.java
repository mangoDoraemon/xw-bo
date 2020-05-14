package com.asiainfo.xwbo.xwbo.dao.base;

import com.asiainfo.xwbo.xwbo.model.po.XwGroupInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.QryPeripheryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.QryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.XwGroupCaseItemInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.XwGroupProductInfoSo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
