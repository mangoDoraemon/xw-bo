package com.asiainfo.xwbo.xwbo.controller;

import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jiahao jin
 * @create 2020-04-30 14:46
 */
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {
    @Resource
    private ICommonExtDao commonExtDao;

    @RequestMapping("")
    public Object health() {
        log.info(System.getProperty("file.encoding"));
        return "health";
    }
}
