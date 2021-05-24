package com.cat.code.neo4j.repository.es;

import com.cat.code.neo4j.domain.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ESManagerRepository extends ElasticsearchRepository<SkuInfo, Long> {
    List<SkuInfo> findByName(String name);
}
