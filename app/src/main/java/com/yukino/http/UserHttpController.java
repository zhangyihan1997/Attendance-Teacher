package com.yukino.http;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.yukino.myapplication.MainActivity;
import com.yukino.utils.RetrofitAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    //Check user id and password
    public static void UserCheck(String teacher_id, String password, UserHttpControllerListener userHttpControllerListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.105.203:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        //link to server and use API
        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        header.put("Content-Type", "application/json");
        body.put("teacher_id", teacher_id);
        body.put("password", password);
        //use check teacher method
        Call<String> teacherCall = service.checkTeacher(header, body);
        //check the server is link successful or not
        teacherCall.enqueue(new Callback<String>() {
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
    //send teacher longitude and latitude to the server database
    public static void SendTeacherLocation(String teacher_id, String Longitude, String Latitude, String time, UserHttpControllerListener userHttpControllerListener) {
        //connect to server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.105.203:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        header.put("Content-Type", "application/json");
        body.put("teacher_id", teacher_id);
        body.put("longitude", Longitude);
        body.put("latitude", Latitude);
        body.put("time", time);
        //use addteacherlocation method
        Call<String> teacherCall = service.addTeacherLocation(header, body);

        teacherCall.enqueue(new Callback<String>() {
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
                    Log.i("call failed", response.toString());
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

    //get the student attendance result from the server database
    public static void GetResult(TextView textView, UserHttpControllerListener userHttpControllerListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.105.203:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/plain");
        //use getresult method
        Call<String> resultCall = service.GetResult(header);

        resultCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    String returnJson = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(returnJson);
                        if(jsonObject.getBoolean("status")) {
                            JSONArray result = jsonObject.getJSONArray("attendance");
                            StringBuilder sb = new StringBuilder();
                            //get the result from database and add them to form a text
                            int student_id, attendance;
                            String time;
                            for (int i = 0; i < result.length(); i++) {
                                student_id = result.getJSONObject(i).getInt("student_id");
                                attendance = result.getJSONObject(i).getInt("attendance");
                                time = result.getJSONObject(i).getString("time");
                                if(attendance == 1){
                                    sb.append("student_id: " + student_id + "\n");
                                    sb.append("Attendance result: Attendance\n");
                                    sb.append("time:"+ time +"\n");
                                }else{
                                    sb.append("student_id: " + student_id + "\n");
                                    sb.append("Attendance result: Absent\n");
                                    sb.append("time:"+ time +"\n");
                                }
                            }
                            textView.setText(sb.toString());
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
                    Log.i("call failed", response.toString());
                    userHttpControllerListener.fail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.i("call failed", throwable.getMessage());
                //userHttpControllerListener.fail();
            }
        });
    }

    //get the student attendance result from the server database
    public static void GetCourse(TextView textView, UserHttpControllerListener userHttpControllerListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.105.203:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/plain");
        //use getresult method
        Call<String> courseCall = service.GetCourse(header);

        courseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    String returnJson = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(returnJson);
                        if(jsonObject.getBoolean("status")) {
                            JSONArray course = jsonObject.getJSONArray("course");
                            StringBuilder sb = new StringBuilder();
                            //get the result from database and add them to form a text
                            int teacher_id;
                            String classroom, coursetime;
                            for (int i = 0; i < course.length(); i++) {
                                teacher_id = course.getJSONObject(i).getInt("teacher_id");
                                String teacherid=String.valueOf(teacher_id);
                                if(MainActivity.account.equals(teacherid)) {
                                    classroom = course.getJSONObject(i).getString("classroom");
                                    coursetime = course.getJSONObject(i).getString("course");
                                    sb.append("classroom: " + classroom + "\n");
                                    sb.append("course:" + coursetime + "\n");
                                }
                                else{
                                    sb.append("no class\n");
                                }
                            }
                            textView.setText(sb.toString());
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
                    Log.i("call failed", response.toString());
                    userHttpControllerListener.fail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.i("call failed", throwable.getMessage());
                //userHttpControllerListener.fail();
            }
        });
    }

    public static void TeacherPassword(String teacher_id,  String password, UserHttpControllerListener userHttpControllerListener) {
        //connect to server
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.105.203:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        header.put("Content-Type", "application/json");
        body.put("teacher_id", teacher_id);
        body.put("password", password);
        //use addteacherlocation method
        Call<String> studentCall = service.changeteacherpassword(header, body);

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
                    Log.i("call failed", response.toString());
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

    public static void getImageDetails(UserHttpControllerListener userHttpControllerListener){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://47.102.105.203:8082")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "text/plain");
        Call<String> call = service.getImageDetails(header);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    String returnJson = response.body();
                    try {
                        JSONObject jsonObject = new JSONObject(returnJson);
                        if(jsonObject.getBoolean("status")) {
                            StringBuilder Uri = new StringBuilder();
                            String ImageUri;
                            ImageUri = jsonObject.getString("Uri");

                            Uri.append(ImageUri);
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
                    Log.i("call failed", response.toString());
                    userHttpControllerListener.fail();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.i("call failed", throwable.getMessage());
                //userHttpControllerListener.fail();
            }
        });
    }


}
