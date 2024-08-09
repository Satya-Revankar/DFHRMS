package com.dfhrms.Fragment;

/**
 * Created by User on 7/19/2017.
 */

public class OndutyhistoryClass {


    int _id;
    String Location;
    String Days;
    String Status;
    String TypeofOD;
    String Description;
    String FromDate;
    String ToDate;
    String ODApprovedOn;


    public OndutyhistoryClass(){}


    public OndutyhistoryClass(int id, String location, String days, String status, String typeofod, String description, String fromdate, String todate, String odapprovedon) {

        this._id = id;
        this.Location = location;
        this.Days=days;
        this.Status = status;
        this.TypeofOD=typeofod;
        this.Description = description;
        this.FromDate=fromdate;
        this.ToDate=todate;
        this.ODApprovedOn=odapprovedon;
    }

    public OndutyhistoryClass(String location, String days, String status, String typeofod, String description, String fromdate, String todate, String odapprovedon)
    {
        this.Location = location;
        this.Days=days;
        this.Status = status;
        this.TypeofOD=typeofod;
        this.Description =description;
        this.FromDate=fromdate;
        this.ToDate=todate;
        this.ODApprovedOn=odapprovedon;
    }

    public String getLocation(){
        return this.Location;
    }
    // setting id
    public void setLocation(String location){
        this.Location = location;
    }

    public String getDays(){
        return this.Days;
    }
    public void setDays(String days){
        this.Days = days;
    }


    public String getStatus(){
        return this.Status;
    }
    public void setStatus(String status){
        this.Status = status;
    }

    public String getTypeofOD(){
        return this.TypeofOD;
    }
    public void setTypeofOD(String typeofOD){
        this.TypeofOD = typeofOD;
    }

    public String getDescription(){
        return this.Description;
    }
    public void setDescription(String description){
        this.Description = description;
    }




    public String getFromDate(){
        return this.FromDate;
    }
    public void setFromDate(String fromdate){
        this.FromDate = fromdate;
    }


    public String getToDate(){
        return this.ToDate;
    }
    public void setToDate(String todate){
        this.ToDate = todate;
    }

    public String getODApprovedOn(){
        return this.ODApprovedOn;
    }
    public void setODApprovedOn(String odapprovedon){
        this.ODApprovedOn =odapprovedon;
    }










}//End o OndutyhistoryClasss
