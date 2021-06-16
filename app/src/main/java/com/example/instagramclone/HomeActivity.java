package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    TextView mUsernameTextView;
    TextView mEmailTextView;

    ParseUser mcurrentuser= ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //init textViews
        mUsernameTextView=(TextView) findViewById(R.id.TextViewUsername);
        mEmailTextView=(TextView) findViewById(R.id.TextViewEmail);


        //Initialising and displaying info
        mUsernameTextView.setText(mcurrentuser.getUsername());
        mEmailTextView.setText(mcurrentuser.getEmail());

    }

    public void logout(View view){
        ((Button) findViewById(R.id.logout)).setClickable(false);
        Toast.makeText(getApplicationContext(),"Logging Out",Toast.LENGTH_SHORT).show();
        mcurrentuser.logOut();
        Intent intent=new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
    }
    public void logout(){
        mcurrentuser.logOut();
        Intent intent=new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
    }
}