package com.dfhrms.SpinnerModels;

public class AbsentReason_SM {

    private String reasonId;
    private String reasonName;

    public AbsentReason_SM(String reasonId, String reasonName) {
        this.reasonId = reasonId;
        this.reasonName = reasonName;
    }

    public String getReasonId(){
        return reasonId;
    }
    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }
    public String getReasonName(){
        return reasonName;
    }
    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

}