package com.iths.manisedighi.brewlikes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

public class AboutActivity extends AppCompatActivity {

   // ImageView toolbarLogo;
    ListView listView;
    ImageView line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        //TODO - Create a imageView logo in the left upper corner

        line = findViewById(R.id.lineImageView);

        Log.d("MyLog", "Initialize activity1");
       // listView = findViewById(R.id.membersListView);

        Log.d("MyLog", "Initialize activity2");

        /*
        /**
        * When BrewLikes logo in the toolbar is clicked, MainActivity starts.
         * //TODO - Use flags here so the activities don't get put on the stack?

        toolbarLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent (AboutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }); */

        //ArrayList for all the members
       /* ArrayList<String> memberList = new ArrayList<>();
        memberList.add("EMMA");
        memberList.add("MANI");
        memberList.add("MILJA");
        memberList.add("MOA");
        memberList.add("PATRIK");
        memberList.add("VICTOR");
        memberList.add("VICTORIA"); */

        Log.d("MyLog", "Added all members");
/*
        //Array Adapter for the memberListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, memberList);

        //Connects the listView to the arrayAdapter
        listView.setAdapter(arrayAdapter);

        */
    }

    /**
     * Action bar with icons
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.camera_option, menu);
        return true;
    }

    /**
     * Handles what happens when the icons in the toolbar are clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       if(id == R.id.cameraIcon){
            Intent intent = new Intent(this, CameraActivity.class);
            startActivity(intent);
            //TODO - Use flags here so the activities don't get put on the stack?
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
