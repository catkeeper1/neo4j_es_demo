package com.cat.code.neo4j.domain;

import com.cat.code.neo4j.constants.NeoConsts;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;

@Node("Student")
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 学生姓名
     */
    @Property
    private String name;

    /**
     * 学生选的所有课程
     */
    @Relationship(NeoConsts.R_LESSON_OF_STUDENT)
    private List<Lesson> lessons;

    /**
     * 学生所在班级
     */
    @Relationship(NeoConsts.R_STUDENT_OF_CLASS)
    private java.lang.Class clazz;

    public Student(String s, ArrayList<Lesson> newArrayList, Class seven) {

    }
}
