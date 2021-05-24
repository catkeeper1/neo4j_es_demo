package com.cat.code.neo4j.domain;

import com.cat.code.neo4j.constants.NeoConsts;
import org.springframework.data.neo4j.core.schema.*;

@Node("Class")
public class Class {
    /**
     * 主键
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 班级名称
     */
    @Property
    private String name;

    /**
     * 班级的班主任
     */
    @Relationship(NeoConsts.R_BOSS_OF_CLASS)
    private Teacher boss;

    public Class() {
    }

    public Class(String name, Teacher boss) {
        this.name = name;
        this.boss = boss;
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

    public Teacher getBoss() {
        return boss;
    }

    public void setBoss(Teacher boss) {
        this.boss = boss;
    }
}
