package com.yukino.model;
//the teacher model
public class Teacher {
    private int teacher_id;
    private String password;

    public Teacher(int teacher_id, String password) {
        this.teacher_id = teacher_id;
        this.password = password;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
