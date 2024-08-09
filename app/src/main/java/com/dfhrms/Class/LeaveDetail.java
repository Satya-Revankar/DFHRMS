package com.dfhrms.Class;

public class LeaveDetail {
	
	//private variables
	int _id;
	String Leave_Id;
	String Name;
	String email;
	String Program;
	String Employee_Id;
	String Opening_Balance;
	String CasualLeave;
	String CLCarryForward;
	String SickLeave;
	String SLCarryForward;
	String AvailedLeaves;
	String manager_email;
	String From_Date;
	String To_Date;
	String Reason;
	String LEave_Status;
	String Approved_On;
	String no_days;
	String Brought_Forword;

	String From_DateC;
	String To_DateC;
	// Empty constructor
	public LeaveDetail(){
		
	}

	// constructor
	public LeaveDetail(int id, String Leave_Id, String Name , String email, String Program, String Employee_Id, String Opening_Balance, String AvailedLeaves, String manager_email, String From_Date, String To_Date, String Reason, String LEave_Status, String Approved_On, String no_days, String Brought_Forword, String From_Datec, String To_Datec, String casualleave, String clcarryforward, String sickleave, String slcarryforward){
		this._id = id;
		this.Leave_Id = Leave_Id;
		this.Name = Name;
		this.email = email;
		this.Program = Program;
		this.Employee_Id = Employee_Id;
		this.Opening_Balance = Opening_Balance;
		this.AvailedLeaves = AvailedLeaves;
		this.manager_email = manager_email;
		this.From_Date = From_Date;
		this.To_Date = To_Date;
		this.Reason = Reason;
		this.LEave_Status = LEave_Status;
		this.Approved_On = Approved_On;
		this.no_days = no_days;
		this.Brought_Forword = Brought_Forword;
		this.From_DateC = From_Datec;
		this.To_DateC=To_Datec;

		this.CasualLeave=casualleave;
		this.CLCarryForward=clcarryforward;
		this.SickLeave=sickleave;
		this.SLCarryForward=slcarryforward;



	}
	
	// constructor
	public LeaveDetail(String Leave_Id, String Name , String email, String Program, String Employee_Id, String Opening_Balance, String AvailedLeaves, String manager_email, String From_Date, String To_Date, String Reason, String LEave_Status, String Approved_On, String no_days, String Brought_Forword, String From_Datec, String To_Datec, String casualleave, String clcarryforward, String sickleave, String slcarryforward){
		
		this.Leave_Id = Leave_Id;
		this.Name = Name;
		this.email = email;
		this.Program = Program;
		this.Employee_Id = Employee_Id;
		this.Opening_Balance = Opening_Balance;
		this.AvailedLeaves = AvailedLeaves;
		this.manager_email = manager_email;
		this.From_Date = From_Date;
		this.To_Date = To_Date;
		this.Reason = Reason;
		this.LEave_Status = LEave_Status;
		this.Approved_On = Approved_On;
		this.no_days = no_days;
		this.Brought_Forword = Brought_Forword;
		
		this.From_DateC = From_Datec;

		this.To_DateC=To_Datec;

		this.CasualLeave=casualleave;
		this.CLCarryForward=clcarryforward;
		this.SickLeave=sickleave;
		this.SLCarryForward=slcarryforward;
	}
	
	
	public String getLeave_Id(){
		return this.Leave_Id;
	}
	
	// setting id
	public void setLeave_Id(String Leave_Id){
		this.Leave_Id = Leave_Id;
	}
	
	
	// getting ID
	public String getName(){
		return this.Name;
	}
	
	// setting id
	public void setName(String Name){
		this.Name = Name;
	}
	
	// getting lead_id
	public String getemail(){
		return this.email;
	}
	
	// setting lead_id
	public void setemail(String email){
		this.email = email;
	}
	
	
	// setting phone number
	
	
	public String getProgram(){
		return this.Program;
	}
	public void setProgram(String Program){
		this.Program = Program;
	}
	
	public String getEmployee_Id(){
		return this.Employee_Id;
	}
	public void setEmployee_Id(String Employee_Id){
		this.Employee_Id = Employee_Id;
	}
	
	
	public String getOpening_Balance(){
		return this.Opening_Balance;
	}
	public void setOpening_Balance(String Opening_Balance){
		this.Opening_Balance = Opening_Balance;
	}
	public String getAvailedLeaves(){
		return this.AvailedLeaves;
	}
	public void setAvailedLeaves(String AvailedLeaves){
		this.AvailedLeaves = AvailedLeaves;
	}
	public String getmanager_email(){
		return this.manager_email;
	}
	public void setmanager_email(String manager_email){
		this.manager_email = manager_email;
	}
	public String getFrom_Date(){  
		return this.From_Date;
	}
	public void setFrom_Date(String From_Date){
		this.From_Date = From_Date;
	}
	public String getTo_Date(){  
		return this.To_Date;
	}
	public void setTo_Date(String To_Date){
		this.To_Date = To_Date;
	}
	public String getReason(){  
		return this.Reason;
	}
	public void setReason(String Reason){
		this.Reason = Reason;
	}
	
	public String getLEave_Status(){  
		return this.LEave_Status;
	}
	public void setLEave_Status(String LEave_Status){
		this.LEave_Status = LEave_Status;
	}
	public String getApproved_On(){  
		return this.Approved_On;
	}
	public void setApproved_On(String Approved_On){
		this.Approved_On = Approved_On;
	}
	
	
	public String getno_days(){  
		return this.no_days;
	}
	public void setno_days(String no_days){
		this.no_days = no_days;
	}


	public String getBrought_Forword(){
		return this.Brought_Forword;
	}
	public void setBrought_Forword(String Brought_Forword){
		this.Brought_Forword = Brought_Forword;
	}

	public String getFrom_DateC(){
		return this.From_DateC;
	}

	public void setFrom_DateC(String From_Datec){
		this.From_DateC = From_Datec;
	}

	public String getTo_DateC(){
		return this.To_DateC;
	}
	public void setTo_DateC(String To_Datec){
		this.To_DateC = To_Datec;
	}


	public String getCasualLeave()
	{ return this.CasualLeave; }
	public void setCasualLeave(String casualLeave)
	{this.CasualLeave=casualLeave;}


	public String getCLCarryForward()
	{ return this.CLCarryForward; }
	public void setCLCarryForward(String clcarryforward)
	{this.CLCarryForward=clcarryforward;}


	public String getSickLeave()
	{return this.SickLeave;}
	public void setSickLeave(String sickleave)
	{this.SickLeave=sickleave;}

	public String getSLCarryForward()
	{return this.getSLCarryForward();}
	public void setSLCarryForward(String slcarryforward)
	{this.SLCarryForward=slcarryforward;}



	/*public String toString()
	{
	    return( this.project_title );
	}*/

}
