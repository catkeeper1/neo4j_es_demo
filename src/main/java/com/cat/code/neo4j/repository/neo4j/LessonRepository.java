package com.cat.code.neo4j.repository.neo4j;

import com.cat.code.neo4j.domain.Lesson;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends Neo4jRepository<Lesson, Long> {
}
