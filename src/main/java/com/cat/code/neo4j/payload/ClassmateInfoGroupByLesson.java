package com.cat.code.neo4j.payload;

import com.cat.code.neo4j.domain.Student;

import java.util.List;

public class ClassmateInfoGroupByLesson {
    /**
     * 课程名称
     */
    private String lessonName;

    /**
     * 学生信息
     */
    private List<Student> students;

    public ClassmateInfoGroupByLesson() {
    }

    public ClassmateInfoGroupByLesson(String lessonName, List<Student> students) {
        this.lessonName = lessonName;
        this.students = students;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
