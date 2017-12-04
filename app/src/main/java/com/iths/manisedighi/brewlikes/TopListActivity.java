 package com.iths.manisedighi.brewlikes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

 public class TopListActivity extends BottomNavigationBaseActivity {

     private TopListCursorAdapter cursorAdapter;
     private ListView topListView;
     private DBHelper dbHelper = new DBHelper(this);
     ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        topListView = findViewById(R.id.beerTopList);

        //Copy and paste this toolbar to every activity!
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        logo = findViewById(R.id.logoImageView);
        //Hides the BrewLikes text from the upper toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        initialize();

        topListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("BeerID", id);
                startActivity(intent);
            }
        });
    }

     /**
      * Initializes the activity
      */
     private void initialize() {
         setupBottomNavigation();
         createCursorAdapter();
     }

     /**
      * Sets up the Cursor Adapter
      */
     private void createCursorAdapter() {
         Cursor cursor = dbHelper.getTopListCursor();
         cursorAdapter = new TopListCursorAdapter(this, cursor);
         topListView.setAdapter(cursorAdapter);
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

 }