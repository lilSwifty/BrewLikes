package com.iths.manisedighi.brewlikes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    ListView listView;
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
        listView = findViewById(R.id.membersListView);

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

        //ArrayList of all the members
       ArrayList<String> memberList = new ArrayList<>();
       memberList.add("EMMA");
       memberList.add("MANI");
       memberList.add("MILJA");
       memberList.add("MOA");
       memberList.add("PATRIK");
       memberList.add("VICTOR");
       memberList.add("VICTORIA");

       //Array Adapter for the memberListView
       ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, memberList);

       //Connects the listView to the arrayAdapter
       listView.setAdapter(arrayAdapter);
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
        if(id == R.id.cameraIcon){
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
            //TODO - Use flags here so the activities don't get put on the stack?
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


