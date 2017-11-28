package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Emma on 2017-11-15.
 * An activity that shows a specific beer and it's information
 */
public class InfoActivity extends BottomNavigationBaseActivity {

    private static final String TAG = "InfoActivity";
    private Context context = InfoActivity.this;

    private ImageView ivBeer;
    private ImageView ivEdit;
    private ImageView ivLocation;
    private ImageView ivSave;
    private ImageView logo;


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
        //to add fragment to the layout
        addSharePhotoFragment();

        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        logo = findViewById(R.id.logoImageView);
        //Hides the BrewLikes text from the upper toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * A method that finds all the views and binds them to the code
     */
    private void findViews(){
        Log.d(TAG, "findViews: get's all the views");
        ivBeer = findViewById(R.id.ivBeer);
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
            Intent cameraIntent = new Intent(this, RankingActivity.class);
            startActivity(cameraIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A method to show all the necessary information about a beer
     */
    private void setupInfoView(){
        Log.d(TAG, "setupInfoView: setting up all the necessary information about the beer");
        etInfo.setVisibility(View.GONE);
        ivSave.setVisibility(View.GONE);
        /*ivBeer.setImageBitmap(?);
        tvBeerName.setText(beer.getName());
        tvCategory.setText(beer.getCategory());
        tvPriceScore.setText(String.valueOf(beer.getPrice()));
        tvTasteScore.setText(String.valueOf(beer.getTaste()));
        tvRateScore.setText(String.valueOf(beer.getAverage()+"/10.0"));
        tvInfo.setText(beer.getComment);
        tvLocation.setText(beer.getLocation());*/
        //TODO set up the info about the beer, takes info out from database
    }

    /**
     * A method that adds the fragment SharePhotoFragment to the layout and place it in the btnShare view
     */
    private void addSharePhotoFragment(){
        Log.d(TAG, "addSharePhotoFragment: adds SharePhotoFragment to btnShare");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.btnShare, new SharePhotoFragment());
        fragmentTransaction.commit();
    }

    /**
     * A method that enables the scroll function in the TextView tvInfo
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
        Intent intent = new Intent(context, CategoriesActivity.class);
        startActivity(intent);
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
        //etInfo.setText(beer.getComment());
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
        /*beer.setComment(etInfo.getText().toString());
        tvInfo.setText(etInfo.getText().toString());*/
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
