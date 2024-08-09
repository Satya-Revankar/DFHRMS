package com.dfhrms.Interfaces;

import com.dfhrms.Class.Class_URL;

import retrofit2.Call;
import retrofit2.http.GET;

public interface student_absent_reason_SI {
    //http://testingrdp.dfindia.org:9000/api/skillattendance/classnottakenreason
    String GetReasons_url = Class_URL.API_URL;

    @GET("absentreason")
    public Call<String> getJSONString(); // Method declaration with the correct annotation
}
