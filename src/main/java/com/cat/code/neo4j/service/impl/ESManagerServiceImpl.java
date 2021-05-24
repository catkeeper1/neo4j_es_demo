package com.cat.code.neo4j.service.impl;

import com.cat.code.neo4j.domain.SkuInfo;
import com.cat.code.neo4j.repository.es.ESManagerRepository;
import com.cat.code.neo4j.service.ESManagerService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ESManagerServiceImpl implements ESManagerService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    private ESManagerRepository esManagerRepository;

    // 创建索引库结构
    @Override
    public void createMappingAndIndex() {
        // 创建索引
        elasticsearchTemplate.createIndex(SkuInfo.class);
        // 创建映射
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    // 导入全部数据到索引库
    @Override
    public void importAll() {
        List<SkuInfo> skuInfoList = new ArrayList<>();
        SkuInfo skuInfo = null;
        for (int i = 1; i < 100; i++) {
            skuInfo = new SkuInfo();
            skuInfo.setId((long) i);
            skuInfo.setName("str");
            skuInfo.setPrice((long) i);
            skuInfo.setNum(7);
            skuInfo.setImage(".com");
            skuInfo.setStatus("1");
            skuInfo.setCreateTime(new Date());
            skuInfo.setUpdateTime(new Date());
            skuInfo.setIsDefault("y");
            skuInfo.setSpuId(7L);
            skuInfo.setCategoryId(7L);
            skuInfo.setCategoryName("str");
            skuInfo.setBrandName("ios");
            skuInfo.setSpec("5g");
            skuInfo.setSpecMap(new HashMap<>());
            skuInfoList.add(skuInfo);
        }
        // 批量导入到索引库
        esManagerRepository.saveAll(skuInfoList);
    }

    @Override
    public void insert() {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(8L);
        skuInfo.setName("华为");
        skuInfo.setSpec("5g");
        skuInfo.setCategoryName("手机");
        esManagerRepository.save(skuInfo);
    }

    @Override
    public void update() {
        SkuInfo skuInfo = esManagerRepository.findById(7L).get();
        skuInfo.setName("华为");
        esManagerRepository.save(skuInfo);
    }

    @Override
    public void delete() {
        esManagerRepository.deleteById(44L);
    }

    @Override
    public Map search() {
        Map<String, Object> resultMap = new HashMap<>();
        // 查询条件构建类
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // BoolQueryBuilder类是QueryBuilders的接口实现类
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 添加查询条件
        /**
         * must是必须的，matchQuery表示模糊查询，参数一表示映射名称，参数二表示映射值，operator表示连接查询条件关系
         */
        boolQuery.must(QueryBuilders.matchQuery("name", "华为").operator(Operator.AND));
        // 把条件对象交给条件构建器
        queryBuilder.withQuery(boolQuery);
        // 开启分页
        Object pageNum = resultMap.get("pageNum");
        Object pageSize = resultMap.get("pageSize");
        if (pageNum == null) {
            pageNum = "1";
        }
        if (pageSize == null) {
            pageSize = "30";
        }
        // 设置分页
        queryBuilder.withPageable(PageRequest.of(Integer.parseInt(pageNum.toString()) - 1, Integer.parseInt(pageSize.toString())));
        // 设置当前的高亮域以及高亮的样式
        HighlightBuilder.Field field = new HighlightBuilder.Field("name")
                .preTags("<span style='color:red'>")// 设置高亮显示的html代码的前缀
                .postTags("</span>");// 设置高亮显示的html代码的后缀
        queryBuilder.withHighlightFields(field);
        org.springframework.data.elasticsearch.core.SearchHits<SkuInfo> hits = elasticsearchTemplate.search(queryBuilder.build(), SkuInfo.class);
        resultMap.put("pageNum", pageNum);
        resultMap.put("hit", hits);
        return resultMap;
    }

    @Override
    public List<SkuInfo> findSkuInfoByName() {
        return esManagerRepository.findByName("华为");
    }
}
