package com.iths.manisedighi.brewlikes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int RESULT_LOAD_IMAGE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        CameraLauncher();
    }

    //opens pop-up with multiple choices

     public void CameraLauncher() {
         AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
         builder.setMessage("Please choose an alternative").setCancelable(false)
                 .setPositiveButton("Take photo", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                         //Check if phone has a camera
                         if (intent.resolveActivity(getPackageManager()) != null) {
                             startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                         }
                     }
                 })
                 .setNegativeButton("Upload image", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         Intent i = new Intent(
                                 Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setTitle("Time to brew...");
                        alert.show();
     }

}


