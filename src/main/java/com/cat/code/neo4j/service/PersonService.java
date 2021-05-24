package com.cat.code.neo4j.service;

import cn.hutool.core.util.StrUtil;
import com.cat.code.neo4j.domain.Class;
import com.cat.code.neo4j.domain.*;
import com.cat.code.neo4j.payload.ClassmateInfoGroupByLesson;
import com.cat.code.neo4j.payload.TeacherStudent;
import com.cat.code.neo4j.repository.neo4j.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.neo4j.cypherdsl.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.*;

import static org.neo4j.cypherdsl.core.Cypher.*;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ClassRepository classRepo;

    @Autowired
    private LessonRepository lessonRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    /**
     * 初始化数据
     */
    public void initData() {
        // 初始化老师
        Teacher akai = new Teacher("迈特凯");
        Teacher kakaxi = new Teacher("旗木卡卡西");
        Teacher zilaiye = new Teacher("自来也");
        Teacher gangshou = new Teacher("纲手");
        Teacher dashewan = new Teacher("大蛇丸");
        teacherRepo.save(akai);
        teacherRepo.save(kakaxi);
        teacherRepo.save(zilaiye);
        teacherRepo.save(gangshou);
        teacherRepo.save(dashewan);

        // 初始化课程
        Lesson tishu = new Lesson("体术", akai);
        Lesson huanshu = new Lesson("幻术", kakaxi);
        Lesson shoulijian = new Lesson("手里剑", kakaxi);
        Lesson luoxuanwan = new Lesson("螺旋丸", zilaiye);
        Lesson xianshu = new Lesson("仙术", zilaiye);
        Lesson yiliao = new Lesson("医疗", gangshou);
        Lesson zhouyin = new Lesson("咒印", dashewan);
        lessonRepo.save(tishu);
        lessonRepo.save(huanshu);
        lessonRepo.save(shoulijian);
        lessonRepo.save(luoxuanwan);
        lessonRepo.save(xianshu);
        lessonRepo.save(yiliao);
        lessonRepo.save(zhouyin);

        // 初始化班级
        Class three = new Class("第三班", akai);
        Class seven = new Class("第七班", kakaxi);
        classRepo.save(three);
        classRepo.save(seven);

        // 初始化学生
        List<Student> threeClass = Lists.newArrayList(new Student("漩涡鸣人", Lists.newArrayList(tishu, shoulijian, luoxuanwan, xianshu), seven), new Student("宇智波佐助", Lists.newArrayList(huanshu, zhouyin, shoulijian), seven), new Student("春野樱", Lists.newArrayList(tishu, yiliao, shoulijian), seven));
        List<Student> sevenClass = Lists.newArrayList(new Student("李洛克", Lists.newArrayList(tishu), three), new Student("日向宁次", Lists
                .newArrayList(tishu), three), new Student("天天", Lists.newArrayList(tishu), three));

        studentRepo.saveAll(threeClass);
        studentRepo.saveAll(sevenClass);

    }

    /**
     * 查询同学关系，根据课程
     *
     * @return 返回同学关系
     */
    public Map<String, List<Student>> findClassmatesGroupByLesson() {
        List<ClassmateInfoGroupByLesson> groupByLesson = studentRepo.findByClassmateGroupByLesson();
        Map<String, List<Student>> result = Maps.newHashMap();

        groupByLesson.forEach(classmateInfoGroupByLesson -> result.put(classmateInfoGroupByLesson.getLessonName(), classmateInfoGroupByLesson
                .getStudents()));

        return result;
    }

    /**
     * 查询全校学生数
     *
     * @return 学生总数
     */
    public Long studentCount(String className) {
        if (StrUtil.isBlank(className)) {
            return studentRepo.count();
        } else {
            return studentRepo.countByClassName(className);
        }
    }

    /**
     * 查询所有师生关系，包括班主任/学生，任课老师/学生
     *
     * @return 师生关系
     */
    public Map<String, Set<Student>> findTeacherStudent() {
        List<TeacherStudent> teacherStudentByClass = studentRepo.findTeacherStudentByClass();
        List<TeacherStudent> teacherStudentByLesson = studentRepo.findTeacherStudentByLesson();
        Map<String, Set<Student>> result = Maps.newHashMap();

        teacherStudentByClass.forEach(teacherStudent -> result.put(teacherStudent.getTeacherName(), Sets.newHashSet(teacherStudent
                .getStudents())));

        teacherStudentByLesson.forEach(teacherStudent -> result.put(teacherStudent.getTeacherName(), Sets.newHashSet(teacherStudent
                .getStudents())));

        return result;
    }

    /**
     * 添加事物处理
     */
    @Transactional
    public void savePerson() {
        PersonEntity person1 = new PersonEntity("云码", "女");
        PersonEntity person2 = new PersonEntity("猫仔", "男");
        PersonEntity person3 = new PersonEntity("木库", "男");
        PersonEntity person4 = new PersonEntity("莫莫", "男");

        person1 = personRepository.save(person1);
        person2 = personRepository.save(person2);
        person3 = personRepository.save(person3);
        person4 = personRepository.save(person4);

        Set<PersonEntity> set1 = new HashSet<>();
        set1.add(person2);
        set1.add(person3);
        //person1.setFriends(set1);

        Set<PersonEntity> set2 = new HashSet<>();
        set2.add(person1);
        set2.add(person3);
        set2.add(person4);
        //person2.setFriends(set2);

        Set<PersonEntity> set3 = new HashSet<>();
        set3.add(person1);
        set3.add(person2);
        set3.add(person4);
        //person3.setFriends(set3);

        Set<PersonEntity> set4 = new HashSet<>();
        set4.add(person2);
        set4.add(person3);
        //person4.setFriends(set4);
        PersonEntity personEntity = personRepository.save(person1);
        System.out.println("新增结果：" + personEntity.getId());
    }

    public void update() {
        PersonEntity personEntity = personRepository.findById(6L).get();
        personEntity.setName("zzd");
        personRepository.save(personEntity);
    }

    public void saveAndUpdatePerson() {
        // 查询节点
        List<PersonEntity> personEntityList = personRepository.findByName("木库");
        for (PersonEntity temp : personEntityList) {
            // 删除节点
            personRepository.deleteById(temp.getId());
        }

        PersonEntity person1 = new PersonEntity("云码", "女");
        PersonEntity person2 = new PersonEntity("猫仔", "男");
        PersonEntity person3 = new PersonEntity("木库", "男");
        Set<PersonEntity> set3 = new HashSet<>();
        set3.add(person1);
        set3.add(person2);
        //person3.setFriends(set3);

        PersonEntity personEntity = personRepository.save(person3);
    }

    public List<PersonEntity> findByName() {
        String name = "云码";
        List<PersonEntity> personEntityList = personRepository.findByName(name);
        personEntityList.forEach(System.out::println);
        return personEntityList;
    }

    public void deleteById() {
        personRepository.deleteById(0L);
    }

    @Transactional
    public void personTransactional() {
        PersonEntity person = new PersonEntity("猪爸爸", "男");
        personRepository.save(person);
        throw new RuntimeException("异常");
    }

    public void LikePerson() {
        List<PersonEntity> personEntityList = personRepository.findByNameLike("Tom");
        for (PersonEntity personEntity : personEntityList) {
            System.out.println("personEntity = " + personEntity.getId());
            System.out.println("personEntity = " + personEntity.getName());
        }
    }

    public void zzz() {
        String sql = "match( p1: Person {name:\"木库\"} )-[rel:FRIEND]->(p2) return p2.name as zzz , p2.sex as xxx";
        Node p1 = node("Person").withProperties("name", parameter("person1"));
        Node p2 = node("Person").withProperties("name", parameter("person2"));
        NamedPath shortestPath = shortestPath("p").definedBy(
                p1.relationshipBetween(p2).unbounded()
        );
        SymbolicName p = shortestPath.getRequiredSymbolicName();
        Statement statement = Cypher.match(shortestPath)
                .with(p, listWith(name("n"))
                        .in(Functions.nodes(shortestPath))
                        .where(anyNode().named("n").hasLabels("Movie")).returning().as("mn")
                )
                .unwind(name("mn")).as("m")
                .with(p, name("m"))
                .match(node("Person").named("d")
                        .relationshipTo(anyNode("m"), "DIRECTED").named("r")
                )
                .returning(p, Functions.collect(name("r")), Functions.collect(name("d")))
                .build();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("person1", "木库");
        parameters.put("person2", "to.getName()");
        List<PersonEntity> all = neo4jTemplate.findAll(statement, parameters, PersonEntity.class);
        for (PersonEntity personEntity : all) {
            System.out.println("personEntity = " + personEntity);
        }
    }

    public PersonEntity getPersonEntity() {
        return personRepository.findById(3L).get();
    }
}
