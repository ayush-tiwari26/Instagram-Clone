package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;


//The class that shows the staging activity before sharing a picture to parse
public class SharePicture extends AppCompatActivity {

    EditText mCaptionEditText;
    ImageView mImage;
    ImageView mRotateBtn;
    Bitmap mImgBitmap;

    ProgressBar mProgressBar;

    //starts intent for photo selection and gives callback
    public void getPhoto(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //staring the process of image selection from gallery
        startActivityForResult(intent,1);
        Log.i("Image","Bitmap");
        // 1 is the request code
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_picture);

        ///init
        mCaptionEditText=(EditText) findViewById(R.id.editTextCaption_share_image);
        mImage=(ImageView) findViewById(R.id.imageViewPictureSelected_share_picture);
        mRotateBtn=(ImageView) findViewById(R.id.floatingActionButtonRotate);
        mRotateBtn.setVisibility(View.INVISIBLE);
        mProgressBar=(ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setAlpha(0);

        //getting pic without permissions
        getPhoto();

        //if permissions not already given
//        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
//        }
//        else{
//            getPhoto();
//        }

    }

//    //after the permission is granteffd/accepted
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode==1)
//        getPhoto();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            Uri uri=data.getData();
            //generating image
            try {
                mImgBitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                Log.i("Image","Bitmap");
                mImage.setImageBitmap(mImgBitmap);
                mRotateBtn.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Something went wrong while retreving image Bitmap",Toast.LENGTH_SHORT).show();
            }
    }

    //Does the work to save the user image in parse server
    public void postImage(View view){

        ((Button) findViewById(R.id.PostImageSelectedBtn_share_picture)).setClickable(false);


        try {
            Log.i("Image","BUTTON CLICKED POST");
            //saving
            savingImage();
            //Create byte array output stream
            ByteArrayOutputStream mByteOutputStream = new ByteArrayOutputStream();
            //putting bitmap of image in stream buffer
            mImgBitmap.compress(Bitmap.CompressFormat.PNG, 100, mByteOutputStream);
            byte[] mByteArray = mByteOutputStream.toByteArray();
            Log.i("Image","Check Point 1");
            //Converting to parse file
            ParseFile mParseFile = new ParseFile("image.png", mByteArray);
            Log.i("Image","Check Point 2");
            //new parse object and class(if not already exists)
            ParseObject mParseObject = new ParseObject("Image");
            mParseObject.put("image", mParseFile);
            mParseObject.put("username", ParseUser.getCurrentUser().getUsername());
            mParseObject.put("caption", mCaptionEditText.getText().toString());
            Log.i("Image","Check Point 3");

            mParseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("Image","Check Point 4");
                        Toast.makeText(getApplicationContext(), "Posted Image", Toast.LENGTH_LONG).show();
                        // going back to user list
                        Intent intent = new Intent(SharePicture.this, UserListActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.i("Image ERROR", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Image could not be posted, try again and please contact admin(Ayush)", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "Image could not be posted, try again and please contact admin(Ayush)", Toast.LENGTH_LONG).show();
        }
        ((Button) findViewById(R.id.PostImageSelectedBtn_share_picture)).setClickable(true);
    }

    public void rotateImage(View view) {
        mImage.animate().rotationBy(90f).setDuration(1000);
        mRotateBtn.animate().rotationBy(90f).setDuration(1000);
    }

    public void savingImage(){
        mProgressBar.animate().alpha(1).setDuration(1000);
    }

}