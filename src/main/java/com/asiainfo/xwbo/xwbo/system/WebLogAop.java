package com.asiainfo.xwbo.xwbo.system;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jiahao jin
 * @create 2020-05-08 14:50
 */
@Slf4j
@Aspect
@Component
public class WebLogAop {

    // 定义切点Pointcut


    @Pointcut( "execution(public * com.asiainfo..controller.*.*(..))" )
    public void expression() {}


    @Before(value = "expression()")
    public void beforeMethod(JoinPoint joinPoint) {

        ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = sra.getRequest();

        String servletPath = request.getServletPath();

        Object[] args = joinPoint.getArgs();
        Stream<?> stream = ArrayUtils.isEmpty(args) ? Stream.empty() : Arrays.asList(args).stream();
        List<Object> logArgs = stream.filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse))).collect(Collectors.toList());

        log.info("调用path: {}   args: {}",servletPath,JSONObject.toJSONString(logArgs));

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