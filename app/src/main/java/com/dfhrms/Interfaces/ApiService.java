package com.dfhrms.Interfaces;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("attendanceimagecapturejson")
    Call<Void> Post_studentimages(@Body JsonObject jsonObject);
}