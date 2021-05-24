package com.cat.code.neo4j.controller;

import com.cat.code.neo4j.domain.SkuInfo;
import com.cat.code.neo4j.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manager")
public class ESManagerController {

    @Autowired
    private ESManagerService esManagerService;

    // 创建索引库结构
    @GetMapping("/create")
    public String create() {
        esManagerService.createMappingAndIndex();
        return "创建索引库结构成功";
    }

    // 导入全部数据
    @GetMapping("/importAll")
    public String importAll() {
        esManagerService.importAll();
        return "导入数据进索引库成功";
    }

    @RequestMapping("/insert")
    public String insert() {
        esManagerService.insert();
        return "新增数据成功";
    }

    @RequestMapping("/update")
    public String update() {
        esManagerService.update();
        return "修改数据成功";
    }

    @RequestMapping("/delete")
    public String delete() {
        esManagerService.delete();
        return "删除数据成功";
    }

    @RequestMapping("/search")
    public Map search() {
        return esManagerService.search();
    }

    @RequestMapping("/skuInfo/get")
    public List<SkuInfo> skuInfo() {
        return esManagerService.findSkuInfoByName();
    }
}
