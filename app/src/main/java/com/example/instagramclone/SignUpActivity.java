package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Parse
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends AppCompatActivity {

    EditText mUsenameEditText;
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mConfirmPasswordEditText;

    ConstraintLayout mBackGroundConstraitLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //setting the click anywhere keyboard disappear;
        mBackGroundConstraitLayout=(ConstraintLayout) findViewById(R.id.constraintLayoutSignUpActivity);
        mBackGroundConstraitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mUsenameEditText.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(mEmailEditText.getWindowToken(), 0);
                mgr.hideSoftInputFromWindow(mConfirmPasswordEditText.getWindowToken(), 0);
            }
        });

        //Init all edit texts
        mUsenameEditText=(EditText) findViewById(R.id.editTextUsernameSignup);
        mEmailEditText=(EditText) findViewById(R.id.editTextEmailSignup);
        mPasswordEditText=(EditText) findViewById(R.id.editTextPasswordSignup);
        mConfirmPasswordEditText=(EditText) findViewById(R.id.editTextConfirmPasswordSignup);

        //setting on enter signup
        mConfirmPasswordEditText.setOnKeyListener((v, keyCode, event) -> {

            if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN) {
                signup(findViewById(R.id.signup_signupActivity_btn));
            }

            return false;
        });

    }

    //login btn click listner
    public void login(View view){
        Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    //signup button click listner, processes parse server for sign up request
    public void signup(View view){

        //avoiding multiple signup calls
        ((Button) findViewById(R.id.signup_signupActivity_btn)).setClickable(false);
        Toast.makeText(getApplicationContext(),"Signing Up",Toast.LENGTH_SHORT).show();

        if(mConfirmPasswordEditText.getText().toString().equals(mPasswordEditText.getText().toString())) {
            //Registering new user
            ParseUser mparseuser = new ParseUser();
            mparseuser.setUsername(mUsenameEditText.getText().toString());
            mparseuser.setEmail(mEmailEditText.getText().toString());
            mparseuser.setPassword(mPasswordEditText.getText().toString());

            //as soon as user is registersd and signed up
            mparseuser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                    {
                        Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();

                        //Switching to main activity
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.putExtra("username", mUsenameEditText.getText().toString());
                        intent.putExtra("password", mPasswordEditText.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                    else{
                        switch (e.getCode()) {
                            case ParseException.USERNAME_TAKEN: {
                                // report error
                                Toast.makeText(getApplicationContext(), "Username Already Taken", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case ParseException.EMAIL_TAKEN: {
                                // report error
                                Toast.makeText(getApplicationContext(), "Email Already Taken", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            default: {
                                // Something else went wrong
                                Toast.makeText(getApplicationContext(), "Error Signing Up, contact admin", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"Passwords does not match",Toast.LENGTH_SHORT).show();
        }

        ((Button) findViewById(R.id.signup_signupActivity_btn)).setClickable(true);
    }
}