package com.asiainfo.xwbo.xwbo.controller;

import com.asiainfo.xwbo.xwbo.model.AjaxResult;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.QryPeripheryXwGroupInfoSo;
import com.asiainfo.xwbo.xwbo.service.XwAreaService;
import com.asiainfo.xwbo.xwbo.service.XwGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiahao jin
 * @create 2020-05-09 11:58
 */
@RestController
@RequestMapping("/area")
public class XwAreaController {

    @Autowired
    private XwAreaService xwAreaService;

    @RequestMapping("/qryInfo")
    public AjaxResult qryAll(@RequestBody QryAreaInfoSo qryAreaInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwAreaService.qryInfo(qryAreaInfoSo));
    }
}
