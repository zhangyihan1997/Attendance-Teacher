package com.yukino.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CheckBox;
import android.content.SharedPreferences;
import android.content.Intent;
import com.yukino.http.UserHttpController;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.location.LocationManager;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 0;

//    private UserLoginTask mAuthTask = null;
    EditText mText;
    private LocationManager lm;
    private static final String TAG = "MainActivity";
    String bestProvider;
    String provider;
    private EditText mNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private EditText name, pass;
    private CheckBox rememberPass;
    private SharedPreferences sp;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String account;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rememberPass = (CheckBox) findViewById(R.id.ck);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass = (CheckBox) findViewById(R.id.ck);
        mNameView = (EditText) findViewById(R.id.name);
        //populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.pass);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        boolean isRemember = preferences.getBoolean("remember_password", false);
        if (isRemember) {
            account = preferences.getString("name", "");
            password = preferences.getString("password", "");
            mNameView.setText(account);
            mPasswordView.setText(password);
            rememberPass.setChecked(true);
        }

        //登录按钮
        Button mNameSignInButton = (Button) findViewById(R.id.sign_in_buttom);
        mNameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                account = mNameView.getText().toString();
                password = mPasswordView.getText().toString();
                UserHttpController.UserCheck(account, password, new UserHttpController.UserHttpControllerListener() {
                    @Override
                    public void success() {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void fail() {
                        Toast.makeText(MainActivity.this, "wrong, please input right password",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, null);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    private void attemptLogin() {
        mNameView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.。
        String student_id = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        editor = preferences.edit();
        if (rememberPass.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("Name", student_id);
            editor.putString("password", password);
        } else {
            editor.clear();
        }
        editor.apply();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.如果用户输入密码，请检查有效的密码。
    }

}