package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class CategoriesActivity extends AppCompatActivity {
    private Context context = CategoriesActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setupBottomNavigation();
    }

    /**
     * A method that sets up the bottom navigation
     */
    private void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.manipulateBottomNavigation(bottomNavigationViewEx);
        BottomNavigationHelper.activateBottomNavigation(context, bottomNavigationViewEx);
    }

}
