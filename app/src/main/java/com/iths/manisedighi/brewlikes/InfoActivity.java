package com.iths.manisedighi.brewlikes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

    private ImageView ivBeer;
    private ImageView ivLocation;                       //

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
        setContentView(R.layout.activity_info3);
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * A method that finds all the views and binds them to the code
     */
    private void findViews(){
        Log.d(TAG, "findViews: get's all the views");
        ivBeer = findViewById(R.id.ivBeer);
        ivLocation = findViewById(R.id.ivLocation);                         //
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

        //Bitmap image = BitmapFactory.decodeFile(beer.getPhotoPath(), BitmapOptions().isSampleSize);

        //ivBeer.setImageBitmap(image);

        ivBeer.setImageBitmap(
                bitmapHelper.decodeSampledBitmapFromFile(beer.getPhotoPath(), 100, 100));

        tvBeerName.setText(beer.getName());
        tvCategory.setText(beer.getCategoryName());
        tvPriceScore.setText(String.valueOf(beer.getPrice()));
        tvTasteScore.setText(String.valueOf(beer.getTaste()));
        tvRateScore.setText(String.valueOf(beer.getAverage()+"/10.0"));
        tvInfo.setText(beer.getComment());
        if(beer.getLocation()==null) {
            ivLocation.setVisibility(View.GONE);
        }else{
           tvLocation.setText(beer.getLocation());
        }

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
        dialog = new AlertDialog.Builder(this).create();
        etInfo = new EditText(this);
        etInfo.setElevation(0);
        dialog.setTitle(getResources().getString(R.string.edit));
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
     * A method that removes all the information about
     * a specific beer that the user wants to delete
     * and sends the user back to the activity that started InfoActivity
     */
    public void onDeleteClick(){
        //TODO sätt stringvalues på Yes, No och Messsage
        Log.d(TAG, "onDeleteClick: clicked");
        dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getResources().getString(R.string.delete));
        dialog.setMessage("Do you want to continue deleting?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.removeBeer(id);
                switch(caller){
                    case 1:
                        Intent rankingIntent = new Intent(context, RankingActivity.class);
                        startActivity(rankingIntent);
                        break;
                    case 2:
                        Intent categoriesIntent = new Intent(context, CategoriesActivity.class);
                        startActivity(categoriesIntent);
                        break;
                    case 3:
                        Intent topListIntent = new Intent(context, TopListActivity.class);
                        startActivity(topListIntent);
                        break;
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
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