package com.asiainfo.xwbo.xwbo.controller;

import com.asiainfo.xwbo.xwbo.dao.ICommonExtDao;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author jiahao jin
 * @create 2020-04-30 14:46
 */
@RestController
@RequestMapping("/health")
public class HealthController {
    @Resource
    private ICommonExtDao commonExtDao;

    @RequestMapping("")
    public Object health() {

        return "health";
    }
}
