package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
    private ImageView logo;


    private TextView tvBeerName;
    private TextView tvCategory;
    private TextView tvPriceScore;
    private TextView tvTasteScore;
    private TextView tvRateScore;
    private TextView tvInfo;
    private TextView tvLocation;

    private Beer beer;
    private DBHelper helper;
    private Long id;

    private AlertDialog dialog;
    private EditText etInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info3);
        init();
    }

    /**
     * A method that triggers the setup of InfoActivity
     */
    private void init(){
        Log.d(TAG, "init: starts the initializing");
        //find and bind all the views to the code
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
        tvBeerName = findViewById(R.id.tvBeerName);
        tvCategory = findViewById(R.id.tvCategory);
        tvPriceScore = findViewById(R.id.tvPriceScore);
        tvTasteScore = findViewById(R.id.tvTasteScore);
        tvRateScore = findViewById(R.id.tvRateScore);
        tvInfo = findViewById(R.id.tvInfo);
        tvLocation = findViewById(R.id.tvLocation);
    }

    /**
     * Upper toolbar with icons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.info_activity_menu, menu);
        return true;
    }

    /**
     * Handles what happens when the icons in the toolbar are clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.ic_camera:
                Intent cameraIntent = new Intent(this, RankingActivity.class);
                startActivity(cameraIntent);
                break;
            case R.id.ic_edit:
                onEditClick();
                break;
            case R.id.ic_delete:
                onDeleteClick();
                break;
        }return super.onOptionsItemSelected(item);
    }

    /**
     * A method to show all the necessary information about a beer
     */
    private void setupInfoView(){
        Log.d(TAG, "setupInfoView: setting up all the necessary information about the beer");

        Intent intent = getIntent();
        id = intent.getLongExtra("BeerID", 0);
        helper = new DBHelper(context);
        beer = helper.getBeerById(id);

        Bitmap image = BitmapFactory.decodeFile(beer.getPhotoPath());

        ivBeer.setImageBitmap(image);

        tvBeerName.setText(beer.getName());
        tvCategory.setText(beer.getCategoryName());
        tvPriceScore.setText(String.valueOf(beer.getPrice()));
        tvTasteScore.setText(String.valueOf(beer.getTaste()));
        tvRateScore.setText(String.valueOf(beer.getAverage()+"/10.0"));
        tvInfo.setText(beer.getComment());
        tvLocation.setText(beer.getLocation());
        //TODO set up the info about the beer, takes info out from database
    }

    /**
     * A method that adds the fragment SharePhotoFragment
     * to the layout and place it in the btnShare view
     */
    private void addSharePhotoFragment(){
        Log.d(TAG, "addSharePhotoFragment: adds the SharePhotoFragment");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("photoPath", beer.getPhotoPath());
        SharePhotoFragment spf = new SharePhotoFragment();
        spf.setArguments(bundle);
        fragmentTransaction.add(R.id.btnShare, spf);
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
     * A method that takes us back to the previous Activity
     * @param view - the view being clicked and calling the method
     */
    public void onNavBackClick(View view){
        Log.d(TAG, "onNavBackClick: nav back clicked");
        onBackPressed();
    }

    /**
     * A method that shows an AlertDialog to edit the comment
     * about a beer in and saves the new comment
     */
    public void onEditClick(){
        Log.d(TAG, "onEditClick: edit clicked.");
        dialog = new AlertDialog.Builder(this).create();
        etInfo = new EditText(this);
        etInfo.setElevation(0);
        etInfo.setTextColor(getResources().getColor(R.color.beer));
        dialog.setTitle("Edit comment");
        dialog.setView(etInfo);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                beer.setComment(etInfo.getText().toString());
                tvInfo.setText(beer.getComment());
            }
        });
        etInfo.setText(beer.getComment());
        dialog.show();
        //TODO save the new comment to the database
    }

    /**
     * A method that removes all the information about
     * a specific beer that the user wants to delete
     */
    public void onDeleteClick(){
        dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("About to delete this beer");
        dialog.setMessage("Do you want to continue deleting?");
        dialog.setButton(DialogInterface.BUTTON_NEUTRAL,"Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.removeBeer(id);
                Intent intent = new Intent(context, TopListActivity.class);
                startActivity(intent);
                finish();
                //TODO fix so that it knows were be to go back (TopList or Category)
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * A method that shows on a map were the user checked in the specific beer
     * @param view - the view being clicked and calling the method
     */
    public void onLocationClick(View view){
        Log.d(TAG, "onLocationClick: location clicked.");
        Intent mapIntent = new Intent(InfoActivity.this, MapActivity.class);
        mapIntent.putExtra("beerId" , beer.getId());
        mapIntent.putExtra("ID", 2);
        startActivity(mapIntent);
        //TODO takes you to map view
    }
}