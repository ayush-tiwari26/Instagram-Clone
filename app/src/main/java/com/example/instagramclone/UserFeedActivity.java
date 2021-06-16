package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {


    //giving the share menu on title bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflator=getMenuInflater();
        mMenuInflator.inflate(R.menu.share_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.share){
            share();
        }else if(item.getItemId()==R.id.myaccount){
            home();
        }else if(item.getItemId()==R.id.logoutMenu){
            logout();
        }else if(item.getItemId()==R.id.userlistMenu){
            backuserList();
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed_activity);

        //setting activity tile as username
        Intent intent=getIntent();
        String username=intent.getStringExtra("username");
        setTitle(username+"'s feed");

        LinearLayout mLinearlayout =(LinearLayout) findViewById(R.id.linearLayoutUserFeed);

//        if(mLinearlayout == null ) {
//            //creating a Image view programmitically
//            ImageView mImageView = new ImageView(getApplicationContext());
//            mImageView.setLayoutParams(
//                    new ViewGroup.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT));
//
//            mImageView.setImageResource(R.drawable.instalogo);
//
//            //adding to linear layout
//            mLinearlayout.addView(mImageView);
//        }

        //Querying database for this user
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Image");
//        query.whereEqualTo("username",username);

        //finding

        Toast.makeText(getApplicationContext(),"Showing all user's posts", (int) 5000).show();

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.i("Image","Finding Query Done");
                if(e==null){
                    //now download all files
                    if(objects.size()>0){
                        for(ParseObject obj:objects){
                            //saving and showing image
                            ParseFile file=(ParseFile) obj.get("image");
                            Log.i("Image","Download started");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    Log.i("Image","Download Done");
                                    //creating bitmap and image view
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);

                                    //creating a Image view programmitically
                                    ImageView mImageView = new ImageView(getApplicationContext());
                                    mImageView.setLayoutParams(
                                            new ViewGroup.LayoutParams(
                                                    1000,
                                                    200));
                                    mImageView.setImageBitmap(bitmap);
                                    //adding to linear layout
                                    mLinearlayout.addView(mImageView);
                                    TextView mCaption=new TextView(getApplicationContext());
                                    mCaption.setLayoutParams(
                                                new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT));
                                    mCaption.setText( obj.getString("caption"));
                                    mCaption.setGravity(Gravity.CENTER);
                                    mLinearlayout.addView(mCaption);
                                    Log.i("Image","Success");
                                }
                            });
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Nothing to view here",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Log.i("Image","ERROR");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Some Error loading data, contact admin(Ayush)",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void backuserList(View view) {
        Intent intent=new Intent(UserFeedActivity.this,UserListActivity.class);
        startActivity(intent);
    }

    public void home(View view) {
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }
    public void home() {
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }

    public void share(View view){
        Intent intent=new Intent(UserFeedActivity.this,SharePicture.class);
        startActivity(intent);
    }
    public void share(){
        Intent intent=new Intent(UserFeedActivity.this,SharePicture.class);
        startActivity(intent);
    }
    public void logout(){
        ParseUser.getCurrentUser().logOut();
        Intent intent=new Intent(UserFeedActivity.this,MainActivity.class);
        startActivity(intent);
    }
    public void backuserList() {
        Intent intent=new Intent(UserFeedActivity.this,UserListActivity.class);
        startActivity(intent);
    }
}