package com.dfhrms.Class;

public class Class_attendanceclassTaken
{
    /*EmpAttendance=anyType{Id=0; Date=2022-03-12; InTime=13:01:34; Outtime=13:01:39;
    Attendance=Class Completed; Status=false; };
    EmpAttendance=anyType{Id=0; Date=2022-03-12;
    InTime=16:47:12; Outtime=16:47:16; Attendance=Class Completed; Status=false; };
    EmpAttendance=anyType{Id=0; Date=2022-03-12; InTime=16:47:18; Outtime= ; Attendance=Class Started;
    Status=false; }; };*/

    String str_id;
    String str_date;
    String str_classtakenon;
    String str_classcompletedon;

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public String getStr_date() {
        return str_date;
    }

    public void setStr_date(String str_date) {
        this.str_date = str_date;
    }

    public String getStr_classtakenon() {
        return str_classtakenon;
    }

    public void setStr_classtakenon(String str_classtakenon) {
        this.str_classtakenon = str_classtakenon;
    }

    public String getStr_classcompletedon() {
        return str_classcompletedon;
    }

    public void setStr_classcompletedon(String str_classcompletedon) {
        this.str_classcompletedon = str_classcompletedon;
    }
}
