package com.cat.code.neo4j.payload;


import com.cat.code.neo4j.domain.Student;

import java.util.List;

public class TeacherStudent {
    /**
     * 教师姓名
     */
    private String teacherName;

    /**
     * 学生信息
     */
    private List<Student> students;

    public TeacherStudent() {
    }

    public TeacherStudent(String teacherName, List<Student> students) {
        this.teacherName = teacherName;
        this.students = students;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
