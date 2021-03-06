package com.cat.code.neo4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

/**
 * @Author: zzd
 * @Time: 2021/4/15 12:00
 * @Description: 启动类
 */
@SpringBootApplication
@EnableNeo4jRepositories
@EnableElasticsearchRepositories
@ComponentScan(basePackages = "com.cat.code.neo4j")
public class ServiceSpringDataNeo4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSpringDataNeo4jApplication.class, args);
    }

}
