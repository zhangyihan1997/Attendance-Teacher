package com.yukino.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;



public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
    }
    public void Timetable (View time){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,TimeTable.class);
        startActivity(intent);
    }
    public void Local (View Local){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,Local.class);
        startActivity(intent);
    }
    public void GPS (View gps){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,BaiDuMapActivity.class);
        startActivity(intent);
    }

}
