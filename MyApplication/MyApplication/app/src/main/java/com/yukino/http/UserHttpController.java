package com.yukino.http;

import android.util.Log;
import com.yukino.utils.RetrofitAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserHttpController {
    public interface UserHttpControllerListener {
        void success();

        void fail();
    }

    public static void UserCheck(String student_id, String password, UserHttpControllerListener userHttpControllerListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.105.203:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        header.put("Content-Type", "text/plain");
        body.put("student_id", student_id);
        body.put("password", password);
        Call<String> studentCall = service.checkUser(header, body);

        studentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String returnJson = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(returnJson);

                        if(jsonObject.getBoolean("status")) {
                            userHttpControllerListener.success();
                        } else {
                            Log.i("call failed", "diu lei lou mou");
                            userHttpControllerListener.fail();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("call failed", "Stack");
                        userHttpControllerListener.fail();
                    }
                } else {
                    Log.i("call failed", response.body());
                    userHttpControllerListener.fail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.i("call failed", throwable.getMessage());
                userHttpControllerListener.fail();
            }
        });
    }
}
