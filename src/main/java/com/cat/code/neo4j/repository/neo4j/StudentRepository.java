package com.cat.code.neo4j.repository.neo4j;

import com.cat.code.neo4j.domain.Student;
import com.cat.code.neo4j.payload.ClassmateInfoGroupByLesson;
import com.cat.code.neo4j.payload.TeacherStudent;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends Neo4jRepository<Student, Long> {

    /**
     * 根据班级查询班级人数
     *
     * @param className 班级名称
     * @return 班级人数
     */
    @Query("MATCH (s:Student)-[r:R_STUDENT_OF_CLASS]->(c:Class{name:{className}}) return count(s)")
    Long countByClassName(@Param("className") String className);


    /**
     * 查询满足 (学生)-[选课关系]-(课程)-[选课关系]-(学生) 关系的 同学
     *
     * @return 返回同学关系
     */
    @Query("match (s:Student)-[:R_LESSON_OF_STUDENT]->(l:Lesson)<-[:R_LESSON_OF_STUDENT]-(:Student) with l.name as lessonName,collect(distinct s) as students return lessonName,students")
    List<ClassmateInfoGroupByLesson> findByClassmateGroupByLesson();

    /**
     * 查询师生关系，(学生)-[班级学生关系]-(班级)-[班主任关系]-(教师)
     *
     * @return 返回师生关系
     */
    @Query("match (s:Student)-[:R_STUDENT_OF_CLASS]->(:Class)-[:R_BOSS_OF_CLASS]->(t:Teacher) with t.name as teacherName,collect(distinct s) as students return teacherName,students")
    List<TeacherStudent> findTeacherStudentByClass();

    /**
     * 查询师生关系，(学生)-[选课关系]-(课程)-[任教老师关系]-(教师)
     *
     * @return 返回师生关系
     */
    @Query("match ((s:Student)-[:R_LESSON_OF_STUDENT]->(:Lesson)-[:R_TEACHER_OF_LESSON]->(t:Teacher))with t.name as teacherName,collect(distinct s) as students return teacherName,students")
    List<TeacherStudent> findTeacherStudentByLesson();
}
