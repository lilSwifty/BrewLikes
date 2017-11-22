package com.iths.manisedighi.brewlikes;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity {

    private Context context = MainActivity.this;

    public CameraActivity cameraActivity;


    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupBottomNavigation();
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        logo = findViewById(R.id.logoImageView);
    }

    /**
     * A method that sets up the bottom navigation
     */
    private void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.manipulateBottomNavigation(bottomNavigationViewEx);
        BottomNavigationHelper.activateBottomNavigation(context, bottomNavigationViewEx);

    }

    /**
     * Upper toolbar with icons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.about_and_camera_icons, menu);
        return true;
    }

    /**
     * Handles what happens when the icons in the toolbar are clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.aboutIcon){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;

        } else if(id == R.id.cameraIcon){
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}






