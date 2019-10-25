package com.yukino.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class List extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Button student1 = (Button) findViewById(R.id.list1);
        Button student2 = (Button) findViewById(R.id.list2);
        Button student3 = (Button) findViewById(R.id.list3);
        Button student4 = (Button) findViewById(R.id.list4);

        student1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this, Student1.class);
                startActivity(intent);
            }
        });

        student2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this, Student2.class);
                startActivity(intent);
            }
        });

        student3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this, Student3.class);
                startActivity(intent);
            }
        });

        student4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List.this, Student4.class);
                startActivity(intent);
            }
        });

    }
}
