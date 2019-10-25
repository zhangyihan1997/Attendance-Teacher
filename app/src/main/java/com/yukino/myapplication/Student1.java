package com.yukino.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class Student1 extends AppCompatActivity {
    ImageView StudentImg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student1);

        StudentImg1 = findViewById(R.id.studentImg1);
        Glide.with(Student1.this)
                .load("http://attendance.uiccst.com/img/1.jpg")
                .into(StudentImg1);

    }
}
