package com.asiainfo.xwbo.xwbo.service;

import com.asiainfo.xwbo.xwbo.model.XwUserInfo;
import com.asiainfo.xwbo.xwbo.model.so.LoginSo;
import com.asiainfo.xwbo.xwbo.model.so.XwUserInfoSo;
import com.asiainfo.xwbo.xwbo.model.vo.XwAreaInfoVo;
import com.asiainfo.xwbo.xwbo.model.vo.XwMicroInfoVo;

/**
 * @author jiahao jin
 * @create 2020-05-11 10:57
 */
public interface XwUserService {
    XwAreaInfoVo areaInfo(XwUserInfoSo xwUserInfoSo) throws Exception;

    XwUserInfo checkUserId(XwUserInfoSo xwUserInfoSo) throws Exception;

    XwUserInfo qryInfo(XwUserInfoSo xwUserInfoSo) throws Exception;

    XwMicroInfoVo microInfo(XwUserInfoSo xwUserInfoSo) throws Exception;

    XwUserInfo login(LoginSo loginSo) throws Exception;
}
