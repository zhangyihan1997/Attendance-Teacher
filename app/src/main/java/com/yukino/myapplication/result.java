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

import android.view.View.OnClickListener;


public class result extends AppCompatActivity {

    public TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = (TextView) findViewById((R.id.result));
        //use scroll bar
        result.setMovementMethod(ScrollingMovementMethod.getInstance());
        //use get result method to get all student attendance result in database
        UserHttpController.GetResult(result, new UserHttpController.UserHttpControllerListener() {
            SpannableString testText = new SpannableString("JOJO");

            @Override
            public void success() {
                Toast.makeText(result.this, "succeed",Toast.LENGTH_SHORT).show();
                //set to text to get all info in the database
                result.setText(testText, TextView.BufferType.SPANNABLE);
            }

            @Override
            public void fail() {
                Toast.makeText(result.this, "wrong",Toast.LENGTH_SHORT).show();
            }
        });

        Button mNameSignInButton1 = (Button) findViewById(R.id.sign_in_buttom1);

        //use a button to jump to baidu map page
        mNameSignInButton1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                        Intent intent = new Intent(result.this, BaiDuMapActivity.class);
                        startActivity(intent);

            }
        });

        Button checkPhoto = (Button) findViewById(R.id.CheckPhoto);
        checkPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(result.this, List.class);
                startActivity(intent);
            }
        });
    }
}

