package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Emma on 2017-11-15.
 */
public class InfoActivity extends BottomNavigationBaseActivity {

    private static final String TAG = "InfoActivity";
    private Context context = InfoActivity.this;

    private ImageView ivBeer;
    private ImageView ivNavBack;
    private ImageView ivShare;
    private ImageView ivEdit;
    private ImageView ivLocation;
    private ImageView ivSave;

    private TextView tvBeerName;
    private TextView tvCategory;
    private TextView tvPriceScore;
    private TextView tvTasteScore;
    private TextView tvRateScore;
    private TextView tvInfo;
    private TextView tvLocation;

    private EditText etInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //find and bind all the widgets to the code
        findViews();
        //for the tvInfo
        enableScrollFunction();
        //for the bottom navigation
        setupBottomNavigation();
        //to set up info about the beer
        setupInfoView();
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
        ivSave = findViewById(R.id.ivSave);
        tvBeerName = findViewById(R.id.tvBeerName);
        tvCategory = findViewById(R.id.tvCategory);
        tvPriceScore = findViewById(R.id.tvPriceScore);
        tvTasteScore = findViewById(R.id.tvTasteScore);
        tvRateScore = findViewById(R.id.tvRateScore);
        tvInfo = findViewById(R.id.tvInfo);
        tvLocation = findViewById(R.id.tvLocation);
        etInfo = findViewById(R.id.etInfo);
    }

    /**
     * A method to show all the necessary information about a beer
     *
     */
    private void setupInfoView(){
        Log.d(TAG, "setupInfoView: setting up all the necessary information about the beer");
        etInfo.setVisibility(View.GONE);
        ivSave.setVisibility(View.GONE);
        //TODO set up the info about the beer, takes info out from database
    }


    /**
     * a method that enables the scroll function in the TextView tvInfo
     */
    private void enableScrollFunction(){
        Log.d(TAG, "enableScrollFunction: enables scrollfunction in tvInfo view");
        tvInfo.setMovementMethod(new ScrollingMovementMethod());
    }

    /**
     * A method that takes us back to CategoriesActivity
     * @param view - the view being clicked and calling the method
     */
    public void onNavBackClick(View view){
        Log.d(TAG, "onNavBackClick: nav back clicked");
//            Intent intent = new Intent(context, Categories.class);
//            startActivity(intent);
    }


    /**
     * A method that share picture on FaceBook
     * @param view - the view being clicked and calling the method
     */
    public void onShareClick(View view) {
        Log.d(TAG, "onShareClick: share clicked.");
        //TODO here comes the upload to facebook
    }

    /**
     * A method that edits the comment about a beer
     * @param view - the view being clicked and calling the method
     */
    public void onEditClick(View view){
        Log.d(TAG, "onEditClick: edit clicked.");
        tvInfo.setVisibility(View.GONE);
        etInfo.setVisibility(View.VISIBLE);
        ivEdit.setVisibility(View.GONE);
        ivLocation.setVisibility(View.GONE);
        tvLocation.setVisibility(View.GONE);
        ivSave.setVisibility(View.VISIBLE);
        //TODO get the comment about the beer to edit
    }

    /**
     * A method that saves the changed comment to the database
     * @param view - the view being clicked and calling the method
     */
    public void onSaveClick(View view){
        Log.d(TAG, "onSaveClick: save clicked.");
        tvInfo.setVisibility(View.VISIBLE);
        etInfo.setVisibility(View.GONE);
        ivEdit.setVisibility(View.VISIBLE);
        ivLocation.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.VISIBLE);
        ivSave.setVisibility(View.GONE);
        //TODO save down the new comment to the database
    }

    /**
     * A method that shows the beer in the map view
     * @param view - the view being clicked and calling the method
     */
    public void onLocationClick(View view){
        Log.d(TAG, "onLocationClick: location clicked.");
                //TODO takes you to map view
    }


}
