package com.asiainfo.xwbo.xwbo.controller;

import com.asiainfo.xwbo.xwbo.model.AjaxResult;
import com.asiainfo.xwbo.xwbo.model.so.QryAreaInfoSo;
import com.asiainfo.xwbo.xwbo.service.XwAreaService;
import com.asiainfo.xwbo.xwbo.system.Sign;
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

    @RequestMapping("/qryAreaInfo")
    @Sign
    public AjaxResult qryAreaInfo(@RequestBody QryAreaInfoSo qryAreaInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwAreaService.qryAreaInfo(qryAreaInfoSo));
    }

    @RequestMapping("/getXwAreaRate")
    @Sign
    public AjaxResult getXwAreaRate(@RequestBody QryAreaInfoSo qryAreaInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwAreaService.getXwAreaRate(qryAreaInfoSo));
    }

    @RequestMapping("/qryCascadeAreaInfo")
    @Sign
    public AjaxResult qryCascadeAreaInfo(@RequestBody QryAreaInfoSo qryAreaInfoSo) throws Exception {
        return AjaxResult.markSuccess(xwAreaService.qryCascadeAreaInfo(qryAreaInfoSo));
    }

    @RequestMapping("/qryMicroInfo")
    @Sign
    public AjaxResult qryMicroInfo(@RequestBody QryAreaInfoSo qryAreaInfoSo, HttpServletRequest request) throws Exception {
        return AjaxResult.markSuccess(xwAreaService.qryMicroInfo(qryAreaInfoSo));
    }
}
