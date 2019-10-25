package com.yukino.myapplication;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class LoginActivity extends Local {

    // private String account;
    //set layout page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
    }
    //link to the local passage
    public void Local (View Local){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,Local.class);
        account = intent.getStringExtra("account");
        startActivity(intent);
    }
    /*public void GPS (View gps){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,BaiDuMapActivity.class);
        account = intent.getStringExtra("account");
        startActivity(intent);
    }*/
    //link to the result page
    public void result (View result){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,result.class);
        account = intent.getStringExtra("account");
        startActivity(intent);
    }
    public void course (View course){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,teacher_course.class);
        startActivity(intent);
    }

    public void password (View pass){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,password.class);
        account = intent.getStringExtra("account");
        startActivity(intent);
    }
}
