package com.asiainfo.xwbo.xwbo.system;

import com.asiainfo.xwbo.xwbo.model.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Zhouce Chen
 * @version Sep 03, 2019
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AjaxResult doException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        request.setAttribute("ex", e);
        e.printStackTrace();
        return AjaxResult.markError(e.getMessage());
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(value = RestClientException.class)
    @ResponseBody
    public AjaxResult restException(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        log.error("调用接口异常", e);
        request.setAttribute("ex", e);
        return AjaxResult.markError("调用接口异常");
    }


}
