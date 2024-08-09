package com.dfhrms.Interfaces;

import com.dfhrms.Class.Class_URL;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CohSub_SpinnerInterface {
    String CohSub_URL = Class_URL.API_URL;
    //http://testingrdp.dfindia.org:9000/api/skillattendance/GetCohortSubjects
    //http://testingrdp.dfindia.org:9000/api/skillattendance/GetCohortSubjects?Location_Name=DET Hubli&Employee_Id=943
    @GET("GetCohortSubjects")
    public Call<String> getJSONString(@Query("Location_Name") String location_name, @Query("Employee_Id") String employee_id);
}
