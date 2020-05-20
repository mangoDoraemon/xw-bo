package com.asiainfo.xwbo.xwbo.controller;

import com.asiainfo.xwbo.xwbo.model.AjaxResult;
import com.asiainfo.xwbo.xwbo.model.so.QryJobInfoSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwJobInfoListSo;
import com.asiainfo.xwbo.xwbo.model.so.SyncXwJobInfoSo;
import com.asiainfo.xwbo.xwbo.service.XwJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author jiahao jin
 * @create 2020-05-18 14:34
 */
@RestController
@RequestMapping("/job")
public class XwJobController {
    @Autowired
    private XwJobService xwJobService;

    @RequestMapping("/syncXwJob")
    public AjaxResult syncXwJob(@RequestBody SyncXwJobInfoListSo syncXwJobInfoListSo, HttpServletRequest request) throws Exception {
        xwJobService.syncXwJob(syncXwJobInfoListSo);
        return AjaxResult.markSuccess();
    }

    @RequestMapping("/qryAcceptJobAll")
    public AjaxResult qryAcceptJobAll(@RequestBody QryJobInfoSo qryJobInfoSo, HttpServletRequest request) throws Exception {

        return AjaxResult.markSuccess(xwJobService.qryAcceptJobAll(qryJobInfoSo));
    }

    @RequestMapping("/qryAcceptJobInfo")
    public AjaxResult qryAcceptJobInfo(@RequestBody @Valid QryJobInfoSo qryJobInfoSo, HttpServletRequest request) throws Exception {

        return AjaxResult.markSuccess(xwJobService.qryAcceptJobInfo(qryJobInfoSo));
    }

    @RequestMapping("/qryEstablishJobAll")
    public AjaxResult qryEstablishJobAll(@RequestBody QryJobInfoSo qryJobInfoSo, HttpServletRequest request) throws Exception {

        return AjaxResult.markSuccess(xwJobService.qryEstablishJobAll(qryJobInfoSo));
    }

    @RequestMapping("/qryEstablishJobInfo")
    public AjaxResult qryEstablishJobInfo(@RequestBody @Valid QryJobInfoSo qryJobInfoSo, HttpServletRequest request) throws Exception {

        return AjaxResult.markSuccess(xwJobService.qryEstablishJobInfo(qryJobInfoSo));
    }

    @RequestMapping("/qryAllJobCount")
    public AjaxResult qryAllJobCount(@RequestBody QryJobInfoSo qryJobInfoSo, HttpServletRequest request) throws Exception {

        return AjaxResult.markSuccess(xwJobService.qryAllJobCount(qryJobInfoSo));
    }

    @RequestMapping("/acceptUpdateHandleMessage")
    public AjaxResult acceptUpdateHandleMessage(@RequestBody SyncXwJobInfoSo syncXwJobInfoSo, HttpServletRequest request) throws Exception {
        xwJobService.acceptUpdateHandleMessage(syncXwJobInfoSo);
        return AjaxResult.markSuccess();
    }

    @RequestMapping("/acceptUpdateState")
    public AjaxResult acceptUpdateState(@RequestBody SyncXwJobInfoSo syncXwJobInfoSo, HttpServletRequest request) throws Exception {
        xwJobService.acceptUpdateState(syncXwJobInfoSo);
        return AjaxResult.markSuccess();
    }
}
