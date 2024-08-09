package com.dfhrms.SpinnerModels;

public class CohSub_SM {

    private String cohort, subject, subjectId, classMode_Id, classMode_Name, classMode_Type;

    public String getCohort(){
        return cohort;
    }
    public void setCohort(String cohort) {
        this.cohort = cohort;
    }
    public String getSubject(){
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String SubjectId) {
        this.subjectId = SubjectId;
    }

    public String getClassMode_Id() {
        return classMode_Id;
    }

    public void setClassMode_Id(String classMode_Id) {
        this.classMode_Id = classMode_Id;
    }

    public String getClassMode_Name() {
        return classMode_Name;
    }

    public void setClassMode_Name(String classMode_Name) {
        this.classMode_Name = classMode_Name;
    }
    public String getClassMode_Type() {
        return classMode_Type;
    }

    public void setClassMode_Type(String classMode_Type) {
        this.classMode_Type = classMode_Type;
    }
}