package com.yukino.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class Student4 extends AppCompatActivity {
    ImageView StudentImg4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student4);

        StudentImg4 = findViewById(R.id.studentImg4);
        Glide.with(Student4.this)
                .load("http://attendance.uiccst.com/img/1630003064.jpg")
                .into(StudentImg4);

    }
}
