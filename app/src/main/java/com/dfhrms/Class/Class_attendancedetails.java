package com.dfhrms.Class;

public class Class_attendancedetails
{
    /* anyType{Status=true; Message=Success; list=anyType{EmployeeDayAttendance=anyType{Id=13;
            Employee_Id=116; Name=Parashuram; Punched_At=27-01-2022 10:24; lat=15.371038; lon=75.123451;
            Location_Code=DCSE Building; };*/

    String str_id;
    String str_empid;
    String str_employeename;
    String str_punchedinDateTime;
    String str_latitude;
    String str_longitude;
    String str_location;

    public String getStr_id() {
        return str_id;
    }

    public void setStr_id(String str_id) {
        this.str_id = str_id;
    }

    public String getStr_empid() {
        return str_empid;
    }

    public void setStr_empid(String str_empid) {
        this.str_empid = str_empid;
    }

    public String getStr_employeename() {
        return str_employeename;
    }

    public void setStr_employeename(String str_employeename) {
        this.str_employeename = str_employeename;
    }

    public String getStr_punchedinDateTime() {
        return str_punchedinDateTime;
    }

    public void setStr_punchedinDateTime(String str_punchedinDateTime) {
        this.str_punchedinDateTime = str_punchedinDateTime;
    }

    public String getStr_latitude() {
        return str_latitude;
    }

    public void setStr_latitude(String str_latitude) {
        this.str_latitude = str_latitude;
    }

    public String getStr_longitude() {
        return str_longitude;
    }

    public void setStr_longitude(String str_longitude) {
        this.str_longitude = str_longitude;
    }

    public String getStr_location() {
        return str_location;
    }

    public void setStr_location(String str_location) {
        this.str_location = str_location;
    }
}
