package com.dfhrms.Class;


/*
<vmLeave_Type>
<Id>6</Id>
<Leave_Type>CL / SL</Leave_Type>
</vmLeave_Type>
<vmLeave_Type>
<Id>5</Id>
<Leave_Type>Earned Leave</Leave_Type>
</vmLeave_Type>
<vmLeave_Type>
<Id>4</Id>
<Leave_Type>Paternity Leave</Leave_Type>
</vmLeave_Type>


<ArrayOfVmLeave_Type>
<vmLeave_Type><Id>6</Id>
<Leave_Type>CL / SL</Leave_Type>
</vmLeave_Type><vmLeave_Type>
<Id>5</Id>
<Leave_Type>Earned Leave</Leave_Type>
</vmLeave_Type>
<vmLeave_Type>
<Id>3</Id>
<Leave_Type>Maternity Leave</Leave_Type>
</vmLeave_Type>
<vmLeave_Type>
<Id>7</Id>
<Leave_Type>Miscarriage Leave</Leave_Type>
</vmLeave_Type>


*/

public class Class_LeaveTypeOnGender
{

        String Leave_Type;
        String Leave_Type_ID;


    public String getleavetype() {
        return Leave_Type;
    }

    public void setleavetype(String leave_type) {
        Leave_Type = leave_type;
    }

    public String getleavetype_ID() {
        return Leave_Type_ID;
    }

    public void setleavetype_ID(String leave_type_id) {
        Leave_Type_ID = leave_type_id;
    }

    public String toString()
    {
        return( this.Leave_Type );
    }
}
