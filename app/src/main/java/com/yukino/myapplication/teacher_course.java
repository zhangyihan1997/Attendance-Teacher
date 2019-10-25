package com.yukino.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View;
import android.widget.Button;

import android.content.Intent;
import com.yukino.http.UserHttpController;




public class teacher_course extends AppCompatActivity {

    public TextView course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        course = (TextView) findViewById((R.id.course));
        //use scroll bar
        //use get result method to get all student attendance result in database

        UserHttpController.GetCourse(course, new UserHttpController.UserHttpControllerListener() {
            SpannableString testText = new SpannableString("JOJO");

            @Override
            public void success() {
                Toast.makeText(teacher_course.this, "succeed",Toast.LENGTH_SHORT).show();
                //set to text to get all info in the database
                course.setText(testText, TextView.BufferType.SPANNABLE);
            }

            @Override
            public void fail() {
                Toast.makeText(teacher_course.this, "wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }
}