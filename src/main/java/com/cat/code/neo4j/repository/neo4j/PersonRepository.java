package com.cat.code.neo4j.repository.neo4j;

import com.cat.code.neo4j.domain.PersonEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends Neo4jRepository<PersonEntity, Long> {
    List<PersonEntity> findByName(String name);

    List<PersonEntity> findByNameLike(String name);

    void deleteByName(String name);

    @Query(""
            + "MATCH p=shortestPath((bacon:Person {name: $person1})-[*]-(meg:Person {name: $person2}))\n"
            + "RETURN p"
    )
    List<PersonEntity> findAllOnShortestPathBetween(@Param("person1") String person1, @Param("person2") String person2);

    @Query(""
            + "MATCH p=shortestPath(\n"
            + "(bacon:Person {name: $person1})-[*]-(meg:Person {name: $person2}))\n"
            + "WITH p, [n IN nodes(p) WHERE n:Movie] AS x\n"
            + "UNWIND x AS m\n"
            + "MATCH (m) <-[r:DIRECTED]-(d:Person)\n"
            + "RETURN p, collect(r), collect(d)"
    )
    List<PersonEntity> findAllOnShortestPathBetween2(@Param("person1") String person1, @Param("person2") String person2);

    @Query("MATCH (a:Person {name: $name}) RETURN a")
    Optional<PersonEntity> findByCustomQuery(String name);

    @Query("MATCH (a:Person) WHERE a.name = :#{#pt1 + #pt2} RETURN a")
    Optional<PersonEntity> findByCustomQueryWithSpEL(String pt1, String pt2);

    @Query(""
            + "MATCH (n:Person) WHERE n.name = $name RETURN n "
            + ":#{orderBy(#pageable)} SKIP $skip LIMIT $limit"
    )
    Slice<PersonEntity> findSliceByName(String name, Pageable pageable);

    @Query(""
            + "MATCH (n:Person) WHERE n.name = $name RETURN n :#{orderBy(#sort)}"
    )
    List<PersonEntity> findAllByName(String name, Sort sort);


    @Query("match (p:person{person_name:{name}})-[a:ACTED_IN]->(m:movie) return m")
    List<PersonEntity> findByPerson(@Param("name") String name);

    @Query("match (p:person{person_name:{name}})-[a:ACTED_IN]->(m:movie) return count(m) as count")
    Integer findCountByPerson(@Param("name") String name);

    @Query("create (m:movie{id:{id},movie_id:{movieId},movie_title:{movieTitle},movie_introduction:{movieIntroduction}}) return m")
    PersonEntity saveMovie(@Param("id") Long id, @Param("movieTitle") String movieTitle, @Param("movieId") String movieId, @Param("movieIntroduction") String movieIntroduction);


}
