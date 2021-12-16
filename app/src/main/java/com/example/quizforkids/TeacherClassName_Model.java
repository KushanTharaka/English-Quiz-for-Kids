package com.example.quizforkids;

public class TeacherClassName_Model {

    private String docName;
    private String ClassName;

    public TeacherClassName_Model(String docName, String className) {
        this.docName = docName;
        ClassName = className;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }
}
