package com.cat.code.neo4j;

import com.cat.code.neo4j.domain.SkuInfo;
import com.cat.code.neo4j.repository.es.ESManagerRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceSpringDataNeo4jApplication.class)
public class ESManagerRepositoryTest {

    @Autowired
    private ESManagerRepository esManagerRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private Environment env;

    @After
    public void after() {
        esManagerRepository.deleteAll();
    }

    @Test
    public void test() {
//        // 创建索引
//        elasticsearchRestTemplate.createIndex(SkuInfo2.class);
//        // 创建映射
//        elasticsearchRestTemplate.putMapping(SkuInfo2.class);
        List<SkuInfo> skuInfoList = esManagerRepository.findByName("华为");
        assertThat(skuInfoList).contains(skuInfoList.get(0));
        skuInfoList.forEach(skuInfo -> assertThat(skuInfo.getName()).isEqualTo("华为"));
        skuInfoList.forEach(skuInfo -> System.out.println("skuInfo = " + skuInfo));
    }

    @Test
    public void insert() {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(1L);
        skuInfo.setName("华为");
        skuInfo.setPrice(7L);
        skuInfo = elasticsearchRestTemplate.save(skuInfo);
        System.out.println("skuInfo = " + skuInfo);
    }

    @Test
    public void search() {
        // 查询条件构建类
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // BoolQueryBuilder类是QueryBuilders的接口实现类
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 添加查询条件
        /**
         * must是必须的，matchQuery表示模糊查询，参数一表示映射名称，参数二表示映射值，operator表示连接查询条件关系
         */
        boolQuery.must(QueryBuilders.termQuery("id", "1"));
        //boolQuery.must(QueryBuilders.matchQuery("name", "华为").operator(Operator.AND));
        queryBuilder.withQuery(boolQuery);
        SearchHits<SkuInfo> hits = elasticsearchRestTemplate.search(queryBuilder.build(), SkuInfo.class);
        System.out.println("hits = " + hits);
    }

    @Test
    public void zzd() {
        Scanner scanner = new Scanner(System.in);
        String str;
        while (true) {
            str = scanner.next();
            str = str.replace("吗", "");
            str = str.replace("?", "!");
            str = str.replace("？", "！");
            System.out.println(str);
        }
    }
}
