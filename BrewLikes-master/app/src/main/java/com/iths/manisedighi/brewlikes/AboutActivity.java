package com.iths.manisedighi.brewlikes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {

    ImageView toolbarLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        //TODO - Create a imageView logo in the left upper corner

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
