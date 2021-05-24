package com.cat.code.neo4j.domain;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Person")
public class PersonEntity {
    // 图库id
    @Id
    @GeneratedValue
    private Long id;
    // 人名
    @Property
    private String name;
    // 性别
    @Property
    private String sex;

    /**
     * type 关系名称
     * direction 关系方向
     * INCOMING 指向自己
     * OUTGOING 指向对方
     * UNDIRECTED 无向
     */
//    @Relationship(type = "FRIEND", direction = Relationship.Direction.OUTGOING)
//    private Set<PersonEntity> friends;
    public PersonEntity() {
    }

    public PersonEntity(String name, String sex) {
        this.name = name;
        this.sex = sex;
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

//    public Set<PersonEntity> getFriends() {
//        return friends;
//    }
//
//    public void setFriends(Set<PersonEntity> friends) {
//        this.friends = friends;
//    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "PersonEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
