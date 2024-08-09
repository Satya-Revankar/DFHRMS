//package ;
package com.dfhrms.Class;
public class AllTeamList {

	//private variables


	String id;

	String email;
	String employee_id;
    String employeeName;

	// Empty constructor
	public AllTeamList(){
		
	}
	// constructor
	
	
	// constructor
	
	
	public String getid(){
		return this.id;
	}
	public String getemail(){
		return this.email;
	}
	// setting id
	public void setid(String id){
		this.id = id;
	}
	public void setemail(String email){
		this.email = email;
	}
	
	// getting ID
		
		
	public String getemployee_id(){
		return this.employee_id;
	}
	public void setemployee_id(String employee_id){
		this.employee_id = employee_id;
	}

	public String getemployeeName(){
		return this.employeeName;
	}
	public void setemployeeName(String employeeName){
		this.employeeName = employeeName;
	}
	
	public String toString()
	{
	    return( this.employeeName );
	}
	
}