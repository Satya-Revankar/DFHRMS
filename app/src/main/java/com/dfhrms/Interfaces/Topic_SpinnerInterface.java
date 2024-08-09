package com.dfhrms.Interfaces;

import com.dfhrms.Class.Class_URL;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Topic_SpinnerInterface {
    //http://testingrdp.dfindia.org:9000/api/skillattendance/gettopicmainlist?Employee_Id=943&Subject_Id=1
    String Topic_URL = Class_URL.API_URL;
    //http://testingrdp.dfindia.org:9000/api/skillattendance/GetCohortSubjects
    //http://testingrdp.dfindia.org:9000/api/skillattendance/GetCohortSubjects?Location_Name=DET Hubli&Employee_Id=943
    @GET("gettopicmainlist")
    public Call<String> getJSONString(@Query("Employee_Id") String Employee_Id,
                                      @Query("Subject_Id") String Subject_Id);

}
