package com.cat.code.neo4j.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EkController {

    @GetMapping(value = "elk/{params}")
    public String testElk(@PathVariable String params) {
        log.info("接口入参 {}", params);
        log.error("测试一个没有异常信息的日志添加");
        params = "Hello World";
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            log.error("这是一个error日志:" + e.getMessage(), e);
        }
        return params;
    }
}

