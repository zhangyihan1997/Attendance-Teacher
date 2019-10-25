package com.yukino.utils;

import com.yukino.model.Student;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("user/check")
    Call<String> checkUser(@HeaderMap Map<String, String> headers, @FieldMap Map<String, String> params);
}
