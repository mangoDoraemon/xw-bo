package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiahao jin
 * @create 2020-05-08 14:50
 */
@Slf4j
@Aspect
@Component
public class WebLogAop {

    // 定义切点Pointcut


    @Pointcut( "execution(public * com.asiainfo..service.*.*(..))" )
    public void expression() {}


    @Before(value = "expression()")
    public void beforeMethod(JoinPoint joinPoint) {

        ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();

        String servletPath = request.getServletPath();

        log.info("调用path: {}   args: {}",servletPath,JSONObject.toJSONString(joinPoint.getArgs()));

    }


    @AfterReturning(value="expression()" ,returning="result")
    public void afterReturning(Object result) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();

        String servletPath = request.getServletPath();

        log.info("path: {}  return: {}",servletPath, JSONObject.toJSONString(result));

    }

    @AfterThrowing(value="expression()" ,throwing="e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        String name=joinPoint.getSignature().getName();
        log.error("method {}  occur exception : {}",name,e.getMessage());
    }
}