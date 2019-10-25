package com.yukino.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class Student3 extends AppCompatActivity {
    ImageView StudentImg3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student3);

        StudentImg3 = findViewById(R.id.studentImg3);
        Glide.with(Student3.this)
                .load("http://attendance.uiccst.com/img/1630003020.jpg")
                .into(StudentImg3);

    }
}
