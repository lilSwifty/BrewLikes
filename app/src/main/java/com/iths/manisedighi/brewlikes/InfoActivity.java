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

    private Toolbar toolbar;

    private ImageView logo;
    private ImageView ivBeer;
    private ImageView ivLocation;

    private BitmapHelper bitmapHelper;

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
    private int caller;

    private AlertDialog dialog;
    private EditText etInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started.");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_test2);
        startInitialize();
    }

    /**
     * A method that triggers the setup of InfoActivity
     */
    private void startInitialize(){
        Log.d(TAG, "init: starts the initializing");
        findViews();
        enableScrollFunction();
        setupBottomNavigation();
        getSpecificBeer();
        setupBeerInfo();
        addSharePhotoFragment();
        setupLogo();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * A method that finds all the views and binds them to the code
     */
    private void findViews(){
        Log.d(TAG, "findViews: get's all the views");
        ivBeer = findViewById(R.id.ivBeer);
        ivLocation = findViewById(R.id.ivLocation);                     
        logo = findViewById(R.id.logoImageView);
        tvBeerName = findViewById(R.id.tvBeerName);
        tvCategory = findViewById(R.id.tvCategory);
        tvPriceScore = findViewById(R.id.tvPriceScore);
        tvTasteScore = findViewById(R.id.tvTasteScore);
        tvRateScore = findViewById(R.id.tvRateScore);
        tvInfo = findViewById(R.id.tvInfo);
        tvLocation = findViewById(R.id.tvLocation);
        toolbar = findViewById(R.id.toolbarTop);
    }

    /**
     * A method that handles what hapens when the logo get's clicked
     */
    private void setupLogo(){
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
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
     * A method that receives the Id for a specific beer and
     * puts it's information in an instance of an Beer object.
     */
    private void getSpecificBeer(){
        Intent intent = getIntent();
        id = intent.getLongExtra("BeerID", 0);
        caller = intent.getIntExtra("info",0);
        helper = new DBHelper(context);
        beer = helper.getBeerById(id);
    }

    /**
     * A method to show all the information about the specific beer
     */
    private void setupBeerInfo(){
        Log.d(TAG, "setupInfoView: setting up all the necessary information about the beer");

        Bitmap image = BitmapFactory.decodeFile(beer.getPhotoPath());

        ivBeer.setImageBitmap(image);

        ivBeer.setAdjustViewBounds(true);
        ivBeer.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tvBeerName.setText(beer.getName());
        tvCategory.setText(beer.getCategoryName());
        int newPrice = ((int) beer.getPrice());
        int newTaste = ((int) beer.getTaste());
        tvPriceScore.setText(getResources().getString(R.string.price)+" "+newPrice);
        tvTasteScore.setText(getResources().getString(R.string.taste)+" "+newTaste);
        tvRateScore.setText(getResources().getString(R.string.rate)+" "+String.valueOf(beer.getAverage()));
        tvInfo.setText(beer.getComment());
        if(beer.getLocation()==null) {
            ivLocation.setVisibility(View.GONE);
        }else{
           tvLocation.setText(beer.getLocation());
        }
    }

    /**
     * A method that shows a bigger beer picture
     * @param view - the picture being clicked
     */
    public void onBeerImageClick(View view){
        Log.d(TAG, "onBeerImageClick: true");
        Intent intent = new Intent(context, ShowBigBeerActivity.class);
        intent.putExtra("photoPath", beer.getPhotoPath());
        startActivity(intent);
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
     * A method that enables edit for the beer comment, shown in an AlertDialog
     * New comment gets saved into the database
     */
    public void onEditClick(){
        Log.d(TAG, "onEditClick: edit clicked.");
        makeAlertDialog(getResources().getString(R.string.edit), null,
                getResources().getString(R.string.cancel));
        etInfo = new EditText(this);
        etInfo.setElevation(0);
        dialog.setView(etInfo);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.saveBeer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                beer.setComment(etInfo.getText().toString());
                helper.editBeer(beer.getId(), beer.getComment());
                tvInfo.setText(beer.getComment());
            }
        });
        etInfo.setText(beer.getComment());
        dialog.show();
    }

    /**
     * A method that takes help from the DBHelper to removes all
     * the information about a specific beer that the user wants to delete
     * and then checks for the activity who triggered InfoActivity
     */
    public void onDeleteClick(){
        Log.d(TAG, "onDeleteClick: clicked");
        makeAlertDialog(getResources().getString(R.string.delete), getResources().getString(R.string.deleting),
                getResources().getString(R.string.no));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.removeBeer(id);
                checkCaller();
            }
        });
        dialog.show();
    }

    /**
     * A method that helps to create an AlertDialog
     * @param title - the Title of the dialog
     * @param message - the message of the dialog
     * @param button - the text on the button
     */
    private void makeAlertDialog(String title, String message, String button){
        dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setIcon(R.drawable.brewlikes_main_image);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * A method that checks which activity that triggered InfoActivity
     * and takes us back to that activity after deleting the specific beer
     * if it was RankingActivity, we want the user to go back to MainActivity
     */
    private void checkCaller(){
        switch(caller){
            case 1:
                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                break;
            case 2:
                Intent categoriesIntent = new Intent(context, CategoriesActivity.class);
                categoriesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(categoriesIntent);
                break;
            case 3:
                Intent topListIntent = new Intent(context, TopListActivity.class);
                topListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(topListIntent);
                break;
        }
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
    }
}