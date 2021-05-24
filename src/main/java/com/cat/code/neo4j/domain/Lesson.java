package com.cat.code.neo4j.domain;

import com.cat.code.neo4j.constants.NeoConsts;
import org.springframework.data.neo4j.core.schema.*;

@Node("Lesson")
public class Lesson {
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 课程名称
     */
    @Property
    private String name;

    /**
     * 任教老师
     */
    @Relationship(NeoConsts.R_TEACHER_OF_LESSON)
    private Teacher teacher;

    public Lesson(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher;
    }

    public Lesson() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
