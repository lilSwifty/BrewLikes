 package com.iths.manisedighi.brewlikes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

 public class TopListActivity extends BottomNavigationBaseActivity {

     private TopListCursorAdapter cursorAdapter;
     private ListView topListView;
     private DBHelper dbHelper = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        topListView = findViewById(R.id.beerTopList);

        setupBottomNavigation();
        //initialize();
        //showTopList();
        //createRoundPicture();
        createCursorAdapter();

    }

     @SuppressLint("WrongViewCast")
     private void initialize() {
         //beerListView = findViewById(R.id.topListItem);
     }

     public void showTopList() {
         //dbHelper.getTopList();
     }

     /**
      * TODO fixa cursor-metoden från Milja
      * Sets up the Cursor Adapter
      */
     private void createCursorAdapter() {
         Cursor cursor = dbHelper.getTopListCursor();
         cursorAdapter = new TopListCursorAdapter(this, cursor);
         topListView.setAdapter(cursorAdapter);
     }

     /**
     * Makes the image of the beer round
     * TODO fixa så det blir en ölbild, och kontrollera att detta funkar
     */
    public void createRoundPicture() {
        ImageView beerImage = findViewById(R.id.beerImage);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.beer);
        RoundedBitmapDrawable roundPic = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundPic.setCircular(true);
        beerImage.setImageDrawable(roundPic);
    }


     /**
      * TODO fixa navBackClick här så man kan gå bakåt
      */
    public void onNavBackClick() {

    }

     /**
      * When pressing any of the items in the list, this method will send you to the Info Activity-class,
      * and show the beer you've pressed.
      * TODO kolla hur man skickar till rätt öl-info
      */
    public void onItemClick(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }


 }