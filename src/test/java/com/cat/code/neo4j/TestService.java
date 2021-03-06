package com.cat.code.neo4j;

import com.cat.code.neo4j.domain.PersonEntity;
import com.cat.code.neo4j.domain.SkuInfo;
import com.cat.code.neo4j.domain.Student;
import com.cat.code.neo4j.repository.neo4j.PersonRepository;
import com.cat.code.neo4j.service.ESManagerService;
import com.cat.code.neo4j.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: zzd
 * @Time: 2019/11/11 15:30
 * @Description: todo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceSpringDataNeo4jApplication.class)
public class TestService {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Autowired
    private ESManagerService esManagerService;

    @Test
    public void testSave() throws Exception {
        personService.savePerson();
    }

    @Test
    public void testEs() throws Exception {
        List<SkuInfo> skuInfoList = esManagerService.findSkuInfoByName();
        skuInfoList.forEach(skuInfo -> System.out.println("skuInfo = " + skuInfo));
    }

    @Test
    public void testSaveUpdate() throws Exception {
        personService.saveAndUpdatePerson();
    }

    @Test
    public void testFind() throws Exception {
        personService.findByName();
    }

    @Test
    public void testDelete() throws Exception {
        personService.deleteById();
    }

    @Test
    public void testTransactional() throws Exception {
        personService.personTransactional();
    }

    @Test
    public void testLike() throws Exception {
        personService.LikePerson();
    }

    @Test
    public void findAll() throws Exception {
        Iterable<PersonEntity> personEntities = personRepository.findAll();
        for (PersonEntity personEntity : personEntities) {
            System.out.println("personEntity = " + personEntity.getId() + personEntity.getName());
        }
    }

    @Test
    public void deleteByID() throws Exception {
        List<PersonEntity> all = personRepository.findAllOnShortestPathBetween("??????", "??????");
        for (PersonEntity personEntity : all) {
            System.out.println("personEntity = " + personEntity);
        }
    }

    @Test
    public void test1() throws Exception {
        List<PersonEntity> all = personRepository.findAllOnShortestPathBetween2("??????", "??????");
        for (PersonEntity personEntity : all) {
            System.out.println("personEntity = " + personEntity);
        }
    }

    @Test
    public void test2() throws Exception {
        Optional<PersonEntity> query = personRepository.findByCustomQuery("??????");
        PersonEntity personEntity1 = query.get();
        System.out.println("personEntity1 = " + personEntity1);
    }

    @Test
    public void test3() throws Exception {
        Optional<PersonEntity> query = personRepository.findByCustomQueryWithSpEL("???", "???");
        PersonEntity personEntity1 = query.get();
        System.out.println("personEntity1 = " + personEntity1);
    }

    @Test
    public void test4() throws Exception {
        Optional<PersonEntity> query = personRepository.findByCustomQueryWithSpEL("???", "???");
        PersonEntity personEntity1 = query.get();
        System.out.println("personEntity1 = " + personEntity1);
    }

    /**
     * ????????????
     */
    @Test
    public void test5() {
        personService.initData();
    }

    /**
     * ??????????????????
     */
    @Test
    public void test6() {
        personRepository.deleteAll();
    }

    /**
     * ????????????????????????
     */
    @Test
    public void testCountStudent() {
        Long all = personService.studentCount(null);
        System.out.println("all = " + all);
        Long seven = personService.studentCount("?????????");
        System.out.println("seven = " + seven);
    }

    /**
     * ????????????????????????????????????
     */
    @Test
    public void testFindClassmates() {
        Map<String, List<Student>> classmates = personService.findClassmatesGroupByLesson();
        classmates.forEach((k, v) -> {
            System.out.println("k = " + k);
        });
    }

    /**
     * ??????????????????????????????????????????/?????????????????????/??????
     */
    @Test
    public void testFindTeacherStudent() {
        Map<String, Set<Student>> teacherStudent = personService.findTeacherStudent();
        teacherStudent.forEach((k, v) -> {
            System.out.println("k = " + k);
        });
    }

    /**
     * ?????????????????????????????????????????????
     */
    @Test
    public void test7() {
        //String sql = "match( p1: Person {name:\"??????\"} )-[rel:FRIEND]->(p2) return p2.name as name , p2.sex as sex";
        String sql = "match (p: Person) return p";
        List<PersonEntity> all = neo4jTemplate.findAll(sql, PersonEntity.class);
        for (PersonEntity personEntity : all) {
            System.out.println("personEntity = " + personEntity);
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    @Test
    public void test8() {
        String sql = "match( p1: Person {name:'??????'} )-[rel:FRIEND]->(p2) return p2.name as name , p2.sex as sex";
        List<PersonEntity> all = neo4jTemplate.findAll(sql, PersonEntity.class);
        for (PersonEntity personEntity : all) {
            System.out.println("personEntity = " + personEntity);
        }
    }

    @Test
    public void test10() {
        int i = getMaxLen("11111abccba12345677654321a");
        System.out.println("test10 = " + i);
    }

    public static int getMaxLen(String str) {
        if (str == null || str.equals("")) {
            return 0;
        }
        char[] charArr = str.toCharArray();
        int max = 0;
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            // i??????????????????????????????i-j????????????i+j???????????????????????????????????????????????????
            for (int j = 0; i - j >= 0 && i + j < str.length(); j++) {
                if (charArr[i + j] != charArr[i - j]) {// ????????????????????????????????????????????????i?????????j?????????
                    break; // ??????????????????????????????????????????????????????2*j+1
                }
                len = 2 * j + 1;
            }
            max = max > len ? max : len;  // ??????len????????????????????????????????????????????????????????????
            // ??????????????????????????????????????????2*j+2
            for (int j = 0; i - j >= 0 && i + j + 1 < str.length(); j++) {
                if (charArr[i + j + 1] != charArr[i - j]) {
                    break;
                }
                len = 2 * j + 2;
            }
            max = max > len ? max : len;
        }
        return max;
    }

    @Test
    public void test11() {
        Item item = getLenByString("11111abccba12345677654321a");
        System.out.println(item.str + " " + item.len);
    }

    public static Item getLenByString(String s) {
        Item item = new Item();
        if (s != null && !s.equals("")) {
            for (int i = 0; i < s.length(); i++) {
                Item item1 = getLenForABCBA(s, i);
                if (item1.len > item.len) {
                    item = item1;
                }
                Item item2 = getLenForABCCBA(s, i);
                if (item2.len > item.len) {
                    item = item2;
                }
            }
        }
        return item;
    }

    final static public class Item {
        int len;
        String str;
    }

    public static Item getLenForABCBA(String s, int idx) {
        int i = 0;
        int start = 0;
        int end = 0;
        for (; i < s.length(); i++) {
            start = idx - i;
            end = idx + i;
            if (start >= 0 && end < s.length()
                    && s.charAt(start) == s.charAt(end)) {
            } else {
                break;
            }
        }
        Item item = new Item();
        item.str = s.substring(start + 1, end);
        item.len = 1 + i * 2;
        return item;
    }

    public static Item getLenForABCCBA(String s, int idx) {
        int start = 0;
        int end = 0;
        int i = 0;
        for (; i < s.length(); i++) {
            start = idx - i;
            end = idx + 1 + i;
            if (start >= 0 && end < s.length()
                    && s.charAt(start) == s.charAt(end)) {
            } else {
                break;
            }
        }
        Item item = new Item();
        item.str = s.substring(start + 1, end);
        item.len = item.str.length();
        return item;
    }

}
