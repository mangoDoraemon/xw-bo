package com.asiainfo.xwbo.xwbo.controller;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.xwbo.xwbo.model.AjaxResult;
import com.asiainfo.xwbo.xwbo.model.so.QryPeripheryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.QryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.service.XwGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiahao jin
 * @create 2020-05-06 09:30
 */
@RestController
@RequestMapping("/group")
public class XwGroupController {

    @Autowired
    private XwGroupService xwGroupService;

    @RequestMapping("/qryAll")
    public AjaxResult qryAll(@RequestBody QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryAll(qryPeripheryXwGroupInfoSo));
    }

    @RequestMapping("/qryInfo")
    public AjaxResult qryInfo(@RequestBody QryXwGroupInfoSo qryXwGroupInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryInfo(qryXwGroupInfoSo));
    }

    @RequestMapping("/qryMember")
    public AjaxResult qryMember(@RequestBody QryXwGroupInfoSo qryXwGroupInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryMember(qryXwGroupInfoSo));
    }

    @RequestMapping("/sync")
    public AjaxResult sync(@RequestBody SyncXwGroupInfoSo syncXwGroupInfoSo, HttpServletRequest request) throws Exception {
        xwGroupService.sync(syncXwGroupInfoSo);
        return AjaxResult.markSuccess();
    }

    @RequestMapping("/industryClass")
    public AjaxResult industryClass() throws Exception {
        return AjaxResult.markSuccess(xwGroupService.industryClass());
    }
}
