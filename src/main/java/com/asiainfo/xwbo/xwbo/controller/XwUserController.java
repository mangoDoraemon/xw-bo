package com.asiainfo.xwbo.xwbo.controller;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.dao.sqlBuild.SqlBuilder;
import com.asiainfo.xwbo.xwbo.model.AjaxResult;
import com.asiainfo.xwbo.xwbo.model.XwUserInfo;
import com.asiainfo.xwbo.xwbo.model.po.XwGroupInfoPo;
import com.asiainfo.xwbo.xwbo.model.po.XwUserInfoPo;
import com.asiainfo.xwbo.xwbo.model.so.LoginSo;
import com.asiainfo.xwbo.xwbo.model.so.QrySubordinatesSo;
import com.asiainfo.xwbo.xwbo.model.so.QryUserHandleInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.XwUserInfoSo;
import com.asiainfo.xwbo.xwbo.service.XwUserService;
import com.asiainfo.xwbo.xwbo.system.Sign;
import com.asiainfo.xwbo.xwbo.system.constants.Constant;
import com.asiainfo.xwbo.xwbo.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-11 10:55
 */
@RestController
@RequestMapping("/user")
public class XwUserController {

    @Autowired
    private XwUserService xwUserService;

    @RequestMapping("/microInfo")
    @Sign
    public AjaxResult microInfo(@RequestBody XwUserInfoSo xwUserInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwUserService.microInfo(xwUserInfoSo));
    }

    @RequestMapping("/login")
    public AjaxResult login(@RequestBody LoginSo loginSo, HttpServletRequest request) throws Exception {
//        HttpSession session = request.getSession();
//        System.out.println(session.getId());
        XwUserInfo xwUserInfo = null;
//        if (session.getAttribute(Constant.SESSION_KEY) == null) {
//            System.out.println("不存在session");
            xwUserInfo = xwUserService.login(loginSo);
//            session.setAttribute(Constant.SESSION_KEY, xwUserInfo);
//            session.setMaxInactiveInterval(3600);
//        } else {
//            System.out.println("存在session");
//        }
        return AjaxResult.markSuccess(xwUserInfo);
    }

    @RequestMapping("/qryInfo")
    @Sign
    public AjaxResult qryInfo(@RequestBody XwUserInfoSo xwUserInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwUserService.qryInfo(xwUserInfoSo));
    }

    @RequestMapping("/checkToken")
    public AjaxResult checkToken(HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwUserService.checkToken(request));
    }

    @RequestMapping("/checkUserId")
    public AjaxResult checkUserId(@RequestBody XwUserInfoSo xwUserInfoSo, HttpServletRequest request) throws Exception {
        XwUserInfo xwUserInfo = null;
        xwUserInfo = xwUserService.checkUserId(xwUserInfoSo);
        String token = UserUtil.sign(xwUserInfo);
        xwUserInfo.setToken(token);
        return AjaxResult.markSuccess(xwUserInfo);
    }

    @RequestMapping("/qrySubordinates")
    @Sign
    public AjaxResult qrySubordinates(@RequestBody QrySubordinatesSo qrySubordinatesSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwUserService.qrySubordinates(qrySubordinatesSo));
    }

    @RequestMapping("/qrySubordinatesWorkDetail")
    @Sign
    public AjaxResult qrySubordinatesWorkDetail(@RequestBody QrySubordinatesSo qrySubordinatesSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwUserService.qrySubordinatesWorkDetail(qrySubordinatesSo));
    }

    @RequestMapping("/qrySubordinatesRoleList")
    @Sign
    public AjaxResult qrySubordinatesRoleList(@RequestBody XwUserInfoSo xwUserInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwUserService.qrySubordinatesRoleList(xwUserInfoSo));
    }

    @RequestMapping("/getAes/{userId}")
    public AjaxResult getAes(HttpServletRequest request, @PathVariable String userId) throws Exception {
//        return AjaxResult.markSuccess();
        return AjaxResult.markSuccess(xwUserService.getAes(userId));
    }

}
