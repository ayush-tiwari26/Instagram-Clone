package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
public class MainActivity extends AppCompatActivity {

    static EditText mUsenameEditText;
    static EditText mPasswordEditText;

    ConstraintLayout mBackGroundConstraitLayout;

    //showing user list and taking current user data
    public void movetoUserList(){
        Intent intent =new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // if user is logged in
        if(ParseUser.getCurrentUser()!=null){
            movetoUserList();
        }

        //Init all edit texts
        mUsenameEditText=(EditText) findViewById(R.id.editTextUsernameLogin);
        mPasswordEditText=(EditText) findViewById(R.id.editTextPasswordLogin);

        //setting the click anywhere keyboard disappear;
        mBackGroundConstraitLayout=(ConstraintLayout) findViewById(R.id.mainActivityConstraintLayoutMainActivity);
        mBackGroundConstraitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mUsenameEditText.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
            }
        });


        //if called after signup
        Intent intent=getIntent();
        String username=intent.getStringExtra("username");
        String password=intent.getStringExtra("password");
        mUsenameEditText.setText(username);
        mPasswordEditText.setText(password);

        //setting on enter login
        mPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN) {
                    login(findViewById(R.id.login_mainActivity_btn));
                }

                return false;
            }
        });
        //done setting

    }

    public void login(View view){
        ParseUser.logInInBackground(mUsenameEditText.getText().toString(), mPasswordEditText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                //if login successful
                if(e == null){
                    Toast.makeText(getApplicationContext(),"Logged in as "+user.getUsername(),Toast.LENGTH_SHORT);

                    //starting user list activity
                    movetoUserList();
                }else{
                    Toast.makeText(getApplicationContext(),"Error Loging in ",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public void signup(View view){
        Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}