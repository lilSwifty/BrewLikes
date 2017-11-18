package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //for the bottom navigation
        setupBottomNavigation();
        findViews();
        setupInfoView();
    }

    /**
     * A method that sets up the bottom navigation
     */
    public void setupBottomNavigation(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);
        BottomNavigationHelper.manipulateBottomNavigation(bottomNavigationViewEx);
        BottomNavigationHelper.activateBottomNavigation(context, bottomNavigationViewEx);
    }

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

    public void setupInfoView(){
        tvInfo.setMovementMethod(new ScrollingMovementMethod());
    }
}
