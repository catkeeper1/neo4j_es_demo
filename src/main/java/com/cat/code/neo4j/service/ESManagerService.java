package com.cat.code.neo4j.service;

import com.cat.code.neo4j.domain.SkuInfo;

import java.util.List;
import java.util.Map;

public interface ESManagerService {
    /**
     * 创建索引库结构
     */
    void createMappingAndIndex();

    /**
     * 导入所有数据进es
     */
    void importAll();

    /**
     * 新增数据
     */
    void insert();

    /**
     * 修改数据
     */
    void update();

    /**
     * 删除数据
     */
    void delete();

    /**
     * 查询数据
     *
     * @return
     */
    Map search();

    /**
     * 根据name查询sku
     *
     * @return
     */
    List<SkuInfo> findSkuInfoByName();
}
