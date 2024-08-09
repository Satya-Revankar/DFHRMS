package com.dfhrms.Class;

/*<vmleaveSummary>
<Leave_Type>1</Leave_Type>
<Leaves_Type>CL</Leaves_Type>
<Opening_Balance>10</Opening_Balance>
<carry_forward>0</carry_forward>
<carry_forward_Year>NA</carry_forward_Year>
<Leaves_Taken>12.5</Leaves_Taken>
<Balance>-2.5</Balance>
<Status>Success</Status>
</vmleaveSummary>*/


public class Class_LeaveBalanceSummary
{
    int _id;
    String LeavesType;
    String LeaveBalanceType;
    String CarryForward;
    String LeavesTaken;
    String TotalBalance;
    String Status;





    public Class_LeaveBalanceSummary(){

    }

    // constructor
    public Class_LeaveBalanceSummary(int id, String leavestype, String leavebalancetype , String carryforwad, String leavestaken, String totalbalance, String status){
        this._id = id;
        this.LeavesType = leavestype;
        this.LeaveBalanceType = leavebalancetype;
        this.CarryForward = carryforwad;
        this.LeavesTaken =leavestaken;
        this.TotalBalance=totalbalance;
        this.Status=status;

    }

    // constructor
    public Class_LeaveBalanceSummary(String leavestype, String leavebalancetype , String carryforwad, String leavestaken, String totalbalance, String status){


        this.LeavesType = leavestype;
        this.LeaveBalanceType = leavebalancetype;
        this.CarryForward = carryforwad;
        this.LeavesTaken =leavestaken;
        this.TotalBalance=totalbalance;
        this.Status=status;
    }


    public String getLeavesType(){
        return this.LeavesType;
    }

    // setting id
    public void setLeavesType(String leavestype){
        this.LeavesType = leavestype;
    }

    public String getLeaveBalanceType(){
        return this.LeaveBalanceType;
    }

    // setting id
    public void setLeaveBalanceType(String leaveBalanceType){
        this.LeaveBalanceType = leaveBalanceType;
    }




    public String getCarryForward(){
        return this.CarryForward;
    }

    // setting id
    public void setCarryForward(String carryforward){
        this.CarryForward = carryforward;
    }





    public String getLeavesTaken(){
        return this.LeavesTaken;
    }

    // setting id
    public void setLeavesTaken(String leavestaken){
        this.LeavesTaken = leavestaken;
    }




    public String getTotalBalance(){
        return this.TotalBalance;
    }

    // setting id
    public void setTotalBalance(String totalbalance){
        this.TotalBalance =totalbalance;
    }





    public String getStatus(){
        return this.Status;
    }

    // setting id
    public void setStatus(String status){
        this.Status =status;
    }

}
