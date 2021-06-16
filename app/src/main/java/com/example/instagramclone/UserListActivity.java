package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_user_list);

        //Adapters and list
        ArrayList<String> usersList=new ArrayList<>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,usersList);


        //Setting list view work
        ListView userListView=(ListView) findViewById(R.id.userListView);



        //setting up query for logged in user
        ParseQuery<ParseUser> usersQuery=ParseUser.getQuery();

        //if user has already siggend in
        if(ParseUser.getCurrentUser()!=null) {
            Toast.makeText(getApplicationContext(),"Showing user list",Toast.LENGTH_SHORT).show();
            usersQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            usersQuery.orderByAscending("username");
            usersQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        Log.i("Users", objects.toString());
                        for (ParseUser user : objects) {
                            usersList.add(user.getUsername());
                            Log.i("Users", usersList.toString());
                        }
                        //updating arrayadapter
                        arrayAdapter.notifyDataSetChanged();
                        userListView.setAdapter(arrayAdapter);
                    } else {
                        e.printStackTrace();
                        Log.i("User", "ERROR");
                    }
                }
            });
            //setting user click
            userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //starting the user feed
                    Intent intent=new Intent(getApplicationContext(),UserFeedActivity.class);
                    intent.putExtra("username",usersList.get(position));
                    startActivity(intent);
                }
            });
        }
        //if user not logged in
        else{
            Intent intent=new Intent(UserListActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }



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
        Intent intent=new Intent(UserListActivity.this,SharePicture.class);
        startActivity(intent);
    }
    public void share(){
        Intent intent=new Intent(UserListActivity.this,SharePicture.class);
        startActivity(intent);
    }
    public void logout(){
        ParseUser.getCurrentUser().logOut();
        Intent intent=new Intent(UserListActivity.this,MainActivity.class);
        startActivity(intent);
    }
    public void backuserList() {
        Intent intent=new Intent(UserListActivity.this,UserListActivity.class);
        startActivity(intent);
    }

}