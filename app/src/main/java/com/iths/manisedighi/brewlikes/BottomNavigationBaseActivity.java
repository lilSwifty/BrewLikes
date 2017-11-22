package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by emmapersson on 2017-11-22.
 */

public class BottomNavigationBaseActivity extends AppCompatActivity {

    /**
     * A method that sets up the bottom navigation
     */
    protected void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        manipulateBottomNavigation(bottomNavigationViewEx);
        activateBottomNavigation(this, bottomNavigationViewEx);
    }

    /**
     * A static method that when called manipulates some of
     * the settings for the bottom navigation
     * @param view - the BottomNavigationViewEx to manipulate
     */
    public void manipulateBottomNavigation(BottomNavigationViewEx view){
        view.enableAnimation(false);
        view.enableShiftingMode(false);
        view.enableItemShiftingMode(false);
        view.setTextVisibility(true);
    }

    /**
     * A static method that handles clicks on the different icons in the navigation view
     * @param context - from where this method being called
     * @param view - the BottomNavigationViewEx to handle
     */
    public void activateBottomNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.ic_home:
                        item.setChecked(true);
                        Intent mainIntent = new Intent(context, MainActivity.class);
                        context.startActivity(mainIntent);
                        break;
                    case R.id.ic_category:
                        item.setChecked(true);
//                        Intent categoryIntent = new Intent(context, CategoryActivity.class);
//                        context.startActivity(categoryIntent);
                        break;
                    case R.id.ic_toplist:
                        item.setChecked(true);
//                        Intent topListIntent = new Intent(context, ToplistActivity.class);
//                        context.startActivity(topListIntent);
                        break;
                    case R.id.ic_mapview:
                        item.setChecked(true);
//                        Intent mapViewIntent = new Intent(context, MapviewActivity.class);
//                        context.startActivity(mapViewIntent);
                        break;
                }
                return false;
            }
        });
    }
}
