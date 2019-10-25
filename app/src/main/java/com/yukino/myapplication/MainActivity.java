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
import android.content.Context;



public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 0;

    EditText mText;
    private LocationManager lm;
    private static final String TAG = "MainActivity";
    private EditText mNameView;
    private EditText mPasswordView;
    private CheckBox rememberPass;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public static String account;
    public static String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link to main page
        setContentView(R.layout.activity_main);
        //store userinfo in the sp
        preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        rememberPass = (CheckBox) findViewById(R.id.ck);
        mNameView = (EditText) findViewById(R.id.name);
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
        //check the remember password box in click or not
        boolean isRemember = preferences.getBoolean("remember_password", false);
        //if click pass the info store in sp to the cureent id and password
        if (isRemember) {
            account = preferences.getString("name", "");
            password = preferences.getString("password", "");
            mNameView.setText(account);
            mPasswordView.setText(password);
            rememberPass.setChecked(true);
        }
        account = preferences.getString("name", "");
        password = preferences.getString("password", "");
        mNameView.setText(account);
        mPasswordView.setText(password);



        //Login button
        Button mNameSignInButton = (Button) findViewById(R.id.sign_in_buttom);
        mNameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //get id to account
                account = mNameView.getText().toString();
                //get password
                password = mPasswordView.getText().toString();
                //use usercheck method to check the id and password whether stored in database
                UserHttpController.UserCheck(account, password, new UserHttpController.UserHttpControllerListener() {
                    @Override
                    public void success() {
                        editor = preferences.edit();
                        if(rememberPass.isChecked()){
                            //get the id and password store in sp
                            editor.putBoolean("remember_password",true);
                            editor.putString("name",account);
                            editor.putString("password",password);
                        }
                        else{
                            editor.clear();
                        }
                        editor.apply();
                        //data.putString(account, account);
                        //if is correct, jump to loginactivity page
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                        intent.putExtra("account", account);
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
        String teacher_id = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        editor = preferences.edit();
        if (rememberPass.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("Name", teacher_id);
            editor.putString("password", password);
        } else {
            editor.clear();
        }
        editor.apply();

    }

}