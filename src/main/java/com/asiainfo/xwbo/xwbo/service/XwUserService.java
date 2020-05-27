package com.asiainfo.xwbo.xwbo.service;

import com.asiainfo.xwbo.xwbo.model.XwUserInfo;
import com.asiainfo.xwbo.xwbo.model.so.LoginSo;
import com.asiainfo.xwbo.xwbo.model.so.QrySubordinatesSo;
import com.asiainfo.xwbo.xwbo.model.so.XwUserInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.PageResultVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwAreaInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwMicroInfoVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-11 10:57
 */
public interface XwUserService {
    XwAreaInfoVo areaInfo(XwUserInfoSo xwUserInfoSo) throws Exception;

    XwUserInfo checkUserId(XwUserInfoSo xwUserInfoSo) throws Exception;

    boolean checkToken(HttpServletRequest request) throws Exception;

    XwUserInfo qryInfo(XwUserInfoSo xwUserInfoSo) throws Exception;

    XwMicroInfoVo microInfo(XwUserInfoSo xwUserInfoSo) throws Exception;

    XwUserInfo login(LoginSo loginSo) throws Exception;

    PageResultVo qrySubordinates(QrySubordinatesSo qrySubordinatesSo) throws Exception;

    PageResultVo qrySubordinatesWorkDetail(QrySubordinatesSo qrySubordinatesSo) throws Exception;

    List qrySubordinatesRoleList(XwUserInfoSo xwUserInfoSo) throws Exception;

    String getAes(String userId) throws Exception;
}
