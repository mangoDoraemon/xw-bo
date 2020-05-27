package com.asiainfo.xwbo.xwbo.controller;

import com.asiainfo.xwbo.xwbo.model.AjaxResult;
import com.asiainfo.xwbo.xwbo.model.so.QryPeripheryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.QryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.UpdateHandleStateSo;
import com.asiainfo.xwbo.xwbo.service.XwGroupService;
import com.asiainfo.xwbo.xwbo.system.Sign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Sign
    public AjaxResult qryAll(@RequestBody QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryAll(qryPeripheryXwGroupInfoSo));
    }

    @RequestMapping("/qryAllPosition")
    @Sign
    public AjaxResult qryAllPosition(@RequestBody QryPeripheryXwGroupInfoSo qryPeripheryXwGroupInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryAllPosition(qryPeripheryXwGroupInfoSo));
    }

    @RequestMapping("/qryInfo")
    @Sign
    public AjaxResult qryInfo(@RequestBody QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryInfo(qryXwGroupInfoSo));
    }

    @RequestMapping("/qryInfoList")
    @Sign
    public AjaxResult qryInfoList(@RequestBody QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryInfoList(qryXwGroupInfoSo));
    }

    @RequestMapping("/qryMember")
    @Sign
    public AjaxResult qryMember(@RequestBody QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryMember(qryXwGroupInfoSo));
    }

    @RequestMapping("/sync")
    @Sign
    public AjaxResult sync(@RequestBody SyncXwGroupInfoSo syncXwGroupInfoSo) throws Exception {
        xwGroupService.sync(syncXwGroupInfoSo);
        return AjaxResult.markSuccess();
    }

    @RequestMapping("/changeHandleState")
    @Sign
    public AjaxResult changeHandleState(@RequestBody UpdateHandleStateSo updateHandelStateSo) throws Exception {
        xwGroupService.changeHandleState(updateHandelStateSo);
        return AjaxResult.markSuccess();
    }

    @RequestMapping("/qryProductInfo")
    @Sign
    public AjaxResult qryProductInfo(@RequestBody QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryProductInfo(qryXwGroupInfoSo));
    }

    @RequestMapping("/qryCaseItemInfo")
    @Sign
    public AjaxResult qryCaseItemInfo(@RequestBody QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwGroupService.qryCaseItemInfo(qryXwGroupInfoSo));
    }

    @RequestMapping("/industryClass")
    public AjaxResult industryClass() throws Exception {
        return AjaxResult.markSuccess(xwGroupService.industryClass());
    }

    @RequestMapping("/managementState")
    public AjaxResult managementState() throws Exception {
        return AjaxResult.markSuccess(xwGroupService.managementState());
    }

    @RequestMapping("/handleState")
    public AjaxResult handleState() throws Exception {
        return AjaxResult.markSuccess(xwGroupService.handleState());
    }

    @RequestMapping("/delete")
    @Sign
    public AjaxResult delete(@RequestBody QryXwGroupInfoSo qryXwGroupInfoSo) throws Exception {
        xwGroupService.delete(qryXwGroupInfoSo);
        return AjaxResult.markSuccess();
    }


}
