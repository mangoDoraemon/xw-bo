package com.asiainfo.xwbo.xwbo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiahao jin
 * @create 2020-04-30 14:46
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @RequestMapping("")
    public Object health(){
        return "health";
    }
}
