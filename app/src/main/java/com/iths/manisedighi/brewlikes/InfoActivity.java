package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Emma on 2017-11-15.
 */
public class InfoActivity extends AppCompatActivity {

    private Context context = InfoActivity.this;

    private ImageView ivBeer;
    private ImageView ivNavBack;
    private ImageView ivShare;
    private ImageView ivEdit;
    private ImageView ivLocation;

    private TextView tvBeerName;
    private TextView tvCategory;
    private TextView tvPriceScore;
    private TextView tvTasteScore;
    private TextView tvRateScore;
    private TextView tvInfo;
    private TextView tvLocation;

    private Beer beer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //find and bind all the widgets to the code
        findViews();
        //for the tvInfo
        enableScrollFunction();
        //for the bottom navigation
        setupBottomNavigation();
        beer = new Beer();
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
     * finds all the widgets and binds the to the code
     */
    private void findViews(){
        ivBeer = findViewById(R.id.ivBeer);
        ivNavBack = findViewById(R.id.ivNavBack);
        ivShare = findViewById(R.id.ivShare);
        ivEdit = findViewById(R.id.ivEdit);
        ivLocation = findViewById(R.id.ivLocation);
        tvBeerName = findViewById(R.id.tvBeerName);
        tvCategory = findViewById(R.id.tvCategory);
        tvPriceScore = findViewById(R.id.tvPriceScore);
        tvTasteScore = findViewById(R.id.tvTasteScore);
        tvRateScore = findViewById(R.id.tvRateScore);
        tvInfo = findViewById(R.id.tvInfo);
        tvLocation = findViewById(R.id.tvLocation);
    }

    /**
     * a method that enables the scroll function in the TextView tvInfo
     */
    private void enableScrollFunction(){
        tvInfo.setMovementMethod(new ScrollingMovementMethod());
    }

    private void onNavBackClick(View view){
        ivNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, Categories.class);
                startActivity(intent);*/
            }
        });
    }

    private void onShareClick(View view){
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO here comes the upload to facebook
            }
        });
    }

    private void onEditClick(View view){
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO what happens here? Back to Ranking or a new view?

            }
        });
    }

    private void onLocationClick(View view){
        ivLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO takes you to map view
            }
        });
    }


}
