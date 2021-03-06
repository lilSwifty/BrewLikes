package com.iths.manisedighi.brewlikes;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class AboutActivity extends AppCompatActivity {

    ListView myListView;
    String[] persons;
    String[] descriptions;
    TypedArray image;
    ImageView line;
    ImageView logo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        line = findViewById(R.id.lineImageView);
        logo = findViewById(R.id.logoImageView);
        //Hides the BrewLikes text from the upper toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Resources res = getResources();
        myListView = findViewById(R.id.myListView);
        persons = res.getStringArray(R.array.persons);
        descriptions = res.getStringArray(R.array.descriptions);
        image = res.obtainTypedArray(R.array.images);

        ItemAdapter itemAdapter = new ItemAdapter(this, persons, descriptions,image);
        myListView.setAdapter(itemAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
                Intent showDetailActivity = new Intent(getApplicationContext(), DetailActivity.class);
                showDetailActivity.putExtra("com.example.manisedighi.brewlikes.ITEM_INDEX", p);
                startActivity(showDetailActivity);

            }
        });


        /**
         * When BrewLikes logo in the toolbar is clicked it launches MainActivity.
         */
        //TODO - Use flags here so the activities don't get put on the stack?
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent (AboutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void credit(View view){
        Intent intent = new Intent(this, CreditActivity.class);
        startActivity(intent);
    }





    /**
     * Upper toolbar with icons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.camera_icon, menu);
        return true;
    }

    /**
     * Handles what happens when the icons in the toolbar are clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.cameraIcon2){
            Intent cameraIntent = new Intent(this, RankingActivity.class);
            startActivity(cameraIntent);
            //TODO - Use flags here so the activities don't get put on the stack?
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}


