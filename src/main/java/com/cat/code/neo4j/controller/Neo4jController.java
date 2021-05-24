package com.cat.code.neo4j.controller;

import com.cat.code.neo4j.domain.PersonEntity;
import com.cat.code.neo4j.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo4j")
public class Neo4jController {

    @Autowired
    private PersonService personService;

    @RequestMapping("/insert")
    public String insert() {
        personService.savePerson();
        return "新增数据成功";
    }

    @RequestMapping("/update")
    public String update() {
        personService.update();
        return "修改数据成功";
    }

    @RequestMapping("/delete")
    public String delete() {
        personService.deleteById();
        return "删除数据成功";
    }

    @RequestMapping("/get")
    public PersonEntity get() {
        return personService.getPersonEntity();
    }
}
