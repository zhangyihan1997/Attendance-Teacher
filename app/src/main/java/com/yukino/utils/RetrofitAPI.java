package com.yukino.utils;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface RetrofitAPI {

    @FormUrlEncoded
    @POST("user/teacher")//check teacher id and password method
    Call<String> checkTeacher(@HeaderMap Map<String, String> headers, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @PUT("location/teacher")//add teacher location method
    Call<String> addTeacherLocation(@HeaderMap Map<String, String> headers, @FieldMap Map<String, String> params);

    @GET("location/student")//get student location method
    Call<String> getStudentLocation(@HeaderMap Map<String, String> headers);

    @GET("result/check")// get student attendance result method
    Call<String> GetResult(@HeaderMap Map<String, String> headers);

    @GET("course/check")// get student attendance result method
    Call<String> GetCourse(@HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @PUT("password/teacher")// get student attendance result method
    Call<String> changeteacherpassword(@HeaderMap Map<String, String> headers, @FieldMap Map<String, String> params);

    @GET("download/Image1")
    Call<String> getImageDetails(@HeaderMap Map<String, String> headers);
}
