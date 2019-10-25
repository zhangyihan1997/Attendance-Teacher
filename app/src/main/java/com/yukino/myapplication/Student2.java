package com.yukino.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class Student2 extends AppCompatActivity {
    ImageView StudentImg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student2);

        StudentImg2 = findViewById(R.id.studentImg2);
        Glide.with(Student2.this)
                .load("http://attendance.uiccst.com/img/1630003010.jpg")
                .into(StudentImg2);

    }
}
