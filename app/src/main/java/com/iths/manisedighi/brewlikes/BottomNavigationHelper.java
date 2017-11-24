package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Emma on 2017-11-15.
 * A helper class to setup and enable the bottom navigation
 */

public class BottomNavigationHelper {

    /**
     * A static method that when called manipulates some of
     * the settings for the bottom navigation
     * @param view - the BottomNavigationViewEx to manipulate
     */
    public static void manipulateBottomNavigation(BottomNavigationViewEx view){
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
    public static void activateBottomNavigation(final Context context, BottomNavigationViewEx view){
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
                        Intent categoriesIntent = new Intent(context, CategoriesActivity.class);
                        context.startActivity(categoriesIntent);
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
