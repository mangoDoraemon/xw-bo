package com.asiainfo.xwbo.xwbo.dao.base;

import com.asiainfo.xwbo.xwbo.model.po.XwUserInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.QrySubordinatesSo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-06 16:17
 */
@Mapper
@Repository
public interface XwUserInfoDao {

    List<XwUserInfoPo> qrySubordinates(QrySubordinatesSo qrySubordinatesSo);
}