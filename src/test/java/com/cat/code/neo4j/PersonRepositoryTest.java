package com.cat.code.neo4j;

import com.cat.code.neo4j.domain.PersonEntity;
import com.cat.code.neo4j.repository.neo4j.PersonRepository;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 用DataNeo4jTest测试 模拟一个新数据库
 */
@DataNeo4jTest
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ServiceSpringDataNeo4jApplication.class)
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void findByName() {
        List<PersonEntity> personEntityList = new ArrayList<>();
        PersonEntity person = new PersonEntity("科比", "男");
        personEntityList.add(person);
        person = new PersonEntity("欧文", "男");
        personEntityList.add(person);
        personRepository.saveAll(personEntityList);
        List<PersonEntity> personEntities = personRepository.findByName("科比");
        assertThat(personEntities.size()).isEqualTo(1);
        personEntities.forEach(p -> assertThat(p.getName()).isEqualTo("科比"));
        personEntities.forEach(personEntity -> System.out.println("personEntity = " + personEntity));
    }

}
